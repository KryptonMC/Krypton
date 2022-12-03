/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.chunk

import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import java.util.EnumSet
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.DataConversion
import org.kryptonmc.krypton.util.SectionPos
import org.kryptonmc.krypton.util.executor.DefaultPoolUncaughtExceptionHandler
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.executor.ThreadPoolBuilder
import org.kryptonmc.krypton.util.executor.daemonThreadFactory
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.palette.PaletteHolder
import org.kryptonmc.krypton.world.chunk.ticket.Ticket
import org.kryptonmc.krypton.world.chunk.ticket.TicketManager
import org.kryptonmc.krypton.world.chunk.ticket.TicketType
import org.kryptonmc.krypton.world.chunk.ticket.TicketTypes
import org.kryptonmc.krypton.world.region.RegionFileManager
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ImmutableCompoundTag
import org.kryptonmc.nbt.ImmutableListTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.buildCompound
import org.kryptonmc.nbt.compound
import space.vectrix.flare.fastutil.Long2ObjectSyncMap
import java.util.function.BooleanSupplier
import java.util.function.LongFunction

@Suppress("StringLiteralDuplication") // TODO: Refactor the serialization out of this class and use constants to fix this issue
class ChunkManager(private val world: KryptonWorld) : AutoCloseable {

    val chunkMap: Long2ObjectMap<KryptonChunk> = Long2ObjectSyncMap.hashmap()
    private val playersByChunk = Long2ObjectSyncMap.hashmap<MutableSet<KryptonPlayer>>()
    private val executor = ThreadPoolBuilder.fixed(2)
        .factory(daemonThreadFactory("Chunk Loader #%d") { setUncaughtExceptionHandler(DefaultPoolUncaughtExceptionHandler(LOGGER)) })
        .build()
    private val ticketManager = TicketManager(this)
    private val regionFileManager = RegionFileManager(world.folder.resolve("region"), world.server.config.advanced.synchronizeChunkWrites)

    fun get(x: Int, z: Int): KryptonChunk? = chunkMap.get(ChunkPos.pack(x, z))

    fun get(position: Long): KryptonChunk? = chunkMap.get(position)

    fun addStartTicket(centerX: Int, centerZ: Int, onLoad: () -> Unit) {
        ticketManager.addTicket(centerX, centerZ, TicketTypes.START, 22, Unit, onLoad)
    }

    fun addPlayer(player: KryptonPlayer, x: Int, z: Int, oldX: Int, oldZ: Int, viewDistance: Int): CompletableFuture<Unit> = execute {
        ticketManager.addPlayer(x, z, oldX, oldZ, player.uuid, viewDistance)
        val pos = ChunkPos.pack(x, z)
        val oldPos = ChunkPos.pack(oldX, oldZ)
        if (pos == oldPos) {
            if (!playersByChunk.containsKey(pos)) playersByChunk.computeIfAbsent(pos, LongFunction { ConcurrentHashMap.newKeySet() }).add(player)
            return@execute // They haven't changed chunks
        }
        val oldSet = playersByChunk.get(oldPos)?.apply { remove(player) }
        if (oldSet != null && oldSet.isEmpty()) playersByChunk.remove(oldPos)
        playersByChunk.computeIfAbsent(pos, LongFunction { ConcurrentHashMap.newKeySet() }).add(player)
    }

    private inline fun execute(crossinline action: () -> Unit): CompletableFuture<Unit> = CompletableFuture.supplyAsync({ action() }, executor)

    fun removePlayer(player: KryptonPlayer, viewDistance: Int = world.server.config.world.viewDistance) {
        val x = SectionPos.blockToSection(player.position.x)
        val z = SectionPos.blockToSection(player.position.z)
        ticketManager.removePlayer(x, z, player.uuid, viewDistance)
        val pos = ChunkPos.pack(x, z)
        val set = playersByChunk.get(pos)?.apply { remove(player) }
        if (set != null && set.isEmpty()) playersByChunk.remove(pos)
    }

    fun players(position: Long): Set<KryptonPlayer> = playersByChunk.getOrDefault(position, emptySet())

    fun load(x: Int, z: Int, ticket: Ticket<*>): KryptonChunk? {
        val pos = ChunkPos.pack(x, z)
        if (chunkMap.containsKey(pos)) return chunkMap.get(pos)!!

        val position = ChunkPos(x, z)
        val nbt = regionFileManager.read(x, z) ?: return null
        val version = if (nbt.contains("DataVersion", 99)) nbt.getInt("DataVersion") else -1
        // We won't upgrade data if use of the data converter is disabled.
        if (version < KryptonPlatform.worldVersion && !world.server.config.advanced.useDataConverter) {
            DataConversion.sendWarning(LOGGER, "chunk at $x, $z")
            error("Tried to load old chunk from version $version when data conversion is disabled!")
        }

        // Don't upgrade if the version is not older than our version.
        val data = DataConversion.upgrade(nbt, MCTypeRegistry.CHUNK, version, true)
        val heightmaps = data.getCompound("Heightmaps")

        val sectionList = data.getList("sections", CompoundTag.ID)
        val sections = arrayOfNulls<ChunkSection>(world.sectionCount)
        for (i in 0 until sectionList.size()) {
            val sectionData = sectionList.getCompound(i)
            val y = sectionData.getByte("Y").toInt()
            val index = world.getSectionIndexFromSectionY(y)
            if (index >= 0 && index < sections.size) {
                val blocks = if (sectionData.contains("block_states", CompoundTag.ID)) {
                    PaletteHolder.readBlocks(sectionData.getCompound("block_states"))
                } else {
                    PaletteHolder(PaletteHolder.Strategy.BLOCKS, KryptonBlocks.AIR.defaultState)
                }
                val biomes = if (sectionData.contains("biomes", CompoundTag.ID)) {
                    PaletteHolder.readBiomes(sectionData.getCompound("biomes"))
                } else {
                    PaletteHolder(PaletteHolder.Strategy.BIOMES, Biomes.PLAINS)
                }
                val section = ChunkSection(y, blocks, biomes, sectionData.getByteArray("BlockLight"), sectionData.getByteArray("SkyLight"))
                sections[index] = section
            }
        }

        val carvingMasks = data.getCompound("CarvingMasks").let { it.getByteArray("AIR") to it.getByteArray("LIQUID") }
        val chunk =  KryptonChunk(
            world,
            position,
            sections,
            data.getLong("LastUpdate"),
            data.getLong("inhabitedTime"),
            ticket,
            carvingMasks,
            data.getCompound("Structures")
        )
        chunkMap.put(position.pack(), chunk)

        val noneOf = EnumSet.noneOf(Heightmap.Type::class.java)
        Heightmap.Type.POST_FEATURES.forEach {
            if (heightmaps.contains(it.name, LongArrayTag.ID)) chunk.setHeightmap(it, heightmaps.getLongArray(it.name)) else noneOf.add(it)
        }
        Heightmap.prime(chunk, noneOf)
        world.entityManager.load(chunk)
        return chunk
    }

    fun unload(x: Int, z: Int, requiredType: TicketType<*>, force: Boolean) {
        val loaded = get(x, z) ?: return
        if (!force && loaded.ticket.type !== requiredType) return
        save(loaded)
        chunkMap.remove(loaded.position.pack())
    }

    fun saveAll(flush: Boolean) {
        chunkMap.values.forEach(::save)
        if (flush) regionFileManager.flush()
    }

    fun save(chunk: KryptonChunk) {
        val lastUpdate = world.time
        chunk.lastUpdate = lastUpdate
        regionFileManager.write(chunk.x, chunk.z, serialize(chunk))
        world.entityManager.save(chunk)
    }

    @Suppress("UnusedPrivateMember")
    fun tick(hasTimeLeft: BooleanSupplier) {
        chunkMap.values.forEach { it.tick(players(it.position.pack()).size) }
    }

    override fun close() {
        saveAll(true)
        regionFileManager.close()
    }

    companion object {

        private val LOGGER = logger<ChunkManager>()

        @JvmStatic
        private fun serialize(chunk: KryptonChunk): CompoundTag {
            val data = buildCompound {
                putInt("DataVersion", KryptonPlatform.worldVersion)
                compound("CarvingMasks") {
                    putByteArray("AIR", chunk.carvingMasks.first)
                    putByteArray("LIQUID", chunk.carvingMasks.second)
                }
                putLong("LastUpdate", chunk.lastUpdate)
                putList("Lights", ListTag.ID)
                putList("LiquidsToBeTicked", ListTag.ID)
                putList("LiquidTicks", ListTag.ID)
                putLong("InhabitedTime", chunk.inhabitedTime)
                putList("PostProcessing", ListTag.ID)
                putString("Status", "full")
                putList("TileEntities", CompoundTag.ID)
                putList("TileTicks", CompoundTag.ID)
                putList("ToBeTicked", ListTag.ID)
                put("Structures", chunk.structures)
                putInt("xPos", chunk.position.x)
                putInt("zPos", chunk.position.z)
            }

            val sectionList = ImmutableListTag.builder(CompoundTag.ID)
            for (i in chunk.minimumLightSection until chunk.maximumLightSection) {
                val sectionIndex = chunk.world.getSectionIndexFromSectionY(i)
                // TODO: Handle light sections below and above the world
                if (sectionIndex >= 0 && sectionIndex < chunk.sections.size) {
                    val section = chunk.sections[sectionIndex]
                    val sectionData = compound {
                        putByte("Y", i.toByte())
                        // FIXME: Fix this in next update
//                        put("block_states", section.blocks.write(KryptonBlockState.CODEC::encode))
                        put("biomes", section.biomes.write { StringTag.of(it.key().asString()) })
                        if (section.blockLight.isNotEmpty()) putByteArray("BlockLight", section.blockLight)
                        if (section.skyLight.isNotEmpty()) putByteArray("SkyLight", section.skyLight)
                    }
                    sectionList.add(sectionData)
                }
            }
            data.put("sections", sectionList.build())

            val heightmapData = ImmutableCompoundTag.builder()
            chunk.heightmaps.forEach { if (it.key in Heightmap.Type.POST_FEATURES) heightmapData.putLongArray(it.key.name, it.value.rawData) }
            data.put("Heightmaps", heightmapData.build())
            return data.build()
        }
    }
}

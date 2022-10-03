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
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.DataConversion
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.pool.ThreadPoolBuilder
import org.kryptonmc.krypton.util.pool.daemonThreadFactory
import org.kryptonmc.krypton.util.pool.uncaughtExceptionHandler
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.downcast
import org.kryptonmc.krypton.world.block.palette.PaletteHolder
import org.kryptonmc.krypton.world.chunk.ticket.Ticket
import org.kryptonmc.krypton.world.chunk.ticket.TicketManager
import org.kryptonmc.krypton.world.chunk.ticket.TicketType
import org.kryptonmc.krypton.world.chunk.ticket.TicketTypes
import org.kryptonmc.krypton.world.region.RegionFileManager
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.buildCompound
import org.kryptonmc.nbt.compound
import space.vectrix.flare.fastutil.Long2ObjectSyncMap
import java.util.function.LongFunction

class ChunkManager(private val world: KryptonWorld) {

    val chunkMap: Long2ObjectMap<KryptonChunk> = Long2ObjectSyncMap.hashmap()
    private val playersByChunk = Long2ObjectSyncMap.hashmap<MutableSet<KryptonPlayer>>()
    private val executor = ThreadPoolBuilder.fixed(2).factory(daemonThreadFactory("Chunk Loader #%d") {
        uncaughtExceptionHandler { thread, exception ->
            LOGGER.error("Caught unhandled exception in thread ${thread.name}!", exception)
            world.server.stop()
        }
    }).build()
    private val ticketManager = TicketManager(this)
    private val regionFileManager = RegionFileManager(world.folder.resolve("region"), world.server.config.advanced.synchronizeChunkWrites)

    operator fun get(x: Int, z: Int): KryptonChunk? = chunkMap.get(ChunkPosition.toLong(x, z))

    operator fun get(position: Long): KryptonChunk? = chunkMap.get(position)

    fun addStartTicket(centerX: Int, centerZ: Int, onLoad: () -> Unit) {
        ticketManager.addTicket(centerX, centerZ, TicketTypes.START, 22, Unit, onLoad)
    }

    fun addPlayer(
        player: KryptonPlayer,
        x: Int,
        z: Int,
        oldX: Int,
        oldZ: Int,
        viewDistance: Int
    ): CompletableFuture<Unit> = CompletableFuture.supplyAsync({
        ticketManager.addPlayer(x, z, oldX, oldZ, player.uuid, viewDistance)
        val pos = ChunkPosition.toLong(x, z)
        val oldPos = ChunkPosition.toLong(oldX, oldZ)
        if (pos == oldPos) {
            if (!playersByChunk.containsKey(pos)) playersByChunk.computeIfAbsent(pos, LongFunction { ConcurrentHashMap.newKeySet() }).add(player)
            return@supplyAsync // They haven't changed chunks
        }
        val oldSet = playersByChunk.get(oldPos)?.apply { remove(player) }
        if (oldSet != null && oldSet.isEmpty()) playersByChunk.remove(oldPos)
        playersByChunk.computeIfAbsent(pos, LongFunction { ConcurrentHashMap.newKeySet() }).add(player)
    }, executor)

    fun removePlayer(player: KryptonPlayer, viewDistance: Int = world.server.config.world.viewDistance) {
        val x = player.location.floorX() shr 4
        val z = player.location.floorZ() shr 4
        ticketManager.removePlayer(x, z, player.uuid, viewDistance)
        val pos = ChunkPosition.toLong(x, z)
        val set = playersByChunk.get(pos)?.apply { remove(player) }
        if (set != null && set.isEmpty()) playersByChunk.remove(pos)
    }

    fun players(position: Long): Set<KryptonPlayer> = playersByChunk.getOrDefault(position, emptySet())

    fun load(x: Int, z: Int, ticket: Ticket<*>): KryptonChunk? {
        val pos = ChunkPosition.toLong(x, z)
        if (chunkMap.containsKey(pos)) return chunkMap.get(pos)!!

        val position = ChunkPosition(x, z)
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
        for (i in 0 until sectionList.size) {
            val sectionData = sectionList.getCompound(i)
            val y = sectionData.getByte("Y").toInt()
            val index = world.sectionIndexFromY(y)
            if (index >= 0 && index < sections.size) {
                val blocks = if (sectionData.contains("block_states", CompoundTag.ID)) {
                    PaletteHolder.readBlocks(sectionData.getCompound("block_states"))
                } else {
                    PaletteHolder(PaletteHolder.Strategy.BLOCKS, Blocks.AIR.defaultState.downcast())
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
        chunkMap.put(position.toLong(), chunk)

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
        chunkMap.remove(loaded.position.toLong())
    }

    fun saveAll(shouldClose: Boolean) {
        chunkMap.values.forEach(::save)
        if (shouldClose) regionFileManager.close() else regionFileManager.flush()
    }

    fun save(chunk: KryptonChunk) {
        val lastUpdate = world.time
        chunk.lastUpdate = lastUpdate
        regionFileManager.write(chunk.x, chunk.z, serialize(chunk))
        world.entityManager.save(chunk)
    }

    companion object {

        private val LOGGER = logger<ChunkManager>()

        @JvmStatic
        private fun serialize(chunk: KryptonChunk): CompoundTag {
            val data = buildCompound {
                int("DataVersion", KryptonPlatform.worldVersion)
                compound("CarvingMasks") {
                    byteArray("AIR", chunk.carvingMasks.first)
                    byteArray("LIQUID", chunk.carvingMasks.second)
                }
                long("LastUpdate", chunk.lastUpdate)
                list("Lights", ListTag.ID)
                list("LiquidsToBeTicked", ListTag.ID)
                list("LiquidTicks", ListTag.ID)
                long("InhabitedTime", chunk.inhabitedTime)
                list("PostProcessing", ListTag.ID)
                string("Status", "full")
                list("TileEntities", CompoundTag.ID)
                list("TileTicks", CompoundTag.ID)
                list("ToBeTicked", ListTag.ID)
                put("Structures", chunk.structures)
                int("xPos", chunk.position.x)
                int("zPos", chunk.position.z)
            }

            val sectionList = ListTag.immutableBuilder(CompoundTag.ID)
            for (i in chunk.minimumLightSection until chunk.maximumLightSection) {
                val sectionIndex = chunk.world.sectionIndexFromY(i)
                // TODO: Handle light sections below and above the world
                if (sectionIndex >= 0 && sectionIndex < chunk.sections.size) {
                    val section = chunk.sections[sectionIndex]
                    val sectionData = compound {
                        byte("Y", i.toByte())
                        // FIXME: Fix this in next update
//                        put("block_states", section.blocks.write(KryptonBlockState.CODEC::encode))
                        put("biomes", section.biomes.write { StringTag.of(it.key().asString()) })
                        if (section.blockLight.isNotEmpty()) byteArray("BlockLight", section.blockLight)
                        if (section.skyLight.isNotEmpty()) byteArray("SkyLight", section.skyLight)
                    }
                    sectionList.add(sectionData)
                }
            }
            data.put("sections", sectionList.build())

            val heightmapData = CompoundTag.immutableBuilder()
            chunk.heightmaps.forEach { if (it.key in Heightmap.Type.POST_FEATURES) heightmapData.longArray(it.key.name, it.value.rawData) }
            data.put("Heightmaps", heightmapData.build())
            return data.build()
        }
    }
}

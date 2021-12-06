/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import ca.spottedleaf.dataconverter.minecraft.MCDataConverter
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry
import it.unimi.dsi.fastutil.objects.ObjectArraySet
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.daemon
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.threadFactory
import org.kryptonmc.krypton.util.uncaughtExceptionHandler
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.palette.PaletteHolder
import org.kryptonmc.krypton.world.block.toNBT
import org.kryptonmc.krypton.world.chunk.ticket.Ticket
import org.kryptonmc.krypton.world.chunk.ticket.TicketManager
import org.kryptonmc.krypton.world.chunk.ticket.TicketType
import org.kryptonmc.krypton.world.chunk.ticket.TicketTypes
import org.kryptonmc.krypton.world.region.RegionFileManager
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.MutableListTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.buildCompound
import org.kryptonmc.nbt.compound
import java.util.EnumSet
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

class ChunkManager(private val world: KryptonWorld) {

    val chunkMap: MutableMap<Long, KryptonChunk> = ConcurrentHashMap()
    private val playersByChunk = ConcurrentHashMap<Long, ObjectSet<KryptonPlayer>>()
    private val executor = Executors.newFixedThreadPool(
        2,
        threadFactory("Chunk Loader #%d") {
            daemon()
            uncaughtExceptionHandler { thread, exception ->
                LOGGER.error("Caught unhandled exception in thread ${thread.name}!", exception)
                world.server.stop()
            }
        }
    )
    private val ticketManager = TicketManager(this)
    private val regionFileManager = RegionFileManager(
        world.folder.resolve("region"),
        world.server.config.advanced.synchronizeChunkWrites
    )

    operator fun get(x: Int, z: Int): KryptonChunk? = chunkMap[ChunkPosition.toLong(x, z)]

    operator fun get(position: Long): KryptonChunk? = chunkMap[position]

    fun addStartTicket(centerX: Int, centerZ: Int, onLoad: () -> Unit) {
        addTicket(centerX, centerZ, TicketTypes.START, 22, Unit, onLoad)
    }

    fun <T> addTicket(x: Int, z: Int, type: TicketType<T>, level: Int, key: T, onLoad: () -> Unit) {
        ticketManager.addTicket(x, z, type, level, key, onLoad)
    }

    fun <T> removeTicket(x: Int, z: Int, type: TicketType<T>, level: Int, key: T) {
        ticketManager.removeTicket(x, z, type, level, key)
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
            if (!playersByChunk.containsKey(pos)) playersByChunk.getOrPut(pos) { ObjectArraySet() }.add(player)
            return@supplyAsync // They haven't changed chunks
        }
        val oldSet = playersByChunk[oldPos]?.apply { remove(player) }
        if (oldSet != null && oldSet.isEmpty()) playersByChunk.remove(oldPos)
        playersByChunk.getOrPut(pos) { ObjectArraySet() }.add(player)
    }, executor)

    fun removePlayer(player: KryptonPlayer, viewDistance: Int = world.server.config.world.viewDistance) {
        val x = player.location.floorX() shr 4
        val z = player.location.floorZ() shr 4
        ticketManager.removePlayer(x, z, player.uuid, viewDistance)
        val pos = ChunkPosition.toLong(x, z)
        val set = playersByChunk[pos]?.apply { remove(player) }
        if (set != null && set.isEmpty()) playersByChunk.remove(pos)
    }

    fun players(position: Long): Set<KryptonPlayer> = playersByChunk[position] ?: emptySet()

    fun load(x: Int, z: Int, ticket: Ticket<*>): KryptonChunk? {
        val pos = ChunkPosition.toLong(x, z)
        if (chunkMap.containsKey(pos)) return chunkMap[pos]!!

        val position = ChunkPosition(x, z)
        val nbt = regionFileManager.read(x, z) ?: return null
        val version = if (nbt.contains("DataVersion", 99)) nbt.getInt("DataVersion") else -1
        // We won't upgrade data if use of the data converter is disabled.
        if (version < KryptonPlatform.worldVersion && !world.server.useDataConverter) {
            LOGGER.error("The server attempted to load a chunk from a earlier version of Minecraft when data conversion is disabled!")
            LOGGER.info("If you would like to use data conversion, provide the --upgrade-data or --use-data-converter flag(s) to the JAR on startup.")
            LOGGER.warn("Beware that this is an experimental tool and has known issues with pre-1.13 worlds.")
            LOGGER.warn("USE THIS TOOL AT YOUR OWN RISK. If the tool corrupts your data, that is YOUR responsibility!")
            error("Tried to load old chunk from version $version when data conversion is disabled!")
        }

        // Don't upgrade if the version is not older than our version.
        val data = if (world.server.useDataConverter && version < KryptonPlatform.worldVersion && nbt.isNotEmpty()) {
            MCDataConverter.convertTag(MCTypeRegistry.CHUNK, nbt.copy(), version, KryptonPlatform.worldVersion)
        } else {
            nbt
        }
        val heightmaps = data.getCompound("Heightmaps")

        val sectionList = data.getList("sections", CompoundTag.ID)
        val sections = arrayOfNulls<ChunkSection>(world.sectionCount)
        for (i in sectionList.indices) {
            val sectionData = sectionList.getCompound(i)
            val y = sectionData.getByte("Y").toInt()
            val index = world.sectionIndexFromY(y)
            if (index >= 0 && index < sections.size) {
                val blocks = if (sectionData.contains("block_states", CompoundTag.ID)) {
                    PaletteHolder.readBlocks(sectionData.getCompound("block_states"))
                } else {
                    PaletteHolder(PaletteHolder.Strategy.BLOCKS, Blocks.AIR)
                }
                val biomes = if (sectionData.contains("biomes", CompoundTag.ID)) {
                    PaletteHolder.readBiomes(sectionData.getCompound("biomes"))
                } else {
                    PaletteHolder(PaletteHolder.Strategy.BIOMES, Biomes.PLAINS)
                }
                val section = ChunkSection(
                    y,
                    blocks,
                    biomes,
                    sectionData.getByteArray("BlockLight"),
                    sectionData.getByteArray("SkyLight")
                )
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
        chunkMap[position.toLong()] = chunk

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
        chunkMap.values.forEach { save(it) }
        if (shouldClose) regionFileManager.close() else regionFileManager.flush()
    }

    fun save(chunk: KryptonChunk) {
        val lastUpdate = world.time
        chunk.lastUpdate = lastUpdate
        regionFileManager.write(chunk.x, chunk.z, chunk.serialize())
        world.entityManager.save(chunk)
    }

    companion object {

        private val LOGGER = logger<ChunkManager>()

        @JvmStatic
        private fun KryptonChunk.serialize(): CompoundTag {
            val data = buildCompound {
                int("DataVersion", KryptonPlatform.worldVersion)
                compound("CarvingMasks") {
                    byteArray("AIR", carvingMasks.first)
                    byteArray("LIQUID", carvingMasks.second)
                }
                long("LastUpdate", lastUpdate)
                list("Lights", ListTag.ID)
                list("LiquidsToBeTicked", ListTag.ID)
                list("LiquidTicks", ListTag.ID)
                long("InhabitedTime", inhabitedTime)
                list("PostProcessing", ListTag.ID)
                string("Status", "full")
                list("TileEntities", CompoundTag.ID)
                list("TileTicks", CompoundTag.ID)
                list("ToBeTicked", ListTag.ID)
                put("Structures", structures)
                int("xPos", position.x)
                int("zPos", position.z)
            }

            val sectionList = MutableListTag(elementType = CompoundTag.ID)
            for (i in minimumLightSection until maximumLightSection) {
                val sectionIndex = world.sectionIndexFromY(i)
                // TODO: Handle light sections below and above the world
                if (sectionIndex >= 0 && sectionIndex < sections.size) {
                    val section = sections[sectionIndex]
                    val tag = compound {
                        byte("Y", i.toByte())
                        put("block_states", section.blocks.write(Block::toNBT))
                        put("biomes", section.biomes.write { StringTag.of(it.key().asString()) })
                        if (section.blockLight.isNotEmpty()) byteArray("BlockLight", section.blockLight)
                        if (section.skyLight.isNotEmpty()) byteArray("SkyLight", section.skyLight)
                    }
                    sectionList.add(tag)
                }
            }
            data.put("sections", sectionList)

            val heightmapData = CompoundTag.builder()
            heightmaps.forEach { if (it.key in Heightmap.Type.POST_FEATURES) heightmapData.longArray(it.key.name, it.value.data.data) }
            data.put("Heightmaps", heightmapData.build())
            return data.build()
        }
    }
}

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
import org.apache.logging.log4j.LogManager
import java.util.EnumSet
import java.util.concurrent.ConcurrentHashMap
import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.DataConversion
import org.kryptonmc.krypton.coordinate.SectionPos
import org.kryptonmc.krypton.util.executor.DefaultPoolUncaughtExceptionHandler
import org.kryptonmc.krypton.util.executor.ThreadPoolBuilder
import org.kryptonmc.krypton.util.executor.daemonThreadFactory
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.world.chunk.data.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.palette.PaletteHolder
import org.kryptonmc.krypton.world.chunk.data.ChunkSection
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
import java.util.function.Consumer
import java.util.function.LongFunction

@Suppress("StringLiteralDuplication") // TODO: Refactor the serialization out of this class and use constants to fix this issue
class ChunkManager(private val world: KryptonWorld) : AutoCloseable {

    private val chunkMap = Long2ObjectSyncMap.hashmap<KryptonChunk>()
    private val playersByChunk = Long2ObjectSyncMap.hashmap<MutableSet<KryptonPlayer>>()
    private val regionFileManager = RegionFileManager(world.folder.resolve("region"), world.server.config.advanced.synchronizeChunkWrites)

    fun chunks(): Collection<KryptonChunk> = chunkMap.values

    fun getChunk(x: Int, z: Int): KryptonChunk? = chunkMap.get(ChunkPos.pack(x, z))

    fun getChunk(position: ChunkPos): KryptonChunk? = chunkMap.get(position.pack())

    fun loadStartingArea(centerX: Int, centerZ: Int, onLoad: Consumer<KryptonChunk>) {
        for (i in 0 until STARTING_AREA_SIZE) {
            val pos = Maths.chunkInSpiral(i, centerX, centerZ)
            val chunk = loadChunk(pos)
            if (chunk != null) onLoad.accept(chunk)
        }
    }

    fun updatePlayerPosition(player: KryptonPlayer, oldPos: ChunkPos, newPos: ChunkPos, viewDistance: Int) {
        val chunksInRange = (viewDistance * 2 + 1) * (viewDistance * 2 + 1)
        for (i in 0 until chunksInRange) {
            val pos = Maths.chunkInSpiral(i, newPos.x, newPos.z)
            if (getChunk(pos) != null) continue
            loadChunk(pos)
        }

        if (oldPos == newPos) {
            if (playersByChunk.containsKey(newPos.pack())) return
            playersByChunk.computeIfAbsent(newPos.pack(), LongFunction { ConcurrentHashMap.newKeySet() }).add(player)
            return
        }

        val oldSet = playersByChunk.get(oldPos.pack())
        if (oldSet != null) {
            oldSet.remove(player)
            if (oldSet.isEmpty()) playersByChunk.remove(oldPos.pack())
        }
        playersByChunk.computeIfAbsent(newPos.pack(), LongFunction { ConcurrentHashMap.newKeySet() }).add(player)
    }

    fun removePlayer(player: KryptonPlayer) {
        val viewDistance = world.server.config.world.viewDistance
        val chunkPos = ChunkPos(SectionPos.blockToSection(player.position.x), SectionPos.blockToSection(player.position.z))
        val packedPos = chunkPos.pack()

        val chunksInRange = (viewDistance * 2 + 1) * (viewDistance * 2 + 1)
        for (i in 0 until chunksInRange) {
            val pos = Maths.chunkInSpiral(i, chunkPos.x, chunkPos.z)
            val chunk = getChunk(pos)
            if (chunk == null) {
                LOGGER.warn("Chunk at $pos is not loaded, but player ${player.name} is in it!")
                continue
            }

            val playerSet = playersByChunk.get(pos.pack()) ?: continue
            if (playerSet.isEmpty()) {
                // Since there's no players in this chunk, we're safe to unload it
                unloadChunk(chunkPos.x, chunkPos.z)
            }
        }

        val playerSet = playersByChunk.get(packedPos)
        if (playerSet == null) {
            LOGGER.warn("Chunk at $chunkPos, which disconnecting player ${player.name} is in, does not have any players registered in it!")
            return
        }

        playerSet.remove(player)
        if (playerSet.isEmpty()) {
            playersByChunk.remove(packedPos)
            // This will likely have been skipped earlier in the main loop, so we'll just make sure this chunk gets unloaded if it needs to be.
            unloadChunk(chunkPos.x, chunkPos.z)
        }
    }

    private fun getPlayersByChunk(position: Long): Set<KryptonPlayer> = playersByChunk.getOrDefault(position, emptySet())

    fun loadChunk(pos: ChunkPos): KryptonChunk? {
        val packed = pos.pack()
        if (chunkMap.containsKey(packed)) return chunkMap.get(packed)!!

        val nbt = regionFileManager.read(pos.x, pos.z) ?: return null
        val version = if (nbt.contains("DataVersion", 99)) nbt.getInt("DataVersion") else -1
        // We won't upgrade data if use of the data converter is disabled.
        if (version < KryptonPlatform.worldVersion && !world.server.config.advanced.useDataConverter) {
            DataConversion.sendWarning(LOGGER, "chunk at $pos")
            error("Tried to load old chunk from version $version when data conversion is disabled!")
        }

        // Don't upgrade if the version is not older than our version.
        val data = DataConversion.upgrade(nbt, MCTypeRegistry.CHUNK, version, true)
        val heightmaps = data.getCompound("Heightmaps")

        val sectionList = data.getList("sections", CompoundTag.ID)
        val sections = arrayOfNulls<ChunkSection>(world.sectionCount())
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
                    PaletteHolder(PaletteHolder.Strategy.BIOMES, Biomes.PLAINS.get())
                }
                val section = ChunkSection(y, blocks, biomes, sectionData.getByteArray("BlockLight"), sectionData.getByteArray("SkyLight"))
                sections[index] = section
            }
        }

        val carvingMasks = data.getCompound("CarvingMasks").let { it.getByteArray("AIR") to it.getByteArray("LIQUID") }
        val chunk =  KryptonChunk(
            world,
            pos,
            sections,
            data.getLong("LastUpdate"),
            data.getLong("inhabitedTime"),
            carvingMasks,
            data.getCompound("Structures")
        )
        chunkMap.put(packed, chunk)

        val noneOf = EnumSet.noneOf(Heightmap.Type::class.java)
        Heightmap.Type.POST_FEATURES.forEach {
            if (heightmaps.contains(it.name, LongArrayTag.ID)) chunk.setHeightmap(it, heightmaps.getLongArray(it.name)) else noneOf.add(it)
        }
        Heightmap.prime(chunk, noneOf)
        world.entityManager.loadAllInChunk(chunk)
        return chunk
    }

    fun unloadChunk(x: Int, z: Int) {
        val loaded = getChunk(x, z) ?: return
        saveChunk(loaded)
        chunkMap.remove(loaded.position.pack())
    }

    fun saveAllChunks(flush: Boolean) {
        chunkMap.values.forEach(::saveChunk)
        if (flush) regionFileManager.flush()
    }

    private fun saveChunk(chunk: KryptonChunk) {
        val lastUpdate = world.time
        chunk.lastUpdate = lastUpdate
        regionFileManager.write(chunk.x, chunk.z, serialize(chunk))
        world.entityManager.saveAllInChunk(chunk)
    }

    @Suppress("UnusedPrivateMember")
    fun tick(hasTimeLeft: BooleanSupplier) {
        chunkMap.values.forEach { it.tick(getPlayersByChunk(it.position.pack()).size) }
    }

    override fun close() {
        saveAllChunks(true)
        regionFileManager.close()
    }

    companion object {

        private const val STARTING_AREA_RADIUS = 12
        private const val STARTING_AREA_SIZE = (STARTING_AREA_RADIUS * 2 + 1) * (STARTING_AREA_RADIUS * 2 + 1)
        private val LOGGER = LogManager.getLogger()

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
            for (i in chunk.minimumLightSection() until chunk.maximumLightSection()) {
                val sectionIndex = chunk.world.getSectionIndexFromSectionY(i)
                // TODO: Handle light sections below and above the world
                if (sectionIndex >= 0 && sectionIndex < chunk.sections().size) {
                    val section = chunk.sections()[sectionIndex]
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
            chunk.heightmaps.forEach { if (it.key in Heightmap.Type.POST_FEATURES) heightmapData.putLongArray(it.key.name, it.value.rawData()) }
            data.put("Heightmaps", heightmapData.build())
            return data.build()
        }
    }
}

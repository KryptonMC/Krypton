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

import ca.spottedleaf.starlight.SWMRNibbleArray
import ca.spottedleaf.starlight.StarLightEngine
import ca.spottedleaf.starlight.StarLightManager
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.util.datafix.DATA_FIXER
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.light.LightLayer
import org.kryptonmc.krypton.world.region.RegionFileManager
import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.buildCompound
import org.kryptonmc.nbt.compound
import java.util.EnumSet
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class ChunkManager(val world: KryptonWorld) {

    val chunkMap = ConcurrentHashMap<Long, KryptonChunk>()
    val lightEngine = StarLightManager(this, world.dimensionType.hasSkylight)
    private val regionFileManager = RegionFileManager(world.folder.resolve("region"), world.server.config.advanced.synchronizeChunkWrites)

    private val chunkCache: Cache<ChunkPosition, KryptonChunk> = Caffeine.newBuilder()
        .maximumSize(512)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build()

    operator fun get(x: Int, z: Int): KryptonChunk? = chunkCache.getIfPresent(ChunkPosition(x, z))

    fun load(positions: List<ChunkPosition>): List<KryptonChunk> {
        val chunks = mutableListOf<KryptonChunk>()
        positions.forEach {
            val chunk = load(it.x, it.z)
            chunkMap[it.toLong()] = chunk
            chunks += chunk
        }
        return chunks
    }

    fun load(x: Int, z: Int): KryptonChunk {
        chunkMap[ChunkPosition.toLong(x, z)]?.let { return it }

        val position = ChunkPosition(x, z)
        val nbt = regionFileManager.read(position)
        val version = if (nbt.contains("DataVersion", 99)) nbt.getInt("DataVersion") else -1
        val data = (DATA_FIXER.update(References.CHUNK, Dynamic(NBTOps, nbt), version, KryptonPlatform.worldVersion).value as CompoundTag).getCompound("Level")
        val heightmaps = data.getCompound("Heightmaps")

        // Light data
        val blockNibbles = StarLightEngine.getFilledEmptyLight(world)
        val skyNibbles = StarLightEngine.getFilledEmptyLight(world)
        val minSection = world.minimumSection - 1
        val hasSkyLight = world.dimensionType.hasSkylight

        val sectionList = data.getList("Sections", CompoundTag.ID)
        val sections = arrayOfNulls<ChunkSection>(world.sectionCount)
        for (i in sectionList.indices) {
            val sectionData = sectionList.getCompound(i)
            val y = sectionData.getByte("Y").toInt()
            if (y == -1 || y == 16) continue
            if (sectionData.contains("Palette", ListTag.ID) && sectionData.contains("BlockStates", LongArrayTag.ID)) {
                val section = ChunkSection(y)
                section.palette.load(sectionData.getList("Palette", CompoundTag.ID), sectionData.getLongArray("BlockStates"))
                section.recount()
                if (!section.isEmpty()) sections[world.sectionIndexFromY(y)] = section
            }
            if (nbt["isLightOn"] != null) {
                blockNibbles[y - minSection] = if (sectionData.contains("BlockLight", ByteArrayTag.ID)) {
                    SWMRNibbleArray(sectionData.getByteArray("BlockLight").clone(), sectionData.getInt("starlight.blocklight_state"))
                } else {
                    SWMRNibbleArray(null, sectionData.getInt("starlight.blocklight_state"))
                }
                if (hasSkyLight) skyNibbles[y - minSection] = if (sectionData.contains("SkyLight", ByteArrayTag.ID)) {
                    SWMRNibbleArray(sectionData.getByteArray("SkyLight").clone(), sectionData.getInt("starlight.skylight_state"))
                } else {
                    SWMRNibbleArray(null, sectionData.getInt("starlight.skylight_state"))
                }
            }
        }

        val carvingMasks = data.getCompound("CarvingMasks").let {
            it.getByteArray("AIR") to it.getByteArray("LIQUID")
        }
        val biomes = KryptonBiomeContainer(world.server.registryHolder.registryOrThrow(InternalResourceKeys.BIOME) as KryptonRegistry<KryptonBiome>, world, position, world.generator.biomeGenerator)
        val chunk =  KryptonChunk(
            world,
            position,
            sections,
            biomes,
            data.getLong("LastUpdate"),
            data.getLong("inhabitedTime"),
            carvingMasks,
            data.getCompound("Structures")
        )
        chunkMap[position.toLong()] = chunk

        chunk.blockNibbles = blockNibbles
        chunk.skyNibbles = skyNibbles

        val noneOf = EnumSet.noneOf(Heightmap.Type::class.java)
        Heightmap.Type.POST_FEATURES.forEach {
            if (heightmaps.contains(it.name, LongArrayTag.ID)) chunk.setHeightmap(it, heightmaps.getLongArray(it.name)) else noneOf.add(it)
        }
        Heightmap.prime(chunk, noneOf)
        lightEngine.lightChunk(chunk, true)
        return chunk
    }

    fun saveAll() = chunkMap.values.forEach { save(it) }

    fun save(chunk: KryptonChunk) {
        val lastUpdate = world.time
        chunk.lastUpdate = lastUpdate
        regionFileManager.write(chunk.position, chunk.serialize())
    }

    fun onLightUpdate(layer: LightLayer, x: Int, y: Int, z: Int) = chunkCache.getIfPresent(ChunkPosition(x, z))?.onLightUpdate(layer, y)
}

private fun KryptonChunk.serialize(): CompoundTag {
    val data = buildCompound {
        intArray("Biomes", biomes.write())
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

    val lightEngine = world.chunkManager.lightEngine
    val sectionList = ListTag(elementType = CompoundTag.ID)
    for (i in lightEngine.minLightSection until lightEngine.maxLightSection) {
        val section = sections.asSequence().filter { it != null && it.y shr 4 == i }.firstOrNull()
        val blockNibble = blockNibbles[i - lightEngine.minLightSection].saveState
        val skyNibble = skyNibbles[i - lightEngine.minLightSection].saveState
        if (section != null || blockNibble != null || skyNibble != null) {
            val sectionData = buildCompound { byte("Y", (i and 255).toByte()) }
            section?.palette?.save(sectionData)
            if (blockNibble != null) {
                blockNibble.data?.let { sectionData.byteArray("BlockLight", it) }
                sectionData.int("starlight.blocklight_state", blockNibble.state)
            }
            if (skyNibble != null) {
                skyNibble.data?.let { sectionData.byteArray("SkyLight", it) }
                sectionData.int("starlight.skylight_state", skyNibble.state)
            }
            sectionList.add(sectionData.build())
        }
    }
    data.put("Sections", sectionList)

    val heightmapData = CompoundTag.builder()
    heightmaps.forEach {
        if (it.key in Heightmap.Type.POST_FEATURES) heightmapData.longArray(it.key.name, it.value.data.data)
    }
    data.put("Heightmaps", heightmapData.build())
    return compound {
        int("DataVersion", CHUNK_DATA_VERSION)
        put("Level", data.build())
    }
}

const val CHUNK_DATA_VERSION = 2578

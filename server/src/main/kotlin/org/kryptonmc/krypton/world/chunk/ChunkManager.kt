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

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.mojang.serialization.Dynamic
import org.kryptonmc.api.world.Biome
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.util.datafix.DATA_FIXER
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.region.RegionFileManager
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.buildCompound
import org.kryptonmc.nbt.compound
import java.util.EnumSet
import java.util.concurrent.TimeUnit

class ChunkManager(private val world: KryptonWorld) {

    private val regionFileManager = RegionFileManager(world.folder.resolve("region"), world.server.config.advanced.synchronizeChunkWrites)
    private val chunkCache: Cache<Long, KryptonChunk> = Caffeine.newBuilder()
        .maximumSize(512)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build()

    operator fun get(x: Int, z: Int): KryptonChunk? = chunkCache.getIfPresent(ChunkPosition.toLong(x, z))

    fun load(positions: List<ChunkPosition>): List<KryptonChunk> {
        val chunks = mutableListOf<KryptonChunk>()
        positions.forEach {
            val chunk = load(it.x, it.z)
            world.chunkMap[it.toLong()] = chunk
            chunks += chunk
        }
        return chunks
    }

    fun load(x: Int, z: Int): KryptonChunk {
        val cachedChunk = chunkCache.getIfPresent(ChunkPosition.toLong(x, z))
        if (cachedChunk != null) return cachedChunk

        val position = ChunkPosition(x, z)
        val nbt = regionFileManager.read(position)
        val version = if (nbt.contains("DataVersion", 99)) nbt.getInt("DataVersion") else -1
        val data = (DATA_FIXER.update(References.CHUNK, Dynamic(NBTOps, nbt), version, ServerInfo.WORLD_VERSION).value as CompoundTag).getCompound("Level")
        val heightmaps = data.getCompound("Heightmaps")

        val sectionList = data.getList("Sections", CompoundTag.ID)
        val sections = arrayOfNulls<ChunkSection>(world.sectionCount)
        for (i in sectionList.indices) {
            val sectionData = sectionList.getCompound(i)
            val y = sectionData.getByte("Y").toInt()
            if (y == -1 || y == 16) continue
            if (sectionData.contains("Palette", ListTag.ID) && sectionData.contains("BlockStates", LongArrayTag.ID)) {
                val section = ChunkSection(
                    y,
                    sectionData.getByteArray("BlockLight"),
                    sectionData.getByteArray("SkyLight")
                )
                section.palette.load(sectionData.getList("Palette", CompoundTag.ID), sectionData.getLongArray("BlockStates"))
                section.recount()
                if (!section.isEmpty()) sections[world.sectionIndexFromY(y)] = section
            }
        }

        val carvingMasks = data.getCompound("CarvingMasks").let {
            it.getByteArray("AIR") to it.getByteArray("LIQUID")
        }
        val chunk =  KryptonChunk(
            world,
            position,
            sections,
            data.getIntArray("Biomes").map { Biome.fromId(it) },
            data.getLong("LastUpdate"),
            data.getLong("inhabitedTime"),
            carvingMasks,
            data.getCompound("Structures")
        )
        chunkCache.put(position.toLong(), chunk)

        val noneOf = EnumSet.noneOf(Heightmap.Type::class.java)
        Heightmap.Type.POST_FEATURES.forEach {
            if (heightmaps.contains(it.name, LongArrayTag.ID)) chunk.setHeightmap(it, heightmaps.getLongArray(it.name)) else noneOf.add(it)
        }
        Heightmap.prime(chunk, noneOf)
        return chunk
    }

    fun saveAll() = world.chunkMap.values.forEach { save(it) }

    fun save(chunk: KryptonChunk) {
        val lastUpdate = world.time
        chunkCache.invalidate(chunk.position.toLong())
        chunk.lastUpdate = lastUpdate
        regionFileManager.write(chunk.position, chunk.serialize())
    }
}

private fun KryptonChunk.serialize(): CompoundTag {
    val data = buildCompound {
        intArray("Biomes", biomes.map { it.id }.toIntArray())
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

    val sectionList = ListTag(elementType = CompoundTag.ID)
    for (i in minimumLightSection until maximumLightSection) {
        val section = sections.asSequence().filter { it != null && it.y shr 4 == i }.firstOrNull() ?: continue
        sectionList.add(compound {
            byte("Y", (i and 255).toByte())
            section.palette.save(this)
            if (section.blockLight.isNotEmpty()) byteArray("BlockLight", section.blockLight)
            if (section.skyLight.isNotEmpty()) byteArray("SkyLight", section.skyLight)
        })
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

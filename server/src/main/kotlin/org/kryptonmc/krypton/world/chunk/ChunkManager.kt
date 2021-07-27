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
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.data.ChunkSerializer
import org.kryptonmc.krypton.world.region.RegionFileManager
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.buildCompound
import org.kryptonmc.nbt.compound
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
        val tag = regionFileManager.read(position)?.getCompound("Level") ?: CompoundTag()
        return (ChunkSerializer.read(world, position, tag) as? ProtoKryptonChunk)?.wrapped ?: error("Cannot load proto chunks yet!")
    }

    fun saveAll() = world.chunkMap.values.forEach { save(it) }

    fun save(chunk: KryptonChunk) {
        chunkCache.invalidate(chunk.position.toLong())
        regionFileManager.write(chunk.position, chunk.serialize())
    }

    companion object {

        val MAX_DISTANCE = 33 + ChunkStatus.MAX_DISTANCE
    }
}

private fun KryptonChunk.serialize(): CompoundTag {
    val data = buildCompound {
        intArray("Biomes", biomes.map { it.id }.toIntArray())
        list("Lights", ListTag.ID)
        list("LiquidsToBeTicked", ListTag.ID)
        list("LiquidTicks", ListTag.ID)
        long("InhabitedTime", inhabitedTime)
        list("PostProcessing", ListTag.ID)
        string("Status", "full")
        list("TileEntities", CompoundTag.ID)
        list("TileTicks", CompoundTag.ID)
        list("ToBeTicked", ListTag.ID)
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

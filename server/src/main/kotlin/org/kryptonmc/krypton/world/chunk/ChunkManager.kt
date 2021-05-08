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
import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import org.kryptonmc.krypton.api.registry.toNamespacedKey
import org.kryptonmc.krypton.api.world.Biome
import org.kryptonmc.krypton.util.calculateBits
import org.kryptonmc.krypton.world.Heightmap.Type.MOTION_BLOCKING
import org.kryptonmc.krypton.world.Heightmap.Type.OCEAN_FLOOR
import org.kryptonmc.krypton.world.Heightmap.Type.WORLD_SURFACE
import org.kryptonmc.krypton.world.HeightmapBuilder
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.data.BitStorage
import org.kryptonmc.krypton.world.region.RegionFileManager
import java.nio.file.Path
import java.util.LinkedList
import java.util.concurrent.TimeUnit

class ChunkManager(private val world: KryptonWorld) {

    private val worldFolder = Path.of(world.name)
    private val regionFileManager = RegionFileManager(worldFolder.resolve("region"), world.server.config.advanced.synchronizeChunkWrites)

    private val chunkCache: Cache<ChunkPosition, KryptonChunk> = Caffeine.newBuilder()
        .maximumSize(512)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build()

    fun load(positions: List<ChunkPosition>): List<KryptonChunk> {
        val chunks = mutableListOf<KryptonChunk>()
        positions.forEach { position -> load(position)?.let { chunks += it } }
        world.chunks += chunks
        return chunks
    }

    fun load(position: ChunkPosition): KryptonChunk? {
        val cachedChunk = chunkCache.getIfPresent(position)
        if (cachedChunk != null) return cachedChunk

        val nbt = regionFileManager.read(position).getCompound("Level")
        if (nbt == CompoundBinaryTag.empty()) return null
        val heightmaps = nbt.getCompound("Heightmaps")

        val sections = nbt.getList("Sections").map { section ->
            section as CompoundBinaryTag

            val palette = LinkedList(section.getList("Palette").map { block ->
                block as CompoundBinaryTag
                val name = block.getString("Name").toNamespacedKey()
                if (name == ChunkBlock.AIR.name) {
                    ChunkBlock.AIR
                } else {
                    ChunkBlock(name, block.getCompound("Properties").associate { it.key to (it.value as StringBinaryTag).value() })
                }
            })

            ChunkSection(
                section.getByte("Y").toInt(),
                section.getByteArray("BlockLight"),
                section.getByteArray("SkyLight"),
                palette,
                BitStorage(palette.size.calculateBits(), 4096, section.getLongArray("BlockStates"))
            )
        } as MutableList<ChunkSection>

        val carvingMasks = nbt.getCompound("CarvingMasks").let { it.getByteArray("AIR") to it.getByteArray("LIQUID") }
        return KryptonChunk(
            world,
            position,
            sections,
            nbt.getIntArray("Biomes").map { Biome.fromId(it) },
            nbt.getLong("LastUpdate"),
            nbt.getLong("inhabitedTime"),
            listOf(
                HeightmapBuilder(heightmaps.get("MOTION_BLOCKING") as LongArrayBinaryTag, MOTION_BLOCKING),
                HeightmapBuilder(heightmaps.get("OCEAN_FLOOR") as LongArrayBinaryTag, OCEAN_FLOOR),
                HeightmapBuilder(heightmaps.get("WORLD_SURFACE") as LongArrayBinaryTag, WORLD_SURFACE)
            ),
            carvingMasks,
            nbt.getCompound("Structures")
        ).apply { chunkCache.put(position, this) }
    }

    fun saveAll() = world.chunks.forEach { save(it) }

    fun save(chunk: KryptonChunk) {
        val lastUpdate = world.time
        chunkCache.invalidate(chunk.position)
        chunk.lastUpdate = lastUpdate
        regionFileManager.write(chunk.position, chunk.serialize())
    }
}

private fun KryptonChunk.serialize(): CompoundBinaryTag {
    val sections = sections.map { section ->
        val palette = section.palette.map { entry ->
            val properties = CompoundBinaryTag.builder().apply { entry.properties.forEach { putString(it.key, it.value) } }.build()
            CompoundBinaryTag.builder()
                .putString("Name", entry.name.toString())
                .put("Properties", properties)
                .build()
        }

        CompoundBinaryTag.builder()
            .putByte("Y", section.y.toByte())
            .apply {
                if (section.blockLight.isNotEmpty()) putByteArray("BlockLight", section.blockLight)
                if (section.skyLight.isNotEmpty()) putByteArray("SkyLight", section.skyLight)
                if (section.blockStates.isNotEmpty()) putLongArray("BlockStates", section.blockStates.data)
                if (palette.isNotEmpty()) put("Palette", ListBinaryTag.of(BinaryTagTypes.COMPOUND, palette))
            }
            .build()
    }

    return CompoundBinaryTag.builder()
        .putInt("DataVersion", CHUNK_DATA_VERSION)
        .put("Level", CompoundBinaryTag.builder()
            .putIntArray("Biomes", biomes.map { it.id }.toIntArray())
            .put("CarvingMasks", CompoundBinaryTag.builder()
                .putByteArray("AIR", carvingMasks.first)
                .putByteArray("LIQUID", carvingMasks.second)
                .build())
            .put("Heightmaps", CompoundBinaryTag.builder()
                .putLongArray("MOTION_BLOCKING", heightmaps.getValue(MOTION_BLOCKING).data.data)
                .putLongArray("MOTION_BLOCKING_NO_LEAVES", LongArray(0))
                .putLongArray("OCEAN_FLOOR", heightmaps.getValue(OCEAN_FLOOR).data.data)
                .putLongArray("OCEAN_FLOOR_WG", LongArray(0))
                .putLongArray("WORLD_SURFACE", heightmaps.getValue(WORLD_SURFACE).data.data)
                .putLongArray("WORLD_SURFACE_WG", LongArray(0))
                .build())
            .putLong("LastUpdate", lastUpdate)
            .put("Lights", ListBinaryTag.empty())
            .put("LiquidsToBeTicked", ListBinaryTag.empty())
            .put("LiquidTicks", ListBinaryTag.empty())
            .putLong("InhabitedTime", inhabitedTime)
            .put("PostProcessing", ListBinaryTag.empty())
            .put("Sections", ListBinaryTag.of(BinaryTagTypes.COMPOUND, sections))
            .putString("Status", "full")
            .put("TileEntities", ListBinaryTag.empty())
            .put("TileTicks", ListBinaryTag.empty())
            .put("ToBeTicked", ListBinaryTag.empty())
            .put("Structures", structures)
            .putInt("xPos", position.x)
            .putInt("zPos", position.z)
            .build())
        .build()
}

const val CHUNK_DATA_VERSION = 2578

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
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTLongArray
import org.jglrxavpok.hephaistos.nbt.NBTString
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import org.kryptonmc.api.util.toKey
import org.kryptonmc.api.world.Biome
import org.kryptonmc.krypton.util.calculateBits
import org.kryptonmc.krypton.world.Heightmap.Type.MOTION_BLOCKING
import org.kryptonmc.krypton.world.Heightmap.Type.OCEAN_FLOOR
import org.kryptonmc.krypton.world.Heightmap.Type.WORLD_SURFACE
import org.kryptonmc.krypton.world.HeightmapBuilder
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.data.BitStorage
import org.kryptonmc.krypton.world.region.RegionFileManager
import org.kryptonmc.krypton.world.transform
import java.util.LinkedList
import java.util.concurrent.TimeUnit

class ChunkManager(private val world: KryptonWorld) {

    private val regionFileManager = RegionFileManager(world.folder.resolve("region"), world.server.config.advanced.synchronizeChunkWrites)
    private val chunkCache: Cache<ChunkPosition, KryptonChunk> = Caffeine.newBuilder()
        .maximumSize(512)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build()

    fun load(positions: List<ChunkPosition>): List<KryptonChunk> {
        val chunks = mutableListOf<KryptonChunk>()
        positions.forEach { chunks.add(load(it)) }
        world.chunks += chunks
        return chunks
    }

    fun load(position: ChunkPosition): KryptonChunk {
        val cachedChunk = chunkCache.getIfPresent(position)
        if (cachedChunk != null) return cachedChunk

        val nbt = regionFileManager.read(position).getCompound("Level")
        val heightmaps = nbt.getCompound("Heightmaps")

        val sections = nbt.getList<NBTCompound>("Sections").map { section ->
            val palette = LinkedList(section.getList<NBTCompound>("Palette").map { block ->
                val name = block.getString("Name").toKey()
                if (name == ChunkBlock.AIR.name) {
                    ChunkBlock.AIR
                } else {
                    ChunkBlock(name, block.getCompound("Properties").transform { it.key to (it.value as NBTString).value })
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

        val carvingMasks = nbt.getCompound("CarvingMasks").let {
            it.getByteArray("AIR") to it.getByteArray("LIQUID")
        }
        return KryptonChunk(
            world,
            position,
            sections,
            nbt.getIntArray("Biomes").map { Biome.fromId(it) },
            nbt.getLong("LastUpdate"),
            nbt.getLong("inhabitedTime"),
            listOf(
                HeightmapBuilder(heightmaps["MOTION_BLOCKING"] as NBTLongArray, MOTION_BLOCKING),
                HeightmapBuilder(heightmaps["OCEAN_FLOOR"] as NBTLongArray, OCEAN_FLOOR),
                HeightmapBuilder(heightmaps["WORLD_SURFACE"] as NBTLongArray, WORLD_SURFACE)
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

private fun KryptonChunk.serialize(): NBTCompound {
    val sections = sections.map { section ->
        val palette = section.palette.map { entry ->
            val properties = NBTCompound().apply { entry.properties.forEach { setString(it.key, it.value) } }
            NBTCompound()
                .setString("Name", entry.name.toString())
                .set("Properties", properties)
        }

        NBTCompound()
            .setByte("Y", section.y.toByte())
            .apply {
                if (section.blockLight.isNotEmpty()) setByteArray("BlockLight", section.blockLight)
                if (section.skyLight.isNotEmpty()) setByteArray("SkyLight", section.skyLight)
                if (section.blockStates.isNotEmpty()) setLongArray("BlockStates", section.blockStates.data)
                if (palette.isNotEmpty()) set("Palette", NBTList<NBTCompound>(NBTTypes.TAG_Compound).apply { palette.forEach { add(it) } })
            }
    }

    return NBTCompound()
        .setInt("DataVersion", CHUNK_DATA_VERSION)
        .set("Level", NBTCompound()
            .setIntArray("Biomes", biomes.map { it.id }.toIntArray())
            .set("CarvingMasks", NBTCompound()
                .setByteArray("AIR", carvingMasks.first)
                .setByteArray("LIQUID", carvingMasks.second))
            .set("Heightmaps", NBTCompound()
                .setLongArray("MOTION_BLOCKING", heightmaps.getValue(MOTION_BLOCKING).data.data)
                .setLongArray("MOTION_BLOCKING_NO_LEAVES", LongArray(0))
                .setLongArray("OCEAN_FLOOR", heightmaps.getValue(OCEAN_FLOOR).data.data)
                .setLongArray("OCEAN_FLOOR_WG", LongArray(0))
                .setLongArray("WORLD_SURFACE", heightmaps.getValue(WORLD_SURFACE).data.data)
                .setLongArray("WORLD_SURFACE_WG", LongArray(0)))
            .setLong("LastUpdate", lastUpdate)
            .set("Lights", NBTList<NBT>(NBTTypes.TAG_List))
            .set("LiquidsToBeTicked", NBTList<NBT>(NBTTypes.TAG_List))
            .set("LiquidTicks", NBTList<NBT>(NBTTypes.TAG_List))
            .setLong("InhabitedTime", inhabitedTime)
            .set("PostProcessing", NBTList<NBT>(NBTTypes.TAG_List))
            .set("Sections", NBTList<NBTCompound>(NBTTypes.TAG_Compound).apply { sections.forEach { add(it) } })
            .setString("Status", "full")
            .set("TileEntities", NBTList<NBT>(NBTTypes.TAG_Compound))
            .set("TileTicks", NBTList<NBT>(NBTTypes.TAG_Compound))
            .set("ToBeTicked", NBTList<NBT>(NBTTypes.TAG_List))
            .set("Structures", structures)
            .setInt("xPos", position.x)
            .setInt("zPos", position.z)
        )
}

const val CHUNK_DATA_VERSION = 2578

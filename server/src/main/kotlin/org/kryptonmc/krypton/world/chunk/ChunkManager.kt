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
import org.kryptonmc.krypton.util.nbt.getByte
import org.kryptonmc.krypton.util.nbt.getByteArray
import org.kryptonmc.krypton.util.nbt.getCompound
import org.kryptonmc.krypton.util.nbt.getIntArray
import org.kryptonmc.krypton.util.nbt.getList
import org.kryptonmc.krypton.util.nbt.getLong
import org.kryptonmc.krypton.util.nbt.getLongArray
import org.kryptonmc.krypton.util.nbt.getString
import org.kryptonmc.krypton.world.Heightmap.Type.MOTION_BLOCKING
import org.kryptonmc.krypton.world.Heightmap.Type.OCEAN_FLOOR
import org.kryptonmc.krypton.world.Heightmap.Type.WORLD_SURFACE
import org.kryptonmc.krypton.world.HeightmapBuilder
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.data.BitStorage
import org.kryptonmc.krypton.world.region.RegionFileManager
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
        positions.forEach { position -> load(position)?.let { chunks += it } }
        world.chunks += chunks
        return chunks
    }

    fun load(position: ChunkPosition): KryptonChunk? {
        val cachedChunk = chunkCache.getIfPresent(position)
        if (cachedChunk != null) return cachedChunk

        val nbt = regionFileManager.read(position).getCompound("Level") ?: return null
        val heightmaps = nbt.getCompound("Heightmaps", NBTCompound())

        val sections = nbt.getList<NBTCompound>("Sections", NBTList(NBTTypes.TAG_Compound)).map { section ->
            val palette = LinkedList(section.getList<NBTCompound>("Palette", NBTList(NBTTypes.TAG_Compound)).map { block ->
                val name = block.getString("Name", "").toKey()
                if (name == ChunkBlock.AIR.name) {
                    ChunkBlock.AIR
                } else {
                    ChunkBlock(name, block.getCompound("Properties", NBTCompound()).iterator().asSequence()
                        .associate { it.first to (it.second as NBTString).value })
                }
            })

            ChunkSection(
                section.getByte("Y", 0).toInt(),
                section.getByteArray("BlockLight", ByteArray(0)),
                section.getByteArray("SkyLight", ByteArray(0)),
                palette,
                BitStorage(palette.size.calculateBits(), 4096, section.getLongArray("BlockStates", LongArray(0)))
            )
        } as MutableList<ChunkSection>

        val carvingMasks = nbt.getCompound("CarvingMasks", NBTCompound()).let {
            it.getByteArray("AIR", ByteArray(0)) to it.getByteArray("LIQUID", ByteArray(0))
        }
        return KryptonChunk(
            world,
            position,
            sections,
            nbt.getIntArray("Biomes", IntArray(0)).map { Biome.fromId(it) },
            nbt.getLong("LastUpdate", 0L),
            nbt.getLong("inhabitedTime", 0L),
            listOf(
                HeightmapBuilder(heightmaps["MOTION_BLOCKING"] as NBTLongArray, MOTION_BLOCKING),
                HeightmapBuilder(heightmaps["OCEAN_FLOOR"] as NBTLongArray, OCEAN_FLOOR),
                HeightmapBuilder(heightmaps["WORLD_SURFACE"] as NBTLongArray, WORLD_SURFACE)
            ),
            carvingMasks,
            nbt.getCompound("Structures", NBTCompound())
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

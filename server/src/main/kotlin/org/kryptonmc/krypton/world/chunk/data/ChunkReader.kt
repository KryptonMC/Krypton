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
package org.kryptonmc.krypton.world.chunk.data

import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.Heightmap
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.chunk.ChunkSection
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.chunk.ProtoChunk
import org.kryptonmc.krypton.world.chunk.ProtoKryptonChunk
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import java.util.EnumSet
import kotlin.system.exitProcess

object ChunkReader {

    private val LOGGER = logger<ChunkReader>()

    fun read(world: KryptonWorld, position: ChunkPosition, tag: CompoundTag): ProtoChunk {
        // Yeah probably a bad idea if we allow this
        if (tag.contains("DataVersion", 99)) {
            val dataVersion = tag.getInt("DataVersion")
            if (dataVersion > ServerInfo.WORLD_VERSION) {
                RuntimeException("Attempted to load chunk saved with newer version of Minecraft! Data version of chunk: $dataVersion, current data version: ${ServerInfo.WORLD_VERSION}").printStackTrace()
                exitProcess(1)
            }
        }
        val data = tag.getCompound("Level")
        val storedPosition = ChunkPosition(data.getInt("xPos"), data.getInt("zPos"))
        if (position != storedPosition) LOGGER.error("Chunk file at $position is in the wrong location! Expected $position, found $storedPosition! Relocating...")
        // TODO: Biomes and tick lists
        val sectionData = data.getList("Sections", CompoundTag.ID)
        val sectionCount = world.sectionCount
        val sections = arrayOfNulls<ChunkSection>(sectionCount)
        sectionData.forEachCompound {
            val y = it.getByte("Y").toInt()
            if (it.contains("Palette", ListTag.ID) && it.contains("BlockStates", LongArrayTag.ID)) {
                val section = ChunkSection(y)
                section.palette.load(it.getList("Palette", ListTag.ID), it.getLongArray("BlockStates"))
                section.recount()
                if (!section.isEmpty()) sections[world.sectionIndexFromY(y)] = section
            }
            // TODO: Lighting data
        }
        val inhabitedTime = data.getLong("InhabitedTime")
        val type = tag.toChunkType()
        val chunkAccessor = if (type == ChunkStatus.Type.FULL) {
            // TODO: Tick lists
            KryptonChunk(world, position, sections, /* TODO */ emptyList(), inhabitedTime)
        } else ProtoChunk(position, sections, world).apply {
            this.inhabitedTime = inhabitedTime
            status = ChunkStatus.fromName(data.getString("Status"))
            // TODO: Lighting
        }
        chunkAccessor.isLightCorrect = data.getBoolean("isLightOn")
        val heightmapData = data.getCompound("Heightmaps")
        val missing = EnumSet.noneOf(Heightmap.Type::class.java)
        chunkAccessor.status.heightmapsAfter.forEach {
            if (heightmapData.contains(it.name)) chunkAccessor.setHeightmap(
                it,
                heightmapData.getLongArray(it.name)
            ) else missing.add(it)
        }
        Heightmap.prime(chunkAccessor, missing)
        // TODO: Structures and post-processing
        if (type == ChunkStatus.Type.FULL) return ProtoKryptonChunk(chunkAccessor as KryptonChunk)
        // TODO: Entities, tile entities, lights, and carving masks
        return chunkAccessor as ProtoChunk
    }
}

private fun CompoundTag.toChunkType() = ChunkStatus.fromName(getCompound("Level").getString("Status")).type

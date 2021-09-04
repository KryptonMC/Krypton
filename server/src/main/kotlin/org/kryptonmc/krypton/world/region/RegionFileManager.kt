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
package org.kryptonmc.krypton.world.region

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.MutableCompoundTag
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import java.nio.file.Path
import kotlin.io.path.createDirectories

/**
 * Manages region files. That's literally it.
 */
class RegionFileManager(
    private val folder: Path,
    private val synchronizeWrites: Boolean
) : AutoCloseable {

    fun read(position: ChunkPosition) = getRegionFile(position).getChunkDataInputStream(position).use {
        if (it == null) return MutableCompoundTag()
        TagIO.read(it, TagCompression.NONE)
    }

    fun write(position: ChunkPosition, tag: CompoundTag) {
        TagIO.write(getRegionFile(position).getChunkDataOutputStream(position), tag, TagCompression.NONE)
    }

    private fun getRegionFile(position: ChunkPosition): RegionFile {
        val regionX = position.x shr 5
        val regionZ = position.z shr 5
        val serialized = ChunkPosition.toLong(regionX, regionZ)
        val cachedFile = REGION_CACHE.getAndMoveToFirst(serialized)
        if (cachedFile != null) return cachedFile

        if (REGION_CACHE.size >= 256) REGION_CACHE.removeLast().close()
        folder.createDirectories()

        val path = folder.resolve("r.$regionX.$regionZ.mca")
        val regionFile = RegionFile(path, synchronizeWrites, folder, RegionFileCompression.ZLIB)
        REGION_CACHE.putAndMoveToFirst(serialized, regionFile)
        return regionFile
    }

    override fun close() = REGION_CACHE.values.forEach { it.close() }

    companion object {

        private val REGION_CACHE = Long2ObjectLinkedOpenHashMap<RegionFile>()
    }
}

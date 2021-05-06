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
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import java.io.InputStream
import java.io.OutputStream
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
        if (it == null) return CompoundBinaryTag.empty()
        BinaryTagIO.unlimitedReader().read(it as InputStream) // avoid overload resolution ambiguity by casting
    }

    fun write(position: ChunkPosition, tag: CompoundBinaryTag) =
        getRegionFile(position).getChunkDataOutputStream(position).use {
            BinaryTagIO.writer().write(tag, it as OutputStream) // avoiding overload resolution ambiguity again
        }

    private fun getRegionFile(position: ChunkPosition): RegionFile {
        val serialized = position.regionX combine position.regionZ
        val cachedFile = REGION_CACHE.getAndMoveToFirst(serialized)
        if (cachedFile != null) return cachedFile

        if (REGION_CACHE.size >= 256) REGION_CACHE.removeLast().close()
        folder.createDirectories()

//        val file = File(folder, "r.${position.regionX}.${position.regionZ}.mca")
        val path = folder.resolve("r.${position.regionX}.${position.regionZ}.mca")
        val regionFile = RegionFile(path, synchronizeWrites, folder, RegionFileCompression.ZLIB)
        REGION_CACHE.putAndMoveToFirst(serialized, regionFile)
        return regionFile
    }

    override fun close() = REGION_CACHE.values.forEach { it.close() }

    fun flush() = REGION_CACHE.values.forEach { it.flush() }

    companion object {

        private val REGION_CACHE = Long2ObjectLinkedOpenHashMap<RegionFile>()
    }
}

// this & 0xFFFFFFFFL | (other & 0xFFFFFFFFL) << 32
private infix fun Int.combine(other: Int) = (toLong() and 0xFFFFFFFFL) or ((other.toLong() and 0xFFFFFFFFL) shl 32)

/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.world.region

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Manages region files. That's literally it.
 */
class RegionFileManager(private val folder: Path, private val synchronizeWrites: Boolean) : AutoCloseable {

    private val regionCache = Long2ObjectLinkedOpenHashMap<RegionFile>()
    private val regionCacheLock = ReentrantLock()

    fun read(x: Int, z: Int): CompoundTag? = getRegionFile(x, z).getChunkDataInputStream(x, z)?.use { TagIO.read(it, TagCompression.NONE) }

    fun write(x: Int, z: Int, data: CompoundTag?) {
        val file = getRegionFile(x, z)
        if (data == null) {
            file.clear(x, z)
            return
        }
        file.getChunkDataOutputStream(x, z).use { TagIO.write(it, data, TagCompression.NONE) }
    }

    private fun getRegionFile(x: Int, z: Int): RegionFile {
        val regionX = x shr 5
        val regionZ = z shr 5
        val serialized = ChunkPos.pack(regionX, regionZ)
        val cachedFile = regionCacheLock.withLock { regionCache.getAndMoveToFirst(serialized) }
        if (cachedFile != null) return cachedFile

        if (regionCache.size >= MAX_CACHE_SIZE) regionCacheLock.withLock { regionCache.removeLast().close() }
        Files.createDirectories(folder)
        val path = folder.resolve("r.$regionX.$regionZ$ANVIL_EXTENSION")
        val regionFile = RegionFile(path, folder, synchronizeWrites)
        regionCacheLock.withLock { regionCache.putAndMoveToFirst(serialized, regionFile) }
        return regionFile
    }

    fun flush() {
        regionCacheLock.withLock { regionCache.values.forEach(RegionFile::flush) }
    }

    override fun close() {
        regionCacheLock.withLock { regionCache.values.forEach(RegionFile::close) }
    }

    companion object {

        private const val ANVIL_EXTENSION = ".mca"
        private const val MAX_CACHE_SIZE = 256
    }
}

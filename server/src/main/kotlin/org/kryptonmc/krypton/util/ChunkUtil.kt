/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.util

import kotlin.math.abs

/**
 * Utilities for dealing with chunks.
 */
object ChunkUtil {

    @JvmStatic
    fun forChunksInRange(chunkX: Int, chunkZ: Int, range: Int, consumer: ChunkPosConsumer) {
        for (x in -range..range) {
            for (z in -range..range) {
                consumer.accept(chunkX + x, chunkZ + z)
            }
        }
    }

    @JvmStatic
    fun forDifferingChunksInRange(chunkX: Int, chunkZ: Int, oldChunkX: Int, oldChunkZ: Int, range: Int, consumer: ChunkPosConsumer) {
        for (x in chunkX - range..chunkX + range) {
            for (z in chunkZ - range..chunkZ + range) {
                // If the difference between either the x and old x or z and old z is > range, then the chunk is
                // newly in range, and we can process it.
                if (abs(x - oldChunkX) > range || abs(z - oldChunkZ) > range) consumer.accept(x, z)
            }
        }
    }

    @JvmStatic
    fun forDifferingChunksInRange(chunkX: Int, chunkZ: Int, oldChunkX: Int, oldChunkZ: Int, range: Int,
                                  newConsumer: ChunkPosConsumer, oldConsumer: ChunkPosConsumer) {
        // Find the new chunks
        forDifferingChunksInRange(chunkX, chunkZ, oldChunkX, oldChunkZ, range, newConsumer)
        // Find the old chunks
        forDifferingChunksInRange(oldChunkX, oldChunkZ, chunkX, chunkZ, range, oldConsumer)
    }

    fun interface ChunkPosConsumer {

        fun accept(x: Int, z: Int)
    }
}

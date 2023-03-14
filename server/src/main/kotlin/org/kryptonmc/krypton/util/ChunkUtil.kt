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

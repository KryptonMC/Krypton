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

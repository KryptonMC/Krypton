package org.kryptonmc.krypton.world.region

import org.kryptonmc.krypton.world.chunk.Chunk
import org.kryptonmc.krypton.world.chunk.ChunkPosition

data class Region(
    val x: Int,
    val z: Int,
    val chunks: Map<ChunkPosition, Chunk>
)
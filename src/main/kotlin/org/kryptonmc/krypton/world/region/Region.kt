package org.kryptonmc.krypton.world.region

import org.kryptonmc.krypton.world.chunk.Chunk

data class Region(
    val x: Int,
    val z: Int,
    val chunks: List<Chunk>
)
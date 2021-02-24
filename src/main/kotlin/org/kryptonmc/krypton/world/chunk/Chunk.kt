package org.kryptonmc.krypton.world.chunk

data class Chunk(
    val position: ChunkPosition,
    val level: ChunkData
)
package org.kryptonmc.krypton.world.chunk

data class ChunkSection(
    val blockCount: Short,
    val bitsPerBlock: Byte,
    val palette: List<Int>? = null,
    val dataArray: LongArray
)
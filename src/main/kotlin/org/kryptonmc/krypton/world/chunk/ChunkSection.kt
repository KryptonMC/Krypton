package org.kryptonmc.krypton.world.chunk

import net.kyori.adventure.nbt.BinaryTag
import org.kryptonmc.krypton.registry.NamespacedKey

//data class ChunkSection(
//    val blockCount: Short,
//    val bitsPerBlock: Byte,
//    val palette: List<Int>? = null,
//    val dataArray: List<Long>
//)

data class ChunkSection(
    val y: Byte,
    val blockLight: List<Byte>,
    val skyLight: List<Byte>,
    val palette: List<ChunkBlock>,
    val blockStates: List<Long>
)

data class ChunkBlock(
    val name: NamespacedKey,
    val properties: Map<String, BinaryTag> = emptyMap()
)
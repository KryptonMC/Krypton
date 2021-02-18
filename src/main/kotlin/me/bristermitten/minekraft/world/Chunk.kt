package me.bristermitten.minekraft.world

import net.kyori.adventure.nbt.CompoundBinaryTag

// TODO: Make this used
data class Chunk(
    val x: Int,
    val y: Int,
    val isFull: Boolean,
    val primaryBitMask: Int,
    val heightmaps: CompoundBinaryTag,
    val biomes: List<Biome>,
    val sections: List<ChunkSection>
)

data class ChunkSection(
    val blockCount: Short,
    val bitsPerBlock: Byte,
    val palette: List<Int>? = null,
    val dataArray: LongArray
)
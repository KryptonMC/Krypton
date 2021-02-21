package org.kryptonmc.krypton.world.chunk

import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.world.Biome

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
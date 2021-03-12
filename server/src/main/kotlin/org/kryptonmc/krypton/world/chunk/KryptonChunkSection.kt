package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.world.chunk.ChunkSection

data class KryptonChunkSection(
    override val y: Int,
    val blockLight: List<Byte>,
    val skyLight: List<Byte>,
    val palette: List<ChunkBlock>,
    val blockStates: List<Long>
) : ChunkSection

data class ChunkBlock(
    val name: NamespacedKey,
    val properties: Map<String, String> = emptyMap()
)
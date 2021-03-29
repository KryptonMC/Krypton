package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.world.chunk.ChunkSection

data class KryptonChunkSection(
    override val y: Int,
    val blockLight: ByteArray,
    val skyLight: ByteArray,
    val palette: List<ChunkBlock>,
    val blockStates: LongArray
) : ChunkSection {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as KryptonChunkSection
        return y == other.y && blockLight.contentEquals(other.blockLight) && skyLight.contentEquals(other.skyLight) && palette == other.palette && blockStates.contentEquals(other.blockStates)
    }

    override fun hashCode(): Int {
        var result = y
        result = 31 * result + blockLight.contentHashCode()
        result = 31 * result + skyLight.contentHashCode()
        result = 31 * result + palette.hashCode()
        result = 31 * result + blockStates.contentHashCode()
        return result
    }
}

data class ChunkBlock(
    val name: NamespacedKey,
    val properties: Map<String, String> = emptyMap()
)
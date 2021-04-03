package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.world.data.StateIndexHolder

data class ChunkSection(
    val y: Int,
    val blockLight: ByteArray,
    val skyLight: ByteArray,
    val palette: List<ChunkBlock>,
    val blockStates: StateIndexHolder
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ChunkSection
        return y == other.y && blockLight.contentEquals(other.blockLight) && skyLight.contentEquals(other.skyLight) && palette == other.palette && blockStates == other.blockStates
    }

    override fun hashCode() = arrayOf(y, blockLight, skyLight, palette, blockStates).contentDeepHashCode()
}

data class ChunkBlock(
    val name: NamespacedKey,
    val properties: Map<String, String> = emptyMap()
)
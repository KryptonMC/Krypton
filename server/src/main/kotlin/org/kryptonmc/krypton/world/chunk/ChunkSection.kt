package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.data.StateIndexHolder

data class ChunkSection(
    val y: Int,
    val blockLight: ByteArray,
    val skyLight: ByteArray,
    val palette: MutableList<ChunkBlock>,
    val blockStates: StateIndexHolder
) {

    fun setBlock(location: Vector, block: KryptonBlock) {
        val index = indexOf(location.x.toInt() and 0xF, location.y.toInt() and 0xF, location.z.toInt() and 0xF)
        val state = palette.indexOfFirst { it.name == block.type.key }
        blockStates[index] = state
    }

    private fun indexOf(x: Int, y: Int, z: Int) = (y shl 8) or (z shl 4) or x

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
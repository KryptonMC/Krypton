package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.registry.Registries
import org.kryptonmc.krypton.world.block.palette.GlobalPalette
import org.kryptonmc.krypton.world.data.PalettedContainer
import org.kryptonmc.krypton.world.data.StateIndexHolder

data class ChunkSection(
    val y: Int,
    private var nonEmptyBlockCount: Int = 0,
    val blockLight: ByteArray,
    val skyLight: ByteArray,
) {

    private val states = PalettedContainer(GLOBAL_BLOCK_STATE_PALETTE, Registries.BLOCK_STATES, AIR)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ChunkSection
        return y == other.y && blockLight.contentEquals(other.blockLight) && skyLight.contentEquals(other.skyLight) && palette == other.palette && blockStates == other.blockStates
    }

    override fun hashCode() = arrayOf(y, blockLight, skyLight, palette, blockStates).contentDeepHashCode()

    companion object {

        private val AIR = NamespacedKey(value = "air")
        private val GLOBAL_BLOCK_STATE_PALETTE = GlobalPalette(Registries.BLOCK_STATES, AIR)
    }
}

data class ChunkBlock(
    val name: NamespacedKey,
    val properties: Map<String, String> = emptyMap()
)
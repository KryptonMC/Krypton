package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.krypton.api.block.Block
import org.kryptonmc.krypton.api.inventory.item.Material
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.world.data.BitStorage
import java.util.LinkedList

/**
 * A section of a chunk (*nah*). These are 16x16x16 areas that hold the actual block states and palette information
 */
data class ChunkSection(
    val y: Int,
    val blockLight: ByteArray = ByteArray(2048),
    val skyLight: ByteArray = ByteArray(2048),
    val palette: LinkedList<ChunkBlock> = LinkedList(),
    var blockStates: BitStorage = BitStorage(4, 4096)
) {

    var nonEmptyBlockCount = 0; private set

    init {
        recount()
    }

    operator fun get(x: Int, y: Int, z: Int) = palette[blockStates[indexOf(x, y, z)]].name

    fun set(block: Block): Boolean {
        if (block.type != Material.AIR) nonEmptyBlockCount++

        val x = block.location.blockX
        val y = block.location.blockY
        val z = block.location.blockZ
        val index = indexOf(x and 0xF, y and 0xF, z and 0xF)
        if (blockStates[index] != 0) return false
        blockStates[index] = palette.getOrUpdate(block.type.key)
        return true
    }

    private fun recount() {
        if (blockStates.isEmpty()) return
        nonEmptyBlockCount = 0
        for (i in 0 until 4096) {
            try {
                if (blockStates[i] != 0) nonEmptyBlockCount++
            } catch (exception: ArrayIndexOutOfBoundsException) {}
        }
    }

    private fun indexOf(x: Int, y: Int, z: Int) = (y shl 8) or (z shl 4) or x

    private fun LinkedList<ChunkBlock>.getOrUpdate(name: NamespacedKey): Int =
        indexOfFirst { it.name == name }.takeIf { it != -1 }?.let { return it } ?: palette.let {
            val block = ChunkBlock(name)
            it.addLast(block)
            it.indexOf(block)
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ChunkSection
        return y == other.y
                && blockLight.contentEquals(other.blockLight)
                && skyLight.contentEquals(other.skyLight)
                && palette == other.palette
                && blockStates == other.blockStates
    }

    override fun hashCode() = arrayOf(y, blockLight, skyLight, palette, blockStates).contentDeepHashCode()
}

data class ChunkBlock(
    val name: NamespacedKey,
    val properties: Map<String, String> = emptyMap()
) {

    companion object {

        val AIR = ChunkBlock(NamespacedKey(value = "air"))
    }
}

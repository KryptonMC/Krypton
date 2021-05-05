package org.kryptonmc.krypton.world

import net.kyori.adventure.nbt.LongArrayBinaryTag
import org.kryptonmc.krypton.api.inventory.item.Material
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.data.BitStorage

class Heightmap(private val chunk: KryptonChunk, val nbt: LongArrayBinaryTag, val type: Type) {

    val data = BitStorage(9, 256, nbt.value())

    fun update(x: Int, y: Int, z: Int, material: Material): Boolean {
        val firstAvailable = data[indexOf(x, z)]
        if (y <= firstAvailable - 2) return false

        if (type.isOpaque(material)) {
            if (y >= firstAvailable) {
                set(x, z, y + 1)
                return true
            }
        } else if (firstAvailable - 1 == y) {
            for (i in y - 1 downTo 0) {
                val position = Vector(x, i, z)
                if (!type.isOpaque(chunk.getBlock(position).type)) continue
                set(x, z, i + 1)
                return true
            }
            set(x, z, 0)
            return true
        }
        return false
    }

    private operator fun set(x: Int, z: Int, value: Int) = data.set(indexOf(x, z), value)

    private fun indexOf(x: Int, z: Int) = x + z * 16

    enum class Type(val isOpaque: (Material) -> Boolean) {

        WORLD_SURFACE(NOT_AIR),
        OCEAN_FLOOR(BLOCKS_MOTION),
        MOTION_BLOCKING(BLOCKS_MOTION);
    }

    companion object {

        private val NOT_AIR: (Material) -> Boolean = { it != Material.AIR }
        private val BLOCKS_MOTION: (Material) -> Boolean = { it.blocksMotion }
    }
}

data class HeightmapBuilder(
    val nbt: LongArrayBinaryTag,
    val type: Heightmap.Type
)

/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world

import org.jglrxavpok.hephaistos.nbt.NBTLongArray
import org.kryptonmc.api.inventory.item.Material
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.data.BitStorage

class Heightmap(private val chunk: KryptonChunk, val nbt: NBTLongArray, val type: Type) {

    val data = BitStorage(9, 256, nbt.value)

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
                if (!type.isOpaque(chunk.getBlock(x, i, z).type)) continue
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
    val nbt: NBTLongArray,
    val type: Heightmap.Type
)

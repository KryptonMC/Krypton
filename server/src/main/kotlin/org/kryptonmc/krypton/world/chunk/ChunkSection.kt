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
package org.kryptonmc.krypton.world.chunk

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.inventory.item.Material
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
        blockStates[index] = palette.getOrUpdate(block.type.key())
        return true
    }

    private fun recount() {
        if (blockStates.isEmpty()) return
        nonEmptyBlockCount = 0
        for (i in 0 until 4096) {
            try {
                if (blockStates[i] != 0) nonEmptyBlockCount++
            } catch (ignored: ArrayIndexOutOfBoundsException) {}
        }
    }

    private fun indexOf(x: Int, y: Int, z: Int) = y shl 8 or (z shl 4) or x

    private fun LinkedList<ChunkBlock>.getOrUpdate(name: Key): Int =
        indexOfFirst { it.name == name }.takeIf { it != -1 }?.let { return it } ?: palette.let {
            val block = ChunkBlock(name)
            it.addLast(block)
            it.indexOf(block)
        }

    override fun equals(other: Any?) = other is ChunkSection &&
        y == other.y &&
        blockLight.contentEquals(other.blockLight) &&
        skyLight.contentEquals(other.skyLight) &&
        palette == other.palette &&
        blockStates == other.blockStates

    override fun hashCode() = arrayOf(y, blockLight, skyLight, palette, blockStates).contentDeepHashCode()

    override fun toString() = "ChunkSection(y=$y, blockLight=$blockLight, skyLight=$skyLight, palette=$palette, blockStates=$blockStates)"
}

data class ChunkBlock(
    val name: Key,
    val properties: Map<String, String> = emptyMap()
) {

    companion object {

        val AIR = ChunkBlock(key("air"))
    }
}

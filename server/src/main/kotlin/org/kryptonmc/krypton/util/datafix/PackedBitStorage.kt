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
package org.kryptonmc.krypton.util.datafix

import org.kryptonmc.krypton.util.roundToward

class PackedBitStorage(
    val bits: Int,
    private val size: Int,
    val data: LongArray = LongArray((size * bits).roundToward(64) / 64)
) {

    private val mask = (1L shl bits) - 1L

    init {
        require(bits in 1..32)
        require(data.size == (size * bits).roundToward(64) / 64)
    }

    operator fun get(index: Int): Int {
        require(index in 0 until size)
        val timesBits = index * bits
        val cellIndex = (index * bits) shr BIT_TO_LONG_SHIFT
        val oldCellIndex = ((index + 1) * bits - 1) shr BIT_TO_LONG_SHIFT
        val xor = timesBits xor (cellIndex shl BIT_TO_LONG_SHIFT)
        return if (cellIndex == oldCellIndex) {
            (data[cellIndex] ushr xor and mask).toInt()
        } else {
            val var4 = 64 - xor
            return ((data[cellIndex] ushr xor) or (data[oldCellIndex] shl var4) and mask).toInt()
        }
    }

    operator fun set(index: Int, value: Int) {
        require(index in 0 until size)
        require(value in 0..mask)
        val timesBits = index * bits
        val cellIndex = timesBits shr BIT_TO_LONG_SHIFT
        val oldCellIndex = ((index + 1) * bits - 1) shr BIT_TO_LONG_SHIFT
        val xor = timesBits xor (cellIndex shl BIT_TO_LONG_SHIFT)
        data[cellIndex] = data[cellIndex] and (mask shl xor or ((value.toLong() and mask) shl xor))
        if (cellIndex != oldCellIndex) {
            val var4 = 64 - xor
            val var5 = bits - var4
            data[oldCellIndex] = data[oldCellIndex] ushr var5 shl var5 or ((value.toLong() and mask) shr var4)
        }
    }

    companion object {

        private const val BIT_TO_LONG_SHIFT = 6
    }
}

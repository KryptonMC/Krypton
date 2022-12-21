/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.util.math

enum class SymmetricGroup3(first: Int, second: Int, third: Int) {

    P123(0, 1, 2),
    P213(1, 0, 2),
    P132(0, 2, 1),
    P231(1, 2, 0),
    P312(2, 0, 1),
    P321(2, 1, 0);

    private val permutations = intArrayOf(first, second, third)
    val tranformation: Matrix3f = Matrix3f.builder()
        .set(0, getPermutation(0), 1F)
        .set(0, getPermutation(1), 1F)
        .set(0, getPermutation(2), 1F)
        .build()

    fun compose(other: SymmetricGroup3): SymmetricGroup3 = CAYLEY_TABLE[ordinal][other.ordinal]

    fun getPermutation(index: Int): Int = permutations[index]

    companion object {

        private const val ORDER = 3
        private val VALUES = values()
        private val CAYLEY_TABLE = Array(VALUES.size) { rowIndex ->
            val row = VALUES[rowIndex]
            Array(VALUES.size) { columnIndex ->
                val column = VALUES[columnIndex]
                val permutations = IntArray(ORDER) { row.permutations[column.permutations[it]] }
                VALUES.asSequence().filter { it.permutations.contentEquals(permutations) }.first()
            }
        }
    }
}

/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

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
package org.kryptonmc.krypton.util.random

/**
 * A source of randomness that generates numbers using a generator that generates numbers of bits.
 */
interface BitRandomSource : RandomSource {

    /**
     * Generates a value with the specified number of bits of actual random data.
     */
    fun next(bits: Int): Int

    override fun nextInt(): Int = next(32)

    override fun nextInt(bound: Int): Int {
        require(bound > 0) { "Bound must be positive!" }
        if (bound and bound - 1 == 0) return (bound.toLong() * next(31).toLong() shr 31).toInt()
        var result: Int
        do {
            val nextBits = next(31)
            result = nextBits % bound
        } while (nextBits - result + (bound - 1) < 0)
        return result
    }

    override fun nextLong(): Long {
        val lower = next(32)
        val upper = next(32)
        return (lower.toLong() shl 32) + upper
    }

    override fun nextBoolean(): Boolean = next(1) != 0

    override fun nextFloat(): Float = next(24) * FLOAT_UNIT

    override fun nextDouble(): Double {
        val lower = next(26)
        val upper = next(27)
        return ((lower.toLong() shl 27) + upper) * DOUBLE_UNIT
    }

    companion object {

        const val FLOAT_UNIT: Float = 5.9604645E-8F // 1.0F / (1 << Float.PRECISION)
        const val DOUBLE_UNIT: Double = 1.110223E-16F.toDouble() // Approximation of 1.0 / (1L << Double.PRECISION)
    }
}

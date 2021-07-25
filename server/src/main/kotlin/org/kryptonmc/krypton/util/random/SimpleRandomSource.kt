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
package org.kryptonmc.krypton.util.random

import java.util.concurrent.atomic.AtomicLong
import kotlin.math.ln
import kotlin.math.sqrt

/**
 * Simple source of random values, based off of [java.util.Random].
 */
class SimpleRandomSource(seed: Long) : RandomSource {

    private val seed = AtomicLong(seed)
    private var nextNextGaussian = 0.0
    private var haveNextNextGaussian = false

    override fun setSeed(seed: Long) = this.seed.set((seed xor MULTIPLIER) and MODULUS_MASK)

    override fun nextInt() = next(32)

    override fun nextInt(upper: Int): Int {
        require(upper > 0) { "Bound must be positive!" }
        if (upper and upper - 1 == 0) return (upper.toLong() * next(31).toLong() shr 31).toInt()
        var nextSeed: Int
        do {
            val oldSeed = next(31)
            nextSeed = oldSeed % upper
        } while (oldSeed - nextSeed + (upper - 1) < 0)
        return nextSeed
    }

    override fun nextLong() = (next(32).toLong() shl 32) + next(32)

    override fun nextBoolean() = next(1) != 0

    override fun nextFloat() = next(24) * FLOAT_MULTIPLIER

    override fun nextDouble() = ((next(26).toLong() shl 27) + next(27)) * DOUBLE_MULTIPLIER

    override fun nextGaussian(): Double {
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false
            return nextNextGaussian
        }
        var v1: Double
        var v2: Double
        var s: Double
        do {
            v1 = 2 * nextDouble() - 1
            v2 = 2 * nextDouble() - 1
            s = v1 * v1 + v2 * v2
        } while (s >= 1 || s == 0.0)
        val multiplier = sqrt(-2 * ln(s) / s)
        nextNextGaussian = v2 * multiplier
        haveNextNextGaussian = true
        return v1 * multiplier
    }

    private fun next(bits: Int): Int {
        val random = seed.get() * MULTIPLIER + INCREMENT and MODULUS_MASK
        seed.set(random)
        return (random shr MODULUS_BITS - bits).toInt()
    }

    companion object {

        // Linear congruential generation parameters and constants
        private const val MODULUS_BITS = 48
        private const val MODULUS_MASK = 281474976710655L
        private const val MULTIPLIER = 25214903917L
        private const val INCREMENT = 11L
        private const val FLOAT_MULTIPLIER = 5.9604645E-8F
        private const val DOUBLE_MULTIPLIER = 1.110223E-16
    }
}

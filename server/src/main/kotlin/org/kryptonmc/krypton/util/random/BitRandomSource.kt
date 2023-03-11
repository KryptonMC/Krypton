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

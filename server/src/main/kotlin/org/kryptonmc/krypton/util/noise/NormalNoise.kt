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
package org.kryptonmc.krypton.util.noise

import it.unimi.dsi.fastutil.doubles.DoubleArrayList
import it.unimi.dsi.fastutil.doubles.DoubleList
import org.kryptonmc.krypton.util.random.RandomSource
import kotlin.math.max
import kotlin.math.min

class NormalNoise(random: RandomSource, firstOctave: Int, amplitudes: DoubleList) {

    private val first = PerlinNoise(random, firstOctave, amplitudes)
    private val second = PerlinNoise(random, firstOctave, amplitudes)
    private val valueFactor: Double

    init {
        var max = Int.MAX_VALUE
        var min = Int.MIN_VALUE
        val iterator = amplitudes.iterator()
        while (iterator.hasNext()) {
            val index = iterator.nextIndex()
            val next = iterator.nextDouble()
            if (next != 0.0) {
                max = min(max, index)
                min = max(min, index)
            }
        }
        valueFactor = (TARGET_DEVIATION / 2) / (min - max).expectedDeviation()
    }

    constructor(random: RandomSource, firstOctave: Int, vararg amplitudes: Double) : this(
        random,
        firstOctave,
        DoubleArrayList(amplitudes)
    )

    fun getValue(x: Double, y: Double, z: Double): Double {
        val factorX = x * INPUT_FACTOR
        val factorY = y * INPUT_FACTOR
        val factorZ = z * INPUT_FACTOR
        return (first.getValue(x, y, z) + second.getValue(factorX, factorY, factorZ)) * valueFactor
    }

    companion object {

        private const val INPUT_FACTOR = 1.0181268882175227
        private const val TARGET_DEVIATION = 0.3333333333333333

        @JvmStatic
        private fun Int.expectedDeviation(): Double = 0.1 * (1.0 + 1.0 / (this + 1).toDouble())
    }
}

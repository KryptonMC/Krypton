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

import it.unimi.dsi.fastutil.ints.IntRBTreeSet
import it.unimi.dsi.fastutil.ints.IntSortedSet
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.util.random.WorldGenRandom
import kotlin.math.pow

class PerlinSimplexNoise private constructor(random: RandomSource, octaves: IntSortedSet) : SurfaceNoise {

    private val noiseLevels: Array<SimplexNoise?>
    private val highestFrequencyValueFactor: Double
    private val highestFrequencyInputFactor: Double

    constructor(random: RandomSource, octaves: IntRange) : this(random, IntRBTreeSet(octaves.toSortedSet()))

    init {
        require(octaves.isNotEmpty()) { "No octaves provided! Octaves must be provided for the noise generator!" }
        val invFirst = -octaves.firstInt()
        val last = octaves.lastInt()
        val total = invFirst + last + 1
        require(total >= 1) { "Total number of octaves must be greater than or equal to 1!" }
        val noiseLevel = SimplexNoise(random)
        noiseLevels = arrayOfNulls(total)
        if (last in 0 until total && octaves.contains(0)) noiseLevels[last] = noiseLevel
        for (i in last + 1 until total) if (i >= 0 && octaves.contains(total - i)) noiseLevels[i] = SimplexNoise(random) else random.skip(262)
        if (last > 0) {
            val sourceLevel = (noiseLevel.getValue(noiseLevel.xOffset, noiseLevel.yOffset, noiseLevel.zOffset) * LONG_MAX_FLOAT.toDouble()).toLong()
            val genRandom = WorldGenRandom(sourceLevel)
            for (i in total - 1 downTo 0) if (i < total && octaves.contains(total - i)) noiseLevels[i] = SimplexNoise(genRandom) else genRandom.skip(262)
        }
        highestFrequencyInputFactor = 2.0.pow(last)
        highestFrequencyValueFactor = 1.0 / (2.0.pow(total) - 1.0)
    }

    fun getValue(x: Double, y: Double, useOrigin: Boolean): Double {
        var total = 0.0
        var highestFreqInputFactor = highestFrequencyInputFactor
        var highestFreqValueFactor = highestFrequencyValueFactor
        noiseLevels.forEach {
            if (it != null) total += it.getValue(x * highestFreqInputFactor + if (useOrigin) it.xOffset else 0.0, y * highestFreqInputFactor + if (useOrigin) it.yOffset else 0.0) * highestFreqValueFactor
            highestFreqInputFactor /= 2.0
            highestFreqValueFactor *= 2.0
        }
        return total
    }

    override fun getValue(x: Double, y: Double, yScale: Double, yMax: Double) = getValue(x, y, true) * 0.55

    companion object {

        private const val LONG_MAX_FLOAT = 9.223372E18F
    }
}

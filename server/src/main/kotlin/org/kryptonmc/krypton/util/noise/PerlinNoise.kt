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
import it.unimi.dsi.fastutil.ints.IntRBTreeSet
import it.unimi.dsi.fastutil.ints.IntSortedSet
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.util.random.WorldGenRandom
import java.util.function.IntConsumer
import kotlin.math.pow

class PerlinNoise private constructor(
    random: RandomSource,
    octaves: Pair<Int, DoubleList>,
    randomGetter: (Long) -> RandomSource = ::WorldGenRandom
) : SurfaceNoise {

    private val noiseLevels: Array<ImprovedNoise?>
    private val amplitudes = octaves.second
    private val lowestFrequencyValueFactor: Double
    private val lowestFrequencyInputFactor: Double

    init {
        val firstOctave = octaves.first
        val noise = ImprovedNoise(random)
        val amplitudeCount = amplitudes.size
        val invFirstOctave = -firstOctave
        noiseLevels = arrayOfNulls(amplitudeCount)
        if (invFirstOctave in 0 until amplitudeCount) {
            val amplitude = amplitudes.getDouble(invFirstOctave)
            if (amplitude != 0.0) noiseLevels[invFirstOctave] = noise
        }
        for (i in invFirstOctave - 1 downTo 0) {
            if (i < amplitudeCount) {
                val amplitude = amplitudes.getDouble(i)
                if (amplitude != 0.0) noiseLevels[i] = ImprovedNoise(random) else random.skip(262)
            }
            random.skip(262)
        }
        require(invFirstOctave >= amplitudeCount - 1) { "Positive octaves are not supported!" }
        lowestFrequencyInputFactor = 2.0.pow(-invFirstOctave)
        lowestFrequencyValueFactor = 2.0.pow(amplitudeCount - 1) / (2.0.pow(amplitudeCount) - 1)
    }

    constructor(random: RandomSource, first: Int, vararg octaves: Double) : this(random, first, DoubleArrayList(octaves))

    constructor(random: RandomSource, first: Int, octaves: DoubleList) : this(random, Pair(first, octaves))

    constructor(random: RandomSource, octaves: IntRange) : this(random, IntRBTreeSet(octaves.toSortedSet()))

    constructor(random: RandomSource, octaves: List<Int>) : this(random, IntRBTreeSet(octaves))

    private constructor(
        random: RandomSource,
        octaves: IntSortedSet,
        randomGetter: (Long) -> RandomSource = ::WorldGenRandom
    ) : this(random, octaves.makeAmplitudes(), randomGetter)

    fun getOctaveNoise(index: Int): ImprovedNoise? = noiseLevels[noiseLevels.size - 1 - index]

    fun getValue(
        x: Double,
        y: Double,
        z: Double,
        yScale: Double = 0.0,
        yMax: Double = 0.0,
        useOrigin: Boolean = false
    ): Double {
        var value = 0.0
        var lowestFreqInputFactor = lowestFrequencyInputFactor
        var lowestFreqValueFactor = lowestFrequencyValueFactor
        for (i in noiseLevels.indices) {
            val noiseLevel = noiseLevels[i]
            if (noiseLevel != null) {
                val noise = noiseLevel.noise(
                    (x * lowestFreqInputFactor).wrap(),
                    if (useOrigin) -noiseLevel.yOffset else (y * lowestFreqInputFactor).wrap(),
                    (z * lowestFreqInputFactor).wrap(),
                    yScale * lowestFreqInputFactor,
                    yMax * lowestFreqInputFactor
                )
                value += amplitudes.getDouble(i) * noise * lowestFreqValueFactor
            }
            lowestFreqInputFactor *= 2.0
            lowestFreqValueFactor /= 2.0
        }
        return value
    }

    override fun getValue(x: Double, y: Double, yScale: Double, yMax: Double): Double = getValue(x, y, 0.0, yScale, yMax, false)

    companion object {

        @JvmStatic
        private fun IntSortedSet.makeAmplitudes(): Pair<Int, DoubleList> {
            require(isNotEmpty()) { "No octaves provided! Octaves must be provided for the noise generator!" }
            val invFirst = -firstInt()
            val last = lastInt()
            val total = invFirst + last + 1
            require(total >= 1) { "Total number of octaves must be greater than or equal to 1!" }
            val amplitudes = DoubleArrayList(DoubleArray(total))
            iterator().forEachRemaining(IntConsumer { amplitudes.set(it + invFirst, 1.0) })
            return Pair(-invFirst, amplitudes)
        }
    }
}

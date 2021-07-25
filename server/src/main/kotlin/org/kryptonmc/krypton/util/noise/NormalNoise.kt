package org.kryptonmc.krypton.util.noise

import it.unimi.dsi.fastutil.doubles.DoubleArrayList
import it.unimi.dsi.fastutil.doubles.DoubleList
import org.kryptonmc.krypton.util.random.RandomSource
import kotlin.math.max
import kotlin.math.min

class NormalNoise(random: RandomSource, first: Int, octaves: DoubleList) {

    private val first = PerlinNoise(random, first, octaves)
    private val second = PerlinNoise(random, first, octaves)
    private val valueFactor: Double

    init {
        var max = Int.MAX_VALUE
        var min = Int.MIN_VALUE
        val iterator = octaves.iterator()
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

    constructor(random: RandomSource, first: Int, vararg octaves: Double) : this(random, first, DoubleArrayList(octaves))

    fun getValue(x: Double, y: Double, z: Double): Double {
        val factorX = x * INPUT_FACTOR
        val factorY = y * INPUT_FACTOR
        val factorZ = z * INPUT_FACTOR
        return (first.getValue(x, y, z) + second.getValue(factorX, factorY, factorZ)) * valueFactor
    }

    companion object {

        private const val INPUT_FACTOR = 1.0181268882175227
        private const val TARGET_DEVIATION = 0.3333333333333333
    }
}

private fun Int.expectedDeviation() = 0.1 * (1.0 + 1.0 / (this + 1).toDouble())

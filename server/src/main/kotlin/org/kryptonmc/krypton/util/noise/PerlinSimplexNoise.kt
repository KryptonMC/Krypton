package org.kryptonmc.krypton.util.noise

import it.unimi.dsi.fastutil.ints.IntSortedSet
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.util.random.WorldGenRandom
import kotlin.math.pow

class PerlinSimplexNoise private constructor(random: RandomSource, octaves: IntSortedSet) : SurfaceNoise {

    private val noiseLevels: Array<SimplexNoise?>
    private val highestFrequencyValueFactor: Double
    private val highestFrequencyInputFactor: Double

    init {
        require(octaves.isNotEmpty()) { "No octaves provided! Octaves must be provided for the noise generator!" }
        val invFirst = -octaves.firstInt()
        val last = octaves.lastInt()
        val total = invFirst + last + 1
        require(total >= 1) { "Total number of octaves must be greater than or equal to 1!" }
        val noiseLevel = SimplexNoise(random)
        noiseLevels = arrayOfNulls(total)
        if (last in 0..total && octaves.contains(0)) noiseLevels[last] = noiseLevel
        for (i in last + 1..total) if (i >= 0 && octaves.contains(total - i)) noiseLevels[i] = SimplexNoise(random) else random.skip(262)
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

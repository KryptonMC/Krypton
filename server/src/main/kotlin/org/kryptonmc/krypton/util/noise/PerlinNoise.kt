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
    amplitudePair: Pair<Int, DoubleList>,
    randomGetter: (Long) -> RandomSource = ::WorldGenRandom
) : SurfaceNoise {

    private val noiseLevels: Array<ImprovedNoise?>
    private val amplitudes = amplitudePair.second
    private val lowestFrequencyValueFactor: Double
    private val lowestFrequencyInputFactor: Double

    init {
        val amp = amplitudePair.first
        val noise = ImprovedNoise(random)
        val amplitudeCount = amplitudes.size
        val ampIndex = -amp
        noiseLevels = arrayOfNulls(amplitudeCount)
        if (ampIndex in 0 until amplitudeCount) {
            val amplitude = amplitudes.getDouble(ampIndex)
            if (amplitude != 0.0) noiseLevels[ampIndex] = noise
        }
        for (i in ampIndex - 1 downTo 0) {
            if (i < amplitudeCount) {
                val amplitude = amplitudes.getDouble(i)
                if (amplitude != 0.0) noiseLevels[i] = ImprovedNoise(random) else random.skip(262)
            }
            random.skip(262)
        }
        require(ampIndex >= amplitudeCount - 1) { "Positive octaves are not supported!" }
        lowestFrequencyInputFactor = 2.0.pow(-ampIndex)
        lowestFrequencyValueFactor = 2.0.pow(amplitudeCount - 1) / (2.0.pow(amplitudeCount) - 1)
    }

    constructor(random: RandomSource, first: Int, vararg octaves: Double) : this(random, first, DoubleArrayList(octaves))

    constructor(random: RandomSource, first: Int, octaves: DoubleList) : this(random, Pair(first, octaves))

    constructor(random: RandomSource, octaves: IntRange) : this(random, octaves.toList())

    constructor(random: RandomSource, octaves: List<Int>) : this(random, IntRBTreeSet(octaves))

    private constructor(
        random: RandomSource,
        octaves: IntSortedSet,
        randomGetter: (Long) -> RandomSource = ::WorldGenRandom
    ) : this(random, octaves.makeAmplitudes(), randomGetter)

    fun getOctaveNoise(index: Int) = noiseLevels[noiseLevels.size - 1 - index]

    fun getValue(x: Double, y: Double, z: Double, yScale: Double = 0.0, yMax: Double = 0.0, useOrigin: Boolean = false): Double {
        var value = 0.0
        var lowestFreqInputFactor = lowestFrequencyInputFactor
        var lowestFreqValueFactor = lowestFrequencyValueFactor
        for (i in noiseLevels.indices) {
            val noiseLevel = noiseLevels[i]
            if (noiseLevel != null) {
                val noise = noiseLevel.noise((x * lowestFreqInputFactor).wrap(), if (useOrigin) -noiseLevel.yOffset else (y * lowestFreqInputFactor).wrap(), (z * lowestFreqInputFactor).wrap(), yScale * lowestFreqInputFactor, yMax * lowestFreqInputFactor)
                value += amplitudes.getDouble(i) * noise * lowestFreqValueFactor
            }
            lowestFreqInputFactor *= 2.0
            lowestFreqValueFactor /= 2.0
        }
        return value
    }

    override fun getValue(x: Double, y: Double, yScale: Double, yMax: Double) = getValue(x, y, 0.0, yScale, yMax, false)
}

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

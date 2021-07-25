package org.kryptonmc.krypton.util.noise

import org.kryptonmc.krypton.util.clampedLerp
import org.kryptonmc.krypton.util.random.RandomSource

class BlendedNoise(
    private val minLimitNoise: PerlinNoise,
    private val maxLimitNoise: PerlinNoise,
    private val mainNoise: PerlinNoise
) {

    constructor(random: RandomSource) : this(PerlinNoise(random, -15..0), PerlinNoise(random, -15..0), PerlinNoise(random, -7..0))

    fun sampleAndClamp(x: Int, y: Int, z: Int, horizontalScale: Double, verticalScale: Double, horizontalStretch: Double, verticalStretch: Double): Double {
        var amp = 0.0
        var freq = 0.0
        var max = 1.0
        var result = 0.0
        for (i in 0 until 8) {
            val octaveNoise = mainNoise.getOctaveNoise(i)
            if (octaveNoise != null) result += octaveNoise.noise((x.toDouble() * horizontalStretch * max).wrap(), (y.toDouble() * verticalStretch * max).wrap(), (z.toDouble() * horizontalStretch * max).wrap(), verticalStretch * max, y.toDouble() * verticalStretch * max) / max
            max /= 2
        }
        val total = (result / 10 + 1) / 2
        max = 1.0
        for (i in 0 until 16) {
            val scaleX = (x.toDouble() * horizontalScale * max).wrap()
            val scaleY = (y.toDouble() * verticalScale * max).wrap()
            val scaleZ = (z.toDouble() * horizontalScale * max).wrap()
            val maxScale = verticalScale * max
            if (total >= 1) minLimitNoise.getOctaveNoise(i)?.let { amp += it.noise(scaleX, scaleY, scaleZ, maxScale, y.toDouble() * maxScale) / max }
            if (total <= 0) maxLimitNoise.getOctaveNoise(i)?.let { freq += it.noise(scaleX, scaleY, scaleZ, maxScale, y.toDouble() * maxScale) / max }
            max /= 2
        }
        return total.clampedLerp(amp / 512, freq / 512)
    }
}

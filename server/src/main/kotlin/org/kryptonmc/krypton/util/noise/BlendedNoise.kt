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

import org.kryptonmc.krypton.util.clampedLerp
import org.kryptonmc.krypton.util.random.RandomSource

class BlendedNoise(
    private val minLimitNoise: PerlinNoise,
    private val maxLimitNoise: PerlinNoise,
    private val mainNoise: PerlinNoise
) {

    constructor(random: RandomSource) : this(
        PerlinNoise(random, -15..0),
        PerlinNoise(random, -15..0),
        PerlinNoise(random, -7..0)
    )

    fun sampleAndClamp(
        x: Int,
        y: Int,
        z: Int,
        horizontalScale: Double,
        verticalScale: Double,
        horizontalStretch: Double,
        verticalStretch: Double
    ): Double {
        var amp = 0.0
        var freq = 0.0
        var max = 1.0
        var result = 0.0
        for (i in 0 until 8) {
            val octaveNoise = mainNoise.getOctaveNoise(i)
            if (octaveNoise != null) result += octaveNoise.noise(
                (x.toDouble() * horizontalStretch * max).wrap(),
                (y.toDouble() * verticalStretch * max).wrap(),
                (z.toDouble() * horizontalStretch * max).wrap(),
                verticalStretch * max,
                y.toDouble() * verticalStretch * max
            ) / max
            max /= 2
        }
        val total = (result / 10 + 1) / 2
        max = 1.0
        for (i in 0 until 16) {
            val scaleX = (x.toDouble() * horizontalScale * max).wrap()
            val scaleY = (y.toDouble() * verticalScale * max).wrap()
            val scaleZ = (z.toDouble() * horizontalScale * max).wrap()
            val maxScale = verticalScale * max
            if (total >= 1) minLimitNoise.getOctaveNoise(i)?.let {
                amp += it.noise(scaleX, scaleY, scaleZ, maxScale, y.toDouble() * maxScale) / max
            }
            if (total <= 0) maxLimitNoise.getOctaveNoise(i)?.let {
                freq += it.noise(scaleX, scaleY, scaleZ, maxScale, y.toDouble() * maxScale) / max
            }
            max /= 2
        }
        return total.clampedLerp(amp / 512, freq / 512)
    }
}

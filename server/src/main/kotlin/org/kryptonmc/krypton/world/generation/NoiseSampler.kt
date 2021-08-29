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
package org.kryptonmc.krypton.world.generation

import org.kryptonmc.krypton.util.clampedLerp
import org.kryptonmc.krypton.util.noise.BlendedNoise
import org.kryptonmc.krypton.util.noise.PerlinNoise
import org.kryptonmc.krypton.util.noise.SimplexNoise
import org.kryptonmc.krypton.world.biome.gen.BiomeGenerator
import org.kryptonmc.krypton.world.biome.gen.TheEndBiomeGenerator.Companion.getHeightValue
import org.kryptonmc.krypton.world.generation.noise.NoiseModifier
import org.kryptonmc.krypton.world.generation.noise.NoiseSettings
import kotlin.math.min
import kotlin.math.sqrt

class NoiseSampler(
    private val biomeGenerator: BiomeGenerator,
    private val cellWidth: Int,
    private val cellHeight: Int,
    private val cellCountY: Int,
    private val noiseSettings: NoiseSettings,
    private val blendedNoise: BlendedNoise,
    private val islandNoise: SimplexNoise?,
    private val depthNoise: PerlinNoise,
    private val caveNoiseModifier: NoiseModifier
) {

    private val topSlideTarget = noiseSettings.topSlide.target
    private val topSlideSize = noiseSettings.topSlide.size
    private val topSlideOffset = noiseSettings.topSlide.offset
    private val bottomSlideTarget = noiseSettings.bottomSlide.target
    private val bottomSlideSize = noiseSettings.bottomSlide.size
    private val bottomSlideOffset = noiseSettings.bottomSlide.offset
    private val dimensionDensityFactor = noiseSettings.densityFactor
    private val dimensionDensityOffset = noiseSettings.densityOffset

    fun fillNoiseColumn(buffer: DoubleArray, x: Int, z: Int, settings: NoiseSettings, seaLevel: Int, minY: Int, noiseSizeY: Int) {
        val height: Double
        val factor: Double
        if (islandNoise != null) {
            height = islandNoise.getHeightValue(x, z).toDouble()
            factor = if (height > 0.0) 0.25 else 1.0
        } else {
            var weightedScale = 0F
            var weightedDepth = 0F
            var totalWeight = 0F
            val sourceDepth = biomeGenerator[x, seaLevel, z].depth
            for (xo in -2..2) {
                for (zo in -2..2) {
                    val biome = biomeGenerator[x + xo, seaLevel, z + zo]
                    val depth = biome.depth
                    val scale = biome.scale
                    val modifiedDepth: Float
                    val modifiedScale: Float
                    if (settings.isAmplified && depth > 0F) {
                        modifiedDepth = 1F + depth * 2F
                        modifiedScale = 1F + depth * 4F
                    } else {
                        modifiedDepth = depth
                        modifiedScale = scale
                    }
                    val modifiedFactor = if (depth > sourceDepth) 0.5F else 1F
                    val weight = modifiedFactor * BIOME_WEIGHTS[xo + 2 + (zo + 2) * 5] / (modifiedDepth + 2F)
                    weightedScale += modifiedScale * weight
                    weightedDepth += modifiedDepth * weight
                    totalWeight += weight
                }
            }
            val totalDepth = weightedDepth / totalWeight
            val totalScale = weightedScale / totalWeight
            val scaledDepth = (totalDepth * 0.5F - 0.125F).toDouble()
            val scaledScale = (totalScale * 0.9F + 0.1F).toDouble()
            height = scaledDepth * 0.265625
            factor = 96.0 / scaledScale
        }
        val horizontalScale = 684.412 * settings.sampling.xzScale
        val verticalScale = 684.412 * settings.sampling.yScale
        val horizontalStretch = horizontalScale / settings.sampling.xzFactor
        val verticalStretch = verticalScale / settings.sampling.yFactor
        val density = if (settings.randomDensityOffset) getRandomDensity(x, z) else 0.0
        for (i in 0..noiseSizeY) {
            val y = i + minY
            val blendedValue = blendedNoise.sampleAndClamp(x, y, z, horizontalScale, verticalScale, horizontalStretch, verticalStretch)
            var initialDensity = computeInitialDensity(y, height, factor, density) + blendedValue
            initialDensity = caveNoiseModifier(initialDensity, y * cellHeight, x * cellWidth, z * cellWidth)
            initialDensity = applySlide(initialDensity, y)
            buffer[i] = initialDensity
        }
    }

    private fun computeInitialDensity(y: Int, depth: Double, scale: Double, randomDensityOffset: Double): Double {
        val density = 1.0 - y.toDouble() * 2.0 / 32.0 + randomDensityOffset
        val dimensionDensity = density * dimensionDensityFactor + dimensionDensityOffset
        val value = (dimensionDensity + depth) * scale
        return value * (if (value > 0.0) 4 else 1).toDouble()
    }

    private fun applySlide(noise: Double, y: Int): Double {
        val bottomY = noiseSettings.minimumY.floorDiv(cellHeight)
        val diffY = y - bottomY
        var temp = noise
        if (topSlideSize > 0.0) {
            val max = ((cellCountY - diffY).toDouble() - topSlideOffset) / topSlideSize
            temp = max.clampedLerp(topSlideTarget.toDouble(), temp)
        }
        if (bottomSlideSize > 0.0) {
            val max = (diffY.toDouble() - bottomSlideOffset) / bottomSlideSize
            temp = max.clampedLerp(bottomSlideTarget.toDouble(), temp)
        }
        return temp
    }

    private fun getRandomDensity(x: Int, z: Int): Double {
        val noiseValue = depthNoise.getValue((x * 200).toDouble(), 10.0, (z * 200).toDouble(), 1.0, 0.0, true)
        val scaled = if (noiseValue < 0.0) -noiseValue * 0.3 else noiseValue
        val value = scaled * 24.575625 - 2.0
        return if (value < 0.0) value * 0.009486607142857142 else min(value, 1.0) * 0.006640625
    }

    companion object {

        private const val OLD_CELL_COUNT_Y = 32
        private val BIOME_WEIGHTS = FloatArray(25).apply {
            for (x in -2..2) {
                for (z in -2..2) {
                    val value = 10F / sqrt((x * x + z * z).toFloat() + 0.2F)
                    this[x + 2 + (z + 2) * 5] = value
                }
            }
        }
    }
}

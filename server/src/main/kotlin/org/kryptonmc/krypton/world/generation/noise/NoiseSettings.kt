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
package org.kryptonmc.krypton.world.generation.noise

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import java.util.function.Function

@JvmRecord
data class NoiseSettings(
    val minimumY: Int,
    val height: Int,
    val sampling: NoiseSampling,
    val topSlide: NoiseSlide,
    val bottomSlide: NoiseSlide,
    val horizontalSize: Int,
    val verticalSize: Int,
    val densityFactor: Double,
    val densityOffset: Double,
    val useSimplexSurfaceNoise: Boolean,
    val randomDensityOffset: Boolean,
    val islandNoiseOverride: Boolean,
    val isAmplified: Boolean
) {

    companion object {

        val CODEC: Codec<NoiseSettings> = RecordCodecBuilder.create<NoiseSettings> {
            it.group(
                Codec.intRange(KryptonDimensionType.MIN_Y, KryptonDimensionType.MAX_Y).fieldOf("min_y").forGetter(NoiseSettings::minimumY),
                Codec.intRange(0, KryptonDimensionType.Y_SIZE).fieldOf("height").forGetter(NoiseSettings::height),
                NoiseSampling.CODEC.fieldOf("sampling").forGetter(NoiseSettings::sampling),
                NoiseSlide.CODEC.fieldOf("top_slide").forGetter(NoiseSettings::topSlide),
                NoiseSlide.CODEC.fieldOf("bottom_slide").forGetter(NoiseSettings::bottomSlide),
                Codec.intRange(1, 4).fieldOf("size_horizontal").forGetter(NoiseSettings::horizontalSize),
                Codec.intRange(1, 4).fieldOf("size_vertical").forGetter(NoiseSettings::verticalSize),
                Codec.DOUBLE.fieldOf("density_factor").forGetter(NoiseSettings::densityFactor),
                Codec.DOUBLE.fieldOf("density_offset").forGetter(NoiseSettings::densityOffset),
                Codec.BOOL.fieldOf("simplex_surface_noise").forGetter(NoiseSettings::useSimplexSurfaceNoise),
                Codec.BOOL.optionalFieldOf("random_density_offset", false, Lifecycle.experimental())
                    .forGetter(NoiseSettings::randomDensityOffset),
                Codec.BOOL.optionalFieldOf("island_noise_override", false, Lifecycle.experimental())
                    .forGetter(NoiseSettings::islandNoiseOverride),
                Codec.BOOL.optionalFieldOf("amplified", false, Lifecycle.experimental()).forGetter(NoiseSettings::isAmplified)
            ).apply(it, ::NoiseSettings)
        }.comapFlatMap(::guardY, Function.identity())

        private fun guardY(settings: NoiseSettings): DataResult<NoiseSettings> {
            if (settings.minimumY + settings.height > KryptonDimensionType.MAX_Y + 1) {
                return DataResult.error("Minimum Y + height cannot be greater than ${KryptonDimensionType.MAX_Y + 1}!")
            }
            if (settings.height % 16 != 0) return DataResult.error("Height must be a multiple of 16!")
            if (settings.minimumY % 16 != 0) return DataResult.error("Minimum Y must be a multiple of 16!")
            return DataResult.success(settings)
        }
    }
}

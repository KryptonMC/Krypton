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
package org.kryptonmc.krypton.world.biome

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class ClimateParameters(
    val temperature: Float,
    val humidity: Float,
    val altitude: Float,
    val weirdness: Float,
    val offset: Float
) {

    fun fitness(other: ClimateParameters) = (temperature - other.temperature) * (temperature - other.temperature) + (humidity - other.humidity) * (humidity - other.humidity) + (altitude - other.altitude) * (altitude - other.altitude) + (weirdness - other.weirdness) * (weirdness - other.weirdness) + (offset - other.offset) * (offset - other.offset)

    companion object {

        val CODEC: Codec<ClimateParameters> = RecordCodecBuilder.create {
            it.group(
                Codec.floatRange(-2F, 2F).fieldOf("temperature").forGetter(ClimateParameters::temperature),
                Codec.floatRange(-2F, 2F).fieldOf("humidity").forGetter(ClimateParameters::humidity),
                Codec.floatRange(-2F, 2F).fieldOf("altitude").forGetter(ClimateParameters::altitude),
                Codec.floatRange(-2F, 2F).fieldOf("weirdness").forGetter(ClimateParameters::weirdness),
                Codec.floatRange(0F, 1F).fieldOf("offset").forGetter(ClimateParameters::offset)
            ).apply(it, ::ClimateParameters)
        }
    }
}

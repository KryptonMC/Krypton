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
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.world.biome.Climate
import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.api.world.biome.Precipitations
import org.kryptonmc.api.world.biome.TemperatureModifier
import org.kryptonmc.api.world.biome.TemperatureModifiers

@JvmRecord
data class KryptonClimate(
    override val precipitation: Precipitation,
    override val temperature: Float,
    override val downfall: Float,
    override val temperatureModifier: TemperatureModifier = TemperatureModifiers.NONE
) : Climate {

    override fun toBuilder(): Climate.Builder = Builder(this)

    class Builder() : Climate.Builder {

        private var precipitation = Precipitations.NONE
        private var temperature = 0F
        private var downfall = 0F
        private var temperatureModifier = TemperatureModifiers.NONE

        constructor(climate: Climate) : this() {
            precipitation = climate.precipitation
            temperature = climate.temperature
            downfall = climate.downfall
            temperatureModifier = climate.temperatureModifier
        }

        override fun precipitation(precipitation: Precipitation): Climate.Builder = apply { this.precipitation = precipitation }

        override fun temperature(temperature: Float): Climate.Builder = apply { this.temperature = temperature }

        override fun downfall(downfall: Float): Climate.Builder = apply { this.downfall = downfall }

        override fun temperatureModifier(modifier: TemperatureModifier): Climate.Builder = apply { temperatureModifier = modifier }

        override fun build(): Climate = KryptonClimate(precipitation, temperature, downfall, temperatureModifier)
    }

    object Factory : Climate.Factory {

        override fun of(
            precipitation: Precipitation,
            temperature: Float,
            downfall: Float,
            temperatureModifier: TemperatureModifier
        ): Climate = KryptonClimate(precipitation, temperature, downfall, temperatureModifier)

        override fun builder(): Climate.Builder = Builder()
    }

    companion object {

        @JvmField
        val DEFAULT: Climate = Builder().build()

        @JvmField
        val CODEC: MapCodec<Climate> = RecordCodecBuilder.mapCodec {
            it.group(
                KryptonPrecipitation.CODEC.fieldOf("precipitation").forGetter(Climate::precipitation),
                Codec.FLOAT.fieldOf("temperature").forGetter(Climate::temperature),
                Codec.FLOAT.fieldOf("downfall").forGetter(Climate::downfall),
                KryptonTemperatureModifier.CODEC
                    .optionalFieldOf("temperature_modifier", TemperatureModifiers.NONE)
                    .forGetter(Climate::temperatureModifier)
            ).apply(it, ::KryptonClimate)
        }
    }
}

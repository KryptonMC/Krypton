/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.world.biome.data

import org.kryptonmc.api.world.biome.Climate
import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.api.world.biome.TemperatureModifier
import org.kryptonmc.krypton.util.serialization.EnumCodecs
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.MapCodec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

@JvmRecord
data class KryptonClimate(
    override val precipitation: Precipitation,
    override val temperature: Float,
    override val downfall: Float,
    override val temperatureModifier: TemperatureModifier = TemperatureModifier.NONE
) : Climate {

    class Builder : Climate.Builder {

        private var precipitation = Precipitation.NONE
        private var temperature = 0F
        private var downfall = 0F
        private var temperatureModifier = TemperatureModifier.NONE

        override fun precipitation(precipitation: Precipitation): Climate.Builder = apply { this.precipitation = precipitation }

        override fun temperature(temperature: Float): Climate.Builder = apply { this.temperature = temperature }

        override fun downfall(downfall: Float): Climate.Builder = apply { this.downfall = downfall }

        override fun temperatureModifier(modifier: TemperatureModifier): Climate.Builder = apply { temperatureModifier = modifier }

        override fun build(): Climate = KryptonClimate(precipitation, temperature, downfall, temperatureModifier)
    }

    object Factory : Climate.Factory {

        override fun builder(): Climate.Builder = Builder()
    }

    companion object {

        @JvmField
        val DEFAULT: Climate = Builder().build()

        @JvmField
        val CODEC: MapCodec<Climate> = RecordCodecBuilder.createMap { instance ->
            instance.group(
                EnumCodecs.PRECIPITATION.fieldOf("precipitation").getting { it.precipitation },
                Codec.FLOAT.fieldOf("temperature").getting { it.temperature },
                Codec.FLOAT.fieldOf("downfall").getting { it.downfall },
                EnumCodecs.TEMPERATURE_MODIFIER.fieldOf("temperature_modifier").getting { it.temperatureModifier }
            ).apply(instance, ::KryptonClimate)
        }
    }
}

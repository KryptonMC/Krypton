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

import org.kryptonmc.api.util.StringSerializable

data class ClimateSettings(
    val precipitation: Precipitation,
    val temperature: Float,
    val downfall: Float,
    val temperatureModifier: TemperatureModifier = TemperatureModifier.NONE
)

enum class Precipitation(override val serialized: String) : StringSerializable {

    NONE("none"),
    RAIN("rain"),
    SNOW("snow");

    companion object {

        private val BY_NAME = values().associateBy { it.serialized }

        fun fromName(name: String) = BY_NAME.getValue(name)
    }
}

enum class TemperatureModifier(override val serialized: String) : StringSerializable {

    NONE("none"),
    FROZEN("frozen");

    companion object {

        private val BY_NAME = values().associateBy { it.serialized }

        fun fromName(name: String) = BY_NAME.getValue(name)
    }
}

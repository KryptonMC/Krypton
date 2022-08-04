/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.util.serialization

import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.api.world.biome.GrassColorModifier
import org.kryptonmc.api.world.biome.TemperatureModifier
import org.kryptonmc.serialization.Codec
import java.util.function.Supplier

object EnumCodecs {

    private const val PRE_BUILT_LOOKUP_MAP_THRESHOLD = 16

    @JvmField
    val TEMPERATURE_MODIFIER: Codec<TemperatureModifier> = of(TemperatureModifier::values)
    @JvmField
    val PRECIPITATION: Codec<Precipitation> = of(Precipitation::values)
    @JvmField
    val GRASS_COLOR_MODIFIER: Codec<GrassColorModifier> = of(GrassColorModifier::values)

    @JvmStatic
    fun <E : Enum<E>> of(valueSupplier: Supplier<Array<E>>): EnumCodec<E> {
        val values = valueSupplier.get()
        // We only create a lookup map if the amount of values is large enough that the map would really be beneficial.
        if (values.size > PRE_BUILT_LOOKUP_MAP_THRESHOLD) {
            val valueMap = values.associateBy { it.name.lowercase() }
            return EnumCodec(valueMap::get)
        }
        return EnumCodec { name -> values.firstOrNull { it.name.lowercase() == name } }
    }
}

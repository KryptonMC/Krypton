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
package org.kryptonmc.krypton.util.serialization

import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.api.world.biome.GrassColorModifier
import org.kryptonmc.api.world.biome.TemperatureModifier
import org.kryptonmc.serialization.Codec
import java.util.function.Function
import java.util.function.Supplier

object EnumCodecs {

    private const val PRE_BUILT_LOOKUP_MAP_THRESHOLD = 16

    @JvmField
    val TEMPERATURE_MODIFIER: Codec<TemperatureModifier> = of { TemperatureModifier.values() }
    @JvmField
    val PRECIPITATION: Codec<Precipitation> = of { Precipitation.values() }
    @JvmField
    val GRASS_COLOR_MODIFIER: Codec<GrassColorModifier> = of { GrassColorModifier.values() }

    @JvmStatic
    fun <E : Enum<E>> of(valueSupplier: Supplier<Array<E>>): EnumCodec<E> = of(valueSupplier) { it.name.lowercase() }

    @JvmStatic
    fun <E : Enum<E>> of(valueSupplier: Supplier<Array<E>>, toName: Function<E, String>): EnumCodec<E> {
        val values = valueSupplier.get()
        // We only create a lookup map if the amount of values is large enough that the map would really be beneficial.
        if (values.size > PRE_BUILT_LOOKUP_MAP_THRESHOLD) {
            val valueMap = values.associateBy(toName::apply)
            return EnumCodec { valueMap.get(it) }
        }
        return EnumCodec { name -> values.firstOrNull { toName.apply(it) == name } }
    }
}

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
package org.kryptonmc.krypton.data.provider.entity

import org.kryptonmc.api.data.Key
import org.kryptonmc.api.data.Keys
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import org.kryptonmc.krypton.entity.aquatic.KryptonTropicalFish
import org.kryptonmc.krypton.entity.aquatic.TropicalFishVariants
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import java.util.function.IntFunction

object TropicalFishData {

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.registerVariantPart(Keys.BASE_COLOR, TropicalFishVariants::extractBaseColor, TropicalFishVariants::modifyBaseColor)
        registrar.registerVariantPart(Keys.PATTERN_COLOR, TropicalFishVariants::extractPatternColor, TropicalFishVariants::modifyPatternColor)
        registrar.registerVariantPart(Keys.TROPICAL_FISH_VARIANT, TropicalFishVariants::extractVariant, TropicalFishVariants::modifyVariant)
    }
}

private fun interface Encoder<E> {

    fun encode(variant: Int, value: E): Int
}

private fun <E> DataProviderRegistrar.registerVariantPart(key: Key<E>, extractor: IntFunction<E>, encoder: Encoder<E>) {
    registerMutable<KryptonTropicalFish, E>(key, { extractor.apply(it.data[MetadataKeys.TROPICAL_FISH.VARIANT]) }) { holder, value ->
        val variant = holder.data[MetadataKeys.TROPICAL_FISH.VARIANT]
        holder.data[MetadataKeys.TROPICAL_FISH.VARIANT] = encoder.encode(variant, value)
    }
}

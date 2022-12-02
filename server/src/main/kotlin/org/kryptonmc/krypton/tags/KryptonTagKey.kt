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
package org.kryptonmc.krypton.tags

import com.google.common.collect.Interners
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataResult

@JvmRecord
data class KryptonTagKey<T>(override val registry: ResourceKey<out Registry<T>>, override val location: Key) : TagKey<T> {

    object Factory : TagKey.Factory {

        override fun <T> of(registry: ResourceKey<out Registry<T>>, location: Key): TagKey<T> = KryptonTagKey.of(registry, location)
    }

    companion object {

        private val VALUES = Interners.newWeakInterner<KryptonTagKey<*>>()

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T> of(registry: ResourceKey<out Registry<T>>, location: Key): KryptonTagKey<T> =
            VALUES.intern(KryptonTagKey(registry, location)) as KryptonTagKey<T>

        @JvmStatic
        fun <T> hashedCodec(registry: ResourceKey<out Registry<T>>): Codec<TagKey<T>> = Codec.STRING.comapFlatMap(
            { input -> if (input.startsWith('#')) Keys.read(input).map { of(registry, it) } else DataResult.error("$input is not a valid tag ID!") },
            { "#${it.location}" }
        )
    }
}

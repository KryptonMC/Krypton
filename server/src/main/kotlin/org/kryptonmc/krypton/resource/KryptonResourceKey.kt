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
package org.kryptonmc.krypton.resource

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import java.util.Collections
import java.util.IdentityHashMap

class KryptonResourceKey<T> private constructor(override val registry: Key, override val location: Key) : ResourceKey<T> {

    override fun toString(): String = "ResourceKey[$registry / $location]"

    object Factory : ResourceKey.Factory {

        override fun <T> of(registry: Key, location: Key): ResourceKey<T> = KryptonResourceKey.of(registry, location)
    }

    companion object {

        private val VALUES = Collections.synchronizedMap(IdentityHashMap<String, ResourceKey<*>>())

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T> of(registry: Key, location: Key): ResourceKey<T> {
            val key = "$registry:$location".intern()
            return VALUES.computeIfAbsent(key) { KryptonResourceKey<T>(registry, location) } as ResourceKey<T>
        }

        @JvmStatic
        fun <T> of(parent: ResourceKey<out Registry<T>>, location: Key): ResourceKey<T> = of(parent.location, location)

        @JvmStatic
        fun <T> codec(key: ResourceKey<out Registry<T>>): Codec<ResourceKey<T>> = Codecs.KEY.xmap({ of(key, it) }, ResourceKey<T>::location)
    }
}

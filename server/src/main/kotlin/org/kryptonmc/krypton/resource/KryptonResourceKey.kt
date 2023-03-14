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
package org.kryptonmc.krypton.resource

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.util.Keys
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
        fun <T> codec(key: ResourceKey<out Registry<T>>): Codec<ResourceKey<T>> = Keys.CODEC.xmap({ of(key, it) }, { it.location })
    }
}

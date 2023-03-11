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
package org.kryptonmc.krypton.registry.dynamic

import com.google.common.collect.ImmutableMap
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.holder.HolderLookup
import org.kryptonmc.krypton.util.ImmutableMaps
import java.util.stream.Collectors
import java.util.stream.Stream

interface RegistryAccess : HolderLookup.Provider {

    fun <E> getRegistry(key: ResourceKey<out Registry<out E>>): KryptonRegistry<E>?

    override fun <T> lookup(key: ResourceKey<out Registry<out T>>): HolderLookup.ForRegistry<T>? = getRegistry(key)?.asLookup()

    fun <E> registryOrThrow(key: ResourceKey<out Registry<out E>>): KryptonRegistry<E> = getRegistry(key) ?: error("Missing required registry $key!")

    fun registries(): Stream<RegistryEntry<*>>

    fun freeze(): Frozen {
        class FrozenAccess(entries: Stream<RegistryEntry<*>>) : ImmutableImpl(entries), Frozen
        return FrozenAccess(registries().map { it.freeze() })
    }

    interface Frozen : RegistryAccess

    open class ImmutableImpl : RegistryAccess {

        private val registries: Map<out RegistryKey<*>, KryptonRegistry<*>>

        constructor(map: Map<out RegistryKey<*>, KryptonRegistry<*>>) {
            registries = ImmutableMaps.copyOf(map)
        }

        constructor(registries: List<KryptonRegistry<*>>) {
            this.registries = registries.stream().collect(Collectors.toUnmodifiableMap({ it.key }, { it }))
        }

        constructor(entries: Stream<RegistryEntry<*>>) {
            registries = entries.collect(ImmutableMap.toImmutableMap({ it.key }, { it.value }))
        }

        @Suppress("UNCHECKED_CAST")
        override fun <E> getRegistry(key: RegistryKey<out E>): KryptonRegistry<E>? = registries.get(key) as? KryptonRegistry<E>

        override fun registries(): Stream<RegistryEntry<*>> = registries.entries.stream().map { RegistryEntry.fromMapEntry(it) }
    }

    @JvmRecord
    data class RegistryEntry<T>(val key: ResourceKey<out Registry<T>>, val value: KryptonRegistry<T>) {

        fun freeze(): RegistryEntry<T> = RegistryEntry(key, value.freeze())

        companion object {

            @JvmStatic
            @Suppress("UNCHECKED_CAST")
            fun <T, R : KryptonRegistry<out T>> fromMapEntry(entry: Map.Entry<RegistryKey<*>, R>): RegistryEntry<T> =
                RegistryEntry(entry.key as RegistryKey<T>, entry.value as KryptonRegistry<T>)
        }
    }

    companion object {

        @JvmField
        val EMPTY: Frozen = ImmutableImpl(ImmutableMaps.of()).freeze()

        @JvmStatic
        fun fromRegistryOfRegistries(registry: KryptonRegistry<out KryptonRegistry<*>>): Frozen = object : Frozen {
            @Suppress("UNCHECKED_CAST")
            override fun <E> getRegistry(key: ResourceKey<out Registry<out E>>): KryptonRegistry<E>? {
                val temp = registry as KryptonRegistry<KryptonRegistry<E>>
                return temp.get(key as ResourceKey<KryptonRegistry<E>>)
            }

            override fun registries(): Stream<RegistryEntry<*>> = registry.entries().stream().map { RegistryEntry.fromMapEntry(it) }

            override fun freeze(): Frozen = this
        }
    }
}

private typealias RegistryKey<T> = ResourceKey<out Registry<T>>

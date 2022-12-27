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

    fun <E> registry(key: ResourceKey<out Registry<out E>>): KryptonRegistry<E>?

    override fun <T> lookup(key: ResourceKey<out Registry<out T>>): HolderLookup.ForRegistry<T>? = registry(key)?.asLookup()

    fun <E> registryOrThrow(key: ResourceKey<out Registry<out E>>): KryptonRegistry<E> = registry(key) ?: error("Missing required registry $key!")

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
        override fun <E> registry(key: RegistryKey<out E>): KryptonRegistry<E>? = registries.get(key) as? KryptonRegistry<E>

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
            override fun <E> registry(key: ResourceKey<out Registry<out E>>): KryptonRegistry<E>? {
                val temp = registry as KryptonRegistry<KryptonRegistry<E>>
                return temp.get(key as ResourceKey<KryptonRegistry<E>>)
            }

            override fun registries(): Stream<RegistryEntry<*>> = registry.entries.stream().map { RegistryEntry.fromMapEntry(it) }

            override fun freeze(): Frozen = this
        }
    }
}

private typealias RegistryKey<T> = ResourceKey<out Registry<T>>

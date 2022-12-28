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
package org.kryptonmc.krypton.registry.network

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryRoots
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.registry.holder.HolderGetter
import org.kryptonmc.krypton.registry.holder.HolderLookup
import org.kryptonmc.krypton.registry.holder.HolderOwner
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.serialization.DelegatingOps
import org.kryptonmc.serialization.DataOps
import org.kryptonmc.serialization.DataResult
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

class RegistryOps<T> private constructor(delegate: DataOps<T>, private val lookupProvider: RegistryInfoLookup) : DelegatingOps<T>(delegate) {

    fun <E> owner(key: ResourceKey<out Registry<out E>>): HolderOwner<E>? = lookupProvider.lookup(key)?.owner

    fun <E> getter(key: ResourceKey<out Registry<out E>>): HolderGetter<E>? = lookupProvider.lookup(key)?.getter

    @JvmRecord
    data class RegistryInfo<T>(val owner: HolderOwner<T>, val getter: HolderGetter<T>)

    interface RegistryInfoLookup {

        fun <T> lookup(key: ResourceKey<out Registry<out T>>): RegistryInfo<T>?
    }

    companion object {

        @JvmStatic
        fun <T> create(delegate: DataOps<T>, provider: HolderLookup.Provider): RegistryOps<T> {
            return create(delegate, memoizeLookup(object : RegistryInfoLookup {
                override fun <T> lookup(key: ResourceKey<out Registry<out T>>): RegistryInfo<T>? = provider.lookup(key)?.let { RegistryInfo(it, it) }
            }))
        }

        @JvmStatic
        fun <T> create(delegate: DataOps<T>, lookup: RegistryInfoLookup): RegistryOps<T> = RegistryOps(delegate, lookup)

        @JvmStatic
        private fun memoizeLookup(lookup: RegistryInfoLookup): RegistryInfoLookup = object : RegistryInfoLookup {
            private val lookups = HashMap<ResourceKey<out Registry<*>>, RegistryInfo<*>?>()

            @Suppress("UNCHECKED_CAST")
            override fun <T> lookup(key: ResourceKey<out Registry<out T>>): RegistryInfo<T>? =
                lookups.computeIfAbsent(key) { lookup.lookup(it) } as? RegistryInfo<T>
        }

        @JvmStatic
        fun <E, O> retrieveGetter(key: ResourceKey<out Registry<out E>>): RecordCodecBuilder<O, HolderGetter<E>> {
            return Codecs.retrieveContext { ops ->
                if (ops !is RegistryOps<*>) return@retrieveContext DataResult.error("Not a registry ops!")
                ops.lookupProvider.lookup(key)?.let { DataResult.success(it.getter) } ?: DataResult.error("Unknown registry $key!")
            }.getting { null }
        }

        @JvmStatic
        fun <E, O> retrieveElement(key: ResourceKey<E>): RecordCodecBuilder<O, Holder.Reference<E>> {
            val registryKey: ResourceKey<out Registry<E>> = KryptonResourceKey.of(RegistryRoots.MINECRAFT, key.registry)
            return Codecs.retrieveContext { ops ->
                if (ops !is RegistryOps<*>) return@retrieveContext DataResult.error("Not a registry ops!")
                ops.lookupProvider.lookup(registryKey)?.getter?.get(key)?.let { DataResult.success(it) }
                    ?: DataResult.error("Cannot find value for key $key!")
            }.getting { null }
        }
    }
}

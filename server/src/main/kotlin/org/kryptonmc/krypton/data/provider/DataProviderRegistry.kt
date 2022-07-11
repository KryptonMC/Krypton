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
package org.kryptonmc.krypton.data.provider

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import java.util.concurrent.ConcurrentHashMap
import org.kryptonmc.api.data.DataProvider
import org.kryptonmc.api.data.Key
import org.kryptonmc.krypton.data.downcast

@Suppress("UNCHECKED_CAST")
object DataProviderRegistry {

    private val providers: Multimap<Key<*>, DataProvider<*>> = HashMultimap.create()
    private val providerCache = ConcurrentHashMap<LookupKey, DataProvider<*>>()
    private val providersCache = ConcurrentHashMap<Class<*>, Collection<DataProvider<*>>>()

    @JvmStatic
    fun <E> getProvider(key: Key<E>, holderType: Class<*>): DataProvider<E> =
        providerCache.computeIfAbsent(LookupKey(key, holderType), DataProviderRegistry::loadProvider) as DataProvider<E>

    fun getProviders(holderType: Class<*>): Collection<DataProvider<*>> {
        val existing = providersCache[holderType]
        if (existing != null) return existing
        val providers = providers.keySet().map { getProvider(it, holderType) }
        providersCache[holderType] = providers
        return providers
    }

    @JvmStatic
    fun register(provider: DataProvider<*>) {
        providers.put(provider.key, provider)
        providerCache.clear()
    }

    @JvmStatic
    fun registerDefaultProviders() {
        // None yet
    }

    @JvmStatic
    private fun loadProvider(key: LookupKey): DataProvider<*> = buildDelegate(key.key) {
        if (it !is AbstractDataProvider<*, *>) return@buildDelegate false
        it.holderType.isAssignableFrom(key.holderType)
    }

    @JvmStatic
    private fun <E> buildDelegate(key: Key<E>, predicate: (DataProvider<E>) -> Boolean): DataProvider<E> =
        buildDelegateProvider(key, (providers.get(key) as Collection<DataProvider<E>>).filter(predicate))

    @JvmStatic
    private fun <V> buildDelegateProvider(key: Key<V>, providers: List<DataProvider<V>>): DataProvider<V> {
        if (providers.isEmpty()) return key.downcast().emptyDataProvider
        if (providers.size == 1) return providers[0]
        return DelegateDataProvider(key, providers)
    }

    @JvmRecord
    private data class LookupKey(val key: Key<*>, val holderType: Class<*>)
}

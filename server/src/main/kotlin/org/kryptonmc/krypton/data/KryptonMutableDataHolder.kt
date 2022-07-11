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
package org.kryptonmc.krypton.data

import org.kryptonmc.api.data.DataProvider
import org.kryptonmc.api.data.Key
import org.kryptonmc.api.data.MutableDataHolder
import org.kryptonmc.krypton.util.ensureMutable
import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.function.Supplier
import kotlin.properties.ReadWriteProperty

interface KryptonMutableDataHolder : KryptonDataHolder, MutableDataHolder {

    override fun <E> set(key: Key<E>, value: E) {
        applyMutable(key) { provider, holder -> provider.set(holder, value) }
    }

    override fun remove(key: Key<*>) {
        applyMutable(key) { provider, holder -> provider.remove(holder) }
    }

    override fun <E> editCollection(key: Key<Collection<E>>, mutator: Consumer<MutableCollection<E>>) {
        applyMutable(key) { provider, holder ->
            val collection = provider[holder]?.ensureMutable() ?: key.downcast().defaultValueSupplier!!.get() as MutableCollection<E>
            mutator.accept(collection)
            provider.set(holder, collection)
        }
    }

    override fun <E> add(key: Key<Collection<E>>, value: E) {
        editCollection(key) { it.add(value) }
    }

    override fun <E> addAll(key: Key<Collection<E>>, values: Collection<E>) {
        editCollection(key) { it.addAll(values) }
    }

    override fun <E> remove(key: Key<Collection<E>>, value: E) {
        editCollection(key) { it.remove(value) }
    }

    override fun <E> removeAll(key: Key<Collection<E>>, predicate: Predicate<E>) {
        editCollection(key) { it.removeIf(predicate) }
    }

    override fun <E> removeAll(key: Key<Collection<E>>, values: Collection<E>) {
        editCollection(key) { it.removeAll(values.toSet()) }
    }

    override fun <K, V> editMap(key: Key<Map<K, V>>, mutator: Consumer<MutableMap<K, V>>) {
        applyMutable(key) { provider, holder ->
            val map = provider[holder]?.ensureMutable() ?: return@applyMutable
            mutator.accept(map)
            provider.set(holder, map)
        }
    }

    override fun <K, V> put(key: Key<Map<K, V>>, mapKey: K, mapValue: V) {
        editMap(key) { it[mapKey] = mapValue }
    }

    override fun <K, V> putAll(key: Key<Map<K, V>>, values: Map<K, V>) {
        editMap(key) { it.putAll(values) }
    }

    override fun <K, V> removeKey(key: Key<Map<K, V>>, mapKey: K) {
        editMap(key) { it.remove(mapKey) }
    }

    override fun <K, V> removeAll(key: Key<Map<K, V>>, values: Map<K, V>) {
        editMap(key) { map -> values.forEach { map.remove(it.key, it.value) } }
    }

    override fun <E> delegate(key: Key<E>): ReadWriteProperty<MutableDataHolder, E?> = ValueDelegate(key)

    fun <E> applyMutable(key: Key<E>, action: BiConsumer<DataProvider<E>, MutableDataHolder>) {
        apply(key, { provider, holder -> action.accept(provider, holder as MutableDataHolder) }, { null })
    }
}

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

import org.kryptonmc.api.data.DataHolder
import org.kryptonmc.api.data.DataProvider
import org.kryptonmc.api.data.Key
import org.kryptonmc.krypton.data.provider.DataProviderRegistry
import java.util.function.BiFunction
import java.util.function.Supplier

interface KryptonDataHolder : DataHolder {

    override val keys: Set<Key<*>>
        get() = DataProviderRegistry.getProviders(javaClass).filter { it[this] != null }.map { it.key }.toSet()

    fun <E> getProvider(key: Key<E>, holder: DataHolder): DataProvider<E> = DataProviderRegistry.getProvider(key, holder.javaClass)

    fun <E, T> apply(key: Key<E>, function: BiFunction<DataProvider<E>, DataHolder, T>, default: Supplier<T>): T {
        val provider = getProvider(key, this)
        if (provider.isSupported(this)) return function.apply(provider, this)
        return default.get()
    }

    override fun supports(key: Key<*>): Boolean = apply(key, { _, _ -> true }, { false })

    override fun <E> get(key: Key<E>): E? = apply(key, DataProvider<E>::get) { null }
}

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

import org.kryptonmc.api.data.ImmutableDataHolder
import org.kryptonmc.api.data.Key
import kotlin.properties.ReadOnlyProperty

@Suppress("UNCHECKED_CAST")
interface KryptonImmutableDataHolder<T : ImmutableDataHolder<T>> : KryptonDataHolder, ImmutableDataHolder<T> {

    override fun <E> set(key: Key<E>, value: E): T = getProvider(key, this).set(this as T, value)

    override fun remove(key: Key<*>): T = getProvider(key, this).remove(this as T)

    override fun <E> transform(key: Key<E>, transformation: (E) -> E): T {
        val provider = getProvider(key, this)
        val value = provider[this as T] ?: return this
        return provider.set(this, value)
    }

    override fun <E> delegate(key: Key<E>): ReadOnlyProperty<T, E?> = ValueDelegate(key)
}

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
import org.kryptonmc.krypton.data.provider.AbstractDataProvider

@Suppress("UNCHECKED_CAST")
abstract class DummyDataHolder<H : DummyDataHolder<H>> protected constructor(override val keys: Set<Key<*>>) : KryptonDataHolder {

    private val providers = mutableMapOf<ProviderKey, DataProvider<*>>()

    fun addProvider(provider: DataProvider<*>): H = apply {
        providers[ProviderKey(provider.key, (provider as AbstractDataProvider<*, *>).holderType)] = provider
    } as H

    override fun <E> getProvider(key: Key<E>, holder: DataHolder): DataProvider<E> =
        requireNotNull(providers[ProviderKey(key, holder.javaClass)]) { "Could not find provider for key $key!" } as DataProvider<E>

    @JvmRecord
    private data class ProviderKey(val key: Key<*>, val holderType: Class<*>)
}

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

import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.data.DataProvider
import org.kryptonmc.api.data.DataRegistration
import org.kryptonmc.api.data.Key

class KryptonDataRegistration(override val keys: Iterable<Key<*>>, private val providerMap: Multimap<Key<*>, DataProvider<*>>) : DataRegistration {

    @Suppress("UNCHECKED_CAST")
    override fun <E> providers(key: Key<E>): Collection<DataProvider<E>> = providerMap.get(key) as Collection<DataProvider<E>>

    class Builder : DataRegistration.Builder {

        private val keys = persistentListOf<Key<*>>().builder()
        private val providerMap = ImmutableMultimap.builder<Key<*>, DataProvider<*>>()

        override fun provider(provider: DataProvider<*>): Builder = apply { providerMap.put(provider.key, provider) }

        override fun key(key: Key<*>): Builder = apply { keys.add(key) }

        override fun keys(vararg keys: Key<*>): Builder = apply { this.keys.addAll(keys) }

        override fun keys(keys: Iterable<Key<*>>): Builder = apply { this.keys.addAll(keys) }

        override fun build(): KryptonDataRegistration = KryptonDataRegistration(keys.build(), providerMap.build())
    }

    object Factory : DataRegistration.Factory {

        override fun builder(): DataRegistration.Builder = Builder()
    }
}

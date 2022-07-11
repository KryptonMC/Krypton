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

import org.kryptonmc.api.data.DataHolder
import org.kryptonmc.api.data.DataProvider
import org.kryptonmc.api.data.ImmutableDataHolder
import org.kryptonmc.api.data.Key
import org.kryptonmc.api.data.MutableDataHolder

@JvmRecord
data class EmptyDataProvider<E>(override val key: Key<E>) : DataProvider<E> {

    override fun isSupported(holder: DataHolder): Boolean = false

    override fun get(holder: DataHolder): E? = null

    override fun set(holder: MutableDataHolder, value: E) {
        // Nothing to do
    }

    override fun remove(holder: MutableDataHolder) {
        // Nothing to do
    }

    override fun <I : ImmutableDataHolder<I>> set(holder: I, value: E): I = holder

    override fun <I : ImmutableDataHolder<I>> remove(holder: I): I = holder
}

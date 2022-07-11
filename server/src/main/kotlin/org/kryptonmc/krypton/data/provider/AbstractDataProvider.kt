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

import java.util.function.Function
import java.util.function.Predicate
import org.kryptonmc.api.data.DataHolder
import org.kryptonmc.api.data.DataProvider
import org.kryptonmc.api.data.ImmutableDataHolder
import org.kryptonmc.api.data.Key
import org.kryptonmc.api.data.MutableDataHolder

@Suppress("UNCHECKED_CAST")
abstract class AbstractDataProvider<H : DataHolder, E>(
    override val key: Key<E>,
    val holderType: Class<H>,
    private val supports: Predicate<H>?,
    private val getter: Function<H, E?>?
) : DataProvider<E> {

    override fun isSupported(holder: DataHolder): Boolean {
        if (!holderType.isInstance(holder)) return false
        if (supports == null) return true
        return supports.test(holder as H)
    }

    override fun get(holder: DataHolder): E? {
        if (!isSupported(holder) || getter == null) return null
        return getter.apply(holder as H)
    }

    protected fun isTypeAllowed(holder: DataHolder): Boolean = holderType.isInstance(holder)

    abstract class AbstractBuilder<B : DataProvider.BaseBuilder<B, H, E>, H : DataHolder, E>(
        protected val key: Key<E>,
        protected val holderType: Class<H>
    ) : DataProvider.BaseBuilder<B, H, E> {

        protected var supports: Predicate<H>? = null
        protected var getter: Function<H, E?>? = null

        override fun supports(supports: Predicate<H>): B = apply { this.supports = supports } as B

        override fun get(getter: Function<H, E?>): B = apply { this.getter = getter } as B
    }

    object Factory : DataProvider.Factory {

        override fun <H : MutableDataHolder, E> mutableBuilder(key: Key<E>, holderType: Class<H>): DataProvider.MutableBuilder<H, E> =
            MutableDataProvider.Builder(key, holderType)

        override fun <H : ImmutableDataHolder<H>, E> immutableBuilder(key: Key<E>, holderType: Class<H>): DataProvider.ImmutableBuilder<H, E> =
            ImmutableDataProvider.Builder(key, holderType)
    }
}

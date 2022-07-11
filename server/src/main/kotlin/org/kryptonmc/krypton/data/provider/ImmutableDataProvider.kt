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

import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Predicate
import org.kryptonmc.api.data.DataProvider
import org.kryptonmc.api.data.ImmutableDataHolder
import org.kryptonmc.api.data.Key
import org.kryptonmc.api.data.MutableDataHolder

@Suppress("UNCHECKED_CAST")
class ImmutableDataProvider<H : ImmutableDataHolder<H>, E>(
    key: Key<E>,
    holderType: Class<H>,
    supports: Predicate<H>?,
    getter: Function<H, E?>?,
    private val setter: BiFunction<H, E, H>?,
    private val remover: Function<H, H>?
) : AbstractDataProvider<H, E>(key, holderType, supports, getter) {

    override fun <I : ImmutableDataHolder<I>> set(holder: I, value: E): I {
        if (!isTypeAllowed(holder) || setter == null) return holder
        return setter.apply(holder as H, value) as I
    }

    override fun <I : ImmutableDataHolder<I>> remove(holder: I): I {
        if (!isTypeAllowed(holder) || remover == null) return holder
        return remover.apply(holder as H) as I
    }

    override fun set(holder: MutableDataHolder, value: E) {
        // Nothing to do since this provider is for immutable holders
    }

    override fun remove(holder: MutableDataHolder) {
        // Nothing to do since this provider is for immutable holders
    }

    class Builder<H : ImmutableDataHolder<H>, E>(
        key: Key<E>,
        holderType: Class<H>
    ) : AbstractBuilder<DataProvider.ImmutableBuilder<H, E>, H, E>(key, holderType), DataProvider.ImmutableBuilder<H, E> {

        private var setter: BiFunction<H, E, H>? = null
        private var remover: Function<H, H>? = null

        override fun set(setter: BiFunction<H, E, H>): Builder<H, E> = apply { this.setter = setter }

        override fun remove(remover: Function<H, H>): Builder<H, E> = apply { this.remover = remover }

        override fun build(): ImmutableDataProvider<H, E> = ImmutableDataProvider(key, holderType, supports, getter, setter, remover)
    }
}

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

import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import org.kryptonmc.api.data.DataProvider
import org.kryptonmc.api.data.ImmutableDataHolder
import org.kryptonmc.api.data.Key
import org.kryptonmc.api.data.MutableDataHolder

@Suppress("UNCHECKED_CAST")
class MutableDataProvider<H : MutableDataHolder, E>(
    key: Key<E>,
    holderType: Class<H>,
    supports: Predicate<H>?,
    getter: Function<H, E?>?,
    private val setter: BiConsumer<H, E>?,
    private val remover: Consumer<H>?
) : AbstractDataProvider<H, E>(key, holderType, supports, getter) {

    override fun set(holder: MutableDataHolder, value: E) {
        if (!isTypeAllowed(holder) || setter == null) return
        setter.accept(holder as H, value)
    }

    override fun remove(holder: MutableDataHolder) {
        if (!isTypeAllowed(holder) || remover == null) return
        remover.accept(holder as H)
    }

    override fun <H : ImmutableDataHolder<H>> set(holder: H, value: E): H = holder

    override fun <H : ImmutableDataHolder<H>> remove(holder: H): H = holder

    class Builder<H : MutableDataHolder, E>(
        key: Key<E>,
        holderType: Class<H>
    ) : AbstractBuilder<DataProvider.MutableBuilder<H, E>, H, E>(key, holderType), DataProvider.MutableBuilder<H, E> {

        private var setter: BiConsumer<H, E>? = null
        private var remover: Consumer<H>? = null

        override fun set(setter: BiConsumer<H, E>): Builder<H, E> = apply { this.setter = setter }

        override fun remove(remover: Consumer<H>): Builder<H, E> = apply { this.remover = remover }

        override fun build(): MutableDataProvider<H, E> = MutableDataProvider(key, holderType, supports, getter, setter, remover)
    }
}

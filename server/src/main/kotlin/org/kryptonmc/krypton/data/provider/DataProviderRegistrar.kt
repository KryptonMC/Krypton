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
import org.kryptonmc.api.data.DataRegistration
import org.kryptonmc.api.data.ImmutableDataHolder
import org.kryptonmc.api.data.Key
import org.kryptonmc.api.data.MutableDataHolder
import org.kryptonmc.api.data.immutableBuilder
import org.kryptonmc.api.data.mutableBuilder
import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Function

class DataProviderRegistrar {

    private val dataRegistrationBuilder = DataRegistration.builder()

    inline fun <reified H : MutableDataHolder, E> registerMutable(key: Key<E>, getter: Function<H, E?>, setter: BiConsumer<H, E>? = null) {
        registerMutable(key) {
            get(getter)
            if (setter != null) set(setter)
        }
    }

    inline fun <reified H : MutableDataHolder, E> registerMutableNoSet(key: Key<E>, getter: Function<H, E?>) {
        registerMutable(key, getter, null)
    }

    inline fun <reified H : MutableDataHolder, E> registerMutable(key: Key<E>, builder: DataProvider.MutableBuilder<H, E>.() -> Unit) {
        register(DataProvider.mutableBuilder<H, E>(key).apply(builder))
    }

    inline fun <reified H : ImmutableDataHolder<H>, E> registerImmutable(key: Key<E>, getter: Function<H, E?>, setter: BiFunction<H, E, H>? = null) {
        registerImmutable<H, E>(key) {
            get(getter)
            if (setter != null) set(setter)
        }
    }

    inline fun <reified H : ImmutableDataHolder<H>, E> registerImmutableNoSet(key: Key<E>, getter: Function<H, E?>) {
        registerImmutable(key, getter, null)
    }

    inline fun <reified H : ImmutableDataHolder<H>, E> registerImmutable(key: Key<E>, builder: DataProvider.ImmutableBuilder<H, E>.() -> Unit) {
        register(DataProvider.immutableBuilder<H, E>(key).apply(builder))
    }

    fun buildAndRegister() {
        val registration = dataRegistrationBuilder.build()
        registration.keys.forEach {
            registration.providers(it).forEach(DataProviderRegistry::register)
        }
    }

    fun <H : DataHolder, E> register(builder: DataProvider.BaseBuilder<*, H, E>) {
        val provider = builder.build()
        dataRegistrationBuilder.key(provider.key).provider(provider)
    }
}

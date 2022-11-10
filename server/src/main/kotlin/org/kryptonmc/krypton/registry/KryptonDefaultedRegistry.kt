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
package org.kryptonmc.krypton.registry

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.DefaultedRegistry
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import java.util.function.Function

/**
 * The defaulted registry implementation that stores a default key to search for when values are registered to store
 * the default value to return if no other value can be found.
 */
class KryptonDefaultedRegistry<T>(
    override val defaultKey: Key,
    key: ResourceKey<out Registry<T>>,
    customHolderProvider: Function<T, Holder.Reference<T>>?
) : KryptonSimpleRegistry<T>(key, customHolderProvider), DefaultedRegistry<T> {

    private var defaultValue: Holder<T>? = null

    private fun defaultValue(): T = checkNotNull(defaultValue) { "The default value was not initialized!" }.value()

    override fun register(id: Int, key: ResourceKey<T>, value: T): Holder<T> {
        val holder = super.register(id, key, value)
        if (defaultKey == key.location) defaultValue = holder
        return holder
    }

    override fun get(key: Key): T = super.get(key) ?: defaultValue()

    override fun get(key: ResourceKey<T>): T = super.get(key) ?: defaultValue()

    override fun get(id: Int): T = super.get(id) ?: defaultValue()

    override fun getKey(value: T): Key = super.getKey(value) ?: defaultKey
}

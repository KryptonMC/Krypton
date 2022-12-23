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
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.holder.Holder

/**
 * The defaulted registry implementation that stores a default key to search for when values are registered to store
 * the default value to return if no other value can be found.
 */
class KryptonDefaultedSimpleRegistry<T> private constructor(
    override val defaultKey: Key,
    key: ResourceKey<out Registry<T>>,
    intrusive: Boolean
) : KryptonSimpleRegistry<T>(key, intrusive), KryptonDefaultedRegistry<T> {

    private var defaultValue: Holder.Reference<T>? = null

    private fun defaultValue(): T = checkNotNull(defaultValue) { "The default value was not initialized!" }.value()

    override fun register(id: Int, key: ResourceKey<T>, value: T): Holder.Reference<T> {
        val holder = super.register(id, key, value)
        if (defaultKey == key.location) defaultValue = holder
        return holder
    }

    override fun get(key: Key): T = super.get(key) ?: defaultValue()

    override fun get(key: ResourceKey<T>): T = super.get(key) ?: defaultValue()

    override fun get(id: Int): T = super.get(id) ?: defaultValue()

    override fun getKey(value: T): Key = super.getKey(value) ?: defaultKey

    override fun getId(value: T): Int {
        val id = super.getId(value)
        return if (id == -1) super.getId(defaultValue()) else id
    }

    companion object {

        @JvmStatic
        fun <T> standard(key: ResourceKey<out Registry<T>>, defaultKey: Key): KryptonDefaultedSimpleRegistry<T> =
            KryptonDefaultedSimpleRegistry(defaultKey, key, false)

        @JvmStatic
        fun <T> intrusive(key: ResourceKey<out Registry<T>>, defaultKey: Key): KryptonDefaultedSimpleRegistry<T> =
            KryptonDefaultedSimpleRegistry(defaultKey, key, true)
    }
}

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

class KryptonDefaultedRegistry<T>(key: ResourceKey<out Registry<T>>, override val defaultKey: Key) : KryptonRegistry<T>(key), DefaultedRegistry<T> {

    private var internalDefault: T? = null
    override val defaultValue: T
        get() = internalDefault ?: error("The default value has not been registered for registry ${key.location}!")

    override fun get(key: Key): T = super.get(key) ?: defaultValue

    override fun get(id: Int): T = super.get(id) ?: defaultValue

    override fun get(key: ResourceKey<T>): T = super.get(key) ?: defaultValue

    override fun get(value: T): Key = super.get(value) ?: defaultKey

    override fun <V : T> register(id: Int, key: ResourceKey<T>, value: V): V {
        if (key.location == defaultKey) internalDefault = value
        return super<KryptonRegistry>.register(id, key, value)
    }
}

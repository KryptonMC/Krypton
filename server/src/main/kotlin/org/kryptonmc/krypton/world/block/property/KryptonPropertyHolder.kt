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
package org.kryptonmc.krypton.world.block.property

import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.block.property.PropertyHolder

interface KryptonPropertyHolder<T : PropertyHolder<T>> : PropertyHolder<T> {

    fun copy(newValues: Map<String, String>): T

    fun copy(key: String, value: String): T

    override fun <V : Comparable<V>> get(key: Property<V>): V? {
        val value = properties[key.name] ?: return null
        return key.fromString(value)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <V : Comparable<V>> set(key: Property<V>, value: V): T {
        require(value in key.values) { "Tried to set invalid property value $value for key $key! Accepted values: ${key.values}" }
        val existing = properties[key.name]
        if (existing != null && existing == value) return this as T
        return copy(key.name, key.toString(value))
    }

    override fun contains(key: Property<*>): Boolean = properties.containsKey(key.name)
}

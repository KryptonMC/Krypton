/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.registry.impl

import org.kryptonmc.krypton.registry.Registry

/**
 * Default implementation for [Registry], which is backed by a map of T to Int, and a list of T?
 *
 * @param values optional pre-filled values map
 *
 * @author Callum Seabrook
 */
open class MappedRegistry<T>(values: Map<out T, Int>? = null) : Registry<T> {

    private var nextId = 0
    private val byT = mutableMapOf<T, Int>()
    private val byId = mutableListOf<T?>()

    init {
        if (values != null) {
            byT.putAll(values)
            byId += values.keys
        }
    }

    override fun get(id: Int): T? {
        if (id >= 0 && id < byId.size) return byId[id]
        return null
    }

    override fun idOf(value: T) = byT[value] ?: -1

    operator fun set(id: Int, value: T) {
        byT[value] = id
        while (byId.size <= id) {
            byId += null
        }
        byId[id] = value
        if (nextId <= id) nextId = id + 1
    }

    operator fun plus(value: T) = set(nextId, value)

    override fun iterator() = byId.filterNotNull().iterator()

    val size = byT.size
}

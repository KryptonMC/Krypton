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
package org.kryptonmc.krypton.util

import org.kryptonmc.api.util.IdMap

class IdMapper<T>(values: Map<out T, Int>? = null) : IdMap<T> {

    private val byT = mutableMapOf<T, Int>()
    private val byId = mutableListOf<T?>()

    private var nextId = 0

    init {
        if (values != null) {
            byT.putAll(values)
            byId += values.keys
        }
    }

    operator fun set(value: T, id: Int) {
        byT[value] = id
        while (byId.size <= id) byId += null
        byId[id] = value
        if (nextId <= id) nextId = id + 1
    }

    operator fun plusAssign(value: T) = set(value, nextId)

    override fun idOf(value: T) = byT[value] ?: -1

    override fun get(id: Int): T? = if (id in byId.indices) byId[id] else null

    operator fun contains(id: Int) = get(id) != null

    override fun iterator() = byId.iterator().asSequence().filterNotNull().iterator()

    val size: Int
        get() = byT.size
}

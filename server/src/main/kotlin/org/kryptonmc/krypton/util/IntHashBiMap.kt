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
package org.kryptonmc.krypton.util

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap

/**
 * The most basic IntBiMap implementation, that uses a map to store T -> Int,
 * and an array to store Int -> T.
 */
class IntHashBiMap<T>(values: Map<out T, Int>? = null) : IntBiMap<T> {

    private val byT = Object2IntOpenHashMap<T>()
    private val byId = ArrayList<T?>()
    private var nextId = 0
    override val size: Int
        get() = byT.size

    init {
        byT.defaultReturnValue(-1)
        if (values != null) {
            byT.putAll(values)
            byId += values.keys
        }
    }

    operator fun set(value: T, id: Int) {
        byT[value] = id
        while (byId.size <= id) byId.add(null)
        byId[id] = value
        if (nextId <= id) nextId = id + 1
    }

    override fun idOf(value: T): Int = byT.getInt(value)

    override fun get(id: Int): T? = byId.getOrNull(id)

    override fun iterator(): Iterator<T> = byId.iterator().asSequence().filterNotNull().iterator()
}

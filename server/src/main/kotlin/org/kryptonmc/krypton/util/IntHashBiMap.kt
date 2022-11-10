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

import com.google.common.collect.Lists
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap

/**
 * The most basic IntBiMap implementation, that uses a map to store T -> Int,
 * and an array to store Int -> T.
 */
class IntHashBiMap<T>(expectedSize: Int) : IntBiMap<T> {

    private val idByValue = Object2IntOpenCustomHashMap<T>(expectedSize, IdentityHashStrategy.get()).apply { defaultReturnValue(-1) }
    private val valueById = Lists.newArrayListWithExpectedSize<T?>(expectedSize)
    private var nextId = 0
    override val size: Int
        get() = idByValue.size

    constructor() : this(DEFAULT_EXPECTED_SIZE)

    fun set(key: T, value: Int) {
        idByValue.put(key, value)
        while (valueById.size <= value) {
            valueById.add(null)
        }
        valueById.set(value, key)
        if (nextId <= value) nextId = value + 1
    }

    fun add(key: T) {
        set(key, nextId)
    }

    override fun getId(value: T): Int = idByValue.getInt(value)

    override fun get(id: Int): T? = valueById.getOrNull(id)

    override fun iterator(): Iterator<T> = valueById.iterator().filterNotNull()

    companion object {

        private const val DEFAULT_EXPECTED_SIZE = 512
    }
}

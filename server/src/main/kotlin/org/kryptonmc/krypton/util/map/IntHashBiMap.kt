/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.util.map

import com.google.common.collect.Lists
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap
import org.kryptonmc.krypton.util.collection.Iterables

/**
 * The most basic IntBiMap implementation, that uses a map to store T -> Int,
 * and an array to store Int -> T.
 */
class IntHashBiMap<T>(expectedSize: Int) : IntBiMap<T> {

    private val idByValue = Reference2IntOpenHashMap<T>(expectedSize).apply { defaultReturnValue(-1) }
    private val valueById = Lists.newArrayListWithExpectedSize<T?>(expectedSize)
    private var nextId = 0

    constructor() : this(DEFAULT_EXPECTED_SIZE)

    override fun size(): Int = idByValue.size

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

    override fun iterator(): Iterator<T> = Iterables.filterNotNull(valueById.iterator())

    companion object {

        private const val DEFAULT_EXPECTED_SIZE = 512
    }
}

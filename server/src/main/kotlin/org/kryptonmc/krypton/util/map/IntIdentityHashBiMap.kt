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

import org.kryptonmc.krypton.util.collection.Iterables
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.util.NoSpread
import kotlin.math.max

/**
 * An IntBiMap implementation that uses identity equality to store values.
 *
 * This should be replaced, as it is a copy and translation of vanilla's
 * `CrudeIncrementalIntIdentityHashBiMap`. It is also not entirely clear why it
 * is necessary.
 */
@Suppress("UNCHECKED_CAST") // Our casts are fine and should always succeed
class IntIdentityHashBiMap<K> : IntBiMap<K> {

    private var keys: Array<K?>
    private var values: IntArray
    private var byId: Array<K?>

    private var nextId = 0
    private var size = 0

    private constructor(initialCapacity: Int) {
        keys = newArray(initialCapacity)
        values = IntArray(initialCapacity)
        byId = newArray(initialCapacity)
    }

    private constructor(keys: Array<K?>, values: IntArray, byId: Array<K?>, nextId: Int, size: Int) {
        this.keys = keys
        this.values = values
        this.byId = byId
        this.nextId = nextId
        this.size = size
    }

    override fun size(): Int = size

    fun add(key: K): Int {
        val id = nextId()
        set(key, id)
        return id
    }

    fun set(key: K, value: Int) {
        val max = max(value, size + 1)
        if (max.toFloat() >= keys.size.toFloat() * LOAD_FACTOR) {
            var newSize = keys.size shl 1
            while (newSize < value) {
                newSize = newSize shl 1
            }
            grow(newSize)
        }
        val firstEmpty = findEmpty(hash(key))
        keys[firstEmpty] = key
        values[firstEmpty] = value
        byId[value] = key
        ++size
        if (value == nextId) ++nextId
    }

    override fun getId(value: K): Int = getValue(indexOf(value, hash(value)))

    override fun get(id: Int): K? {
        if (id >= 0 && id < byId.size) return byId[id]
        return null
    }

    private fun getValue(id: Int): Int = if (id == -1) -1 else values[id]

    private fun nextId(): Int {
        while (nextId < byId.size && byId[nextId] != null) {
            ++nextId
        }
        return nextId
    }

    private fun grow(newSize: Int) {
        val oldKeys = keys
        val oldValues = values
        val map = IntIdentityHashBiMap<K>(newSize)
        for (i in oldKeys.indices) {
            val key = oldKeys[i]
            if (key != null) map.set(key, oldValues[i])
        }
        keys = map.keys
        values = map.values
        byId = map.byId
        nextId = map.nextId
        size = map.size
    }

    private fun hash(key: K): Int = (Maths.murmurHash3Mixer(System.identityHashCode(key)) and Int.MAX_VALUE) % keys.size

    private fun indexOf(key: K, startIndex: Int): Int {
        for (i in startIndex until keys.size) {
            if (keys[i] == key) return i
            if (keys[i] == EMPTY_SLOT) return NOT_FOUND
        }
        for (i in 0 until startIndex) {
            if (keys[i] == key) return i
            if (keys[i] == EMPTY_SLOT) return NOT_FOUND
        }
        return NOT_FOUND
    }

    private fun findEmpty(startIndex: Int): Int {
        for (i in startIndex until keys.size) {
            if (keys[i] == EMPTY_SLOT) return i
        }
        for (i in 0 until startIndex) {
            if (keys[i] == EMPTY_SLOT) return i
        }
        throw RuntimeException("Overflowed :(")
    }

    override fun iterator(): Iterator<K> = Iterables.filterNotNull(NoSpread.iteratorsForArray(byId))

    fun clear() {
        keys.fill(null)
        byId.fill(null)
        nextId = 0
        size = 0
    }

    fun copy(): IntIdentityHashBiMap<K> = IntIdentityHashBiMap(keys.clone(), values.clone(), byId.clone(), nextId, size)

    companion object {

        const val NOT_FOUND: Int = -1
        private val EMPTY_SLOT: Any? = null
        private const val LOAD_FACTOR = 0.8F

        @JvmStatic
        fun <T> create(initialCapacity: Int): IntIdentityHashBiMap<T> = IntIdentityHashBiMap((initialCapacity.toFloat() / LOAD_FACTOR).toInt())

        @JvmStatic
        private fun <T> newArray(size: Int): Array<T?> = arrayOfNulls<Any>(size) as Array<T?>
    }
}

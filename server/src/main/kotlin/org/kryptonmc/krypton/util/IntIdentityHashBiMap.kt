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
    override var size: Int = 0
        private set

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

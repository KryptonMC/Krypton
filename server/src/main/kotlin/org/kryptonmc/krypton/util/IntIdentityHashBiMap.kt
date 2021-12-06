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

import kotlin.math.max

@Suppress("UNCHECKED_CAST") // Our casts are fine and should always succeed
class IntIdentityHashBiMap<K>(initialCapacity: Int) : IntBiMap<K> {

    private var keys: Array<K?>
    private var values: IntArray
    private var byId: Array<K?>

    private var nextId = 0
    override var size = 0
        private set

    init {
        val capacity = (initialCapacity.toFloat() / LOAD_FACTOR).toInt()
        keys = arrayOfNulls<Any>(capacity) as Array<K?>
        values = IntArray(capacity)
        byId = arrayOfNulls<Any>(capacity) as Array<K?>
    }

    fun add(key: K) = nextId().apply { set(key, this) }

    operator fun set(key: K, value: Int) {
        val max = max(value, size + 1)
        if (max >= keys.size * LOAD_FACTOR) {
            var newSize = keys.size shl 1
            while (newSize < value) newSize = newSize shl 1
            grow(newSize)
        }
        val firstEmpty = findEmpty(hash(key))
        keys[firstEmpty] = key
        values[firstEmpty] = value
        byId[value] = key
        size++
        if (value == nextId) nextId++
    }

    fun clear() {
        keys.fill(null)
        byId.fill(null)
        nextId = 0
        size = 0
    }

    override fun idOf(value: K) = getValue(indexOf(value, hash(value)))

    override fun get(id: Int) = if (id in 0..byId.size) byId[id] else null

    override fun iterator() = byId.iterator().asSequence().filterNotNull().iterator()

    private fun getValue(id: Int) = if (id == -1) -1 else values[id]

    private fun nextId(): Int {
        while (nextId < byId.size && byId[nextId] != null) nextId++
        return nextId
    }

    private fun grow(newSize: Int) {
        val oldKeys = keys
        val oldValues = values
        this.keys = arrayOfNulls<Any>(newSize) as Array<K?>
        this.values = IntArray(newSize)
        this.byId = arrayOfNulls<Any>(newSize) as Array<K?>
        nextId = 0
        size = 0
        for (i in oldKeys.indices) oldKeys[i]?.let { set(it, oldValues[i]) }
    }

    private fun hash(key: K) = (murmurHash3Mixer(System.identityHashCode(key)) and Int.MAX_VALUE) % keys.size

    private fun indexOf(key: K, value: Int): Int {
        for (i in value until keys.size) {
            if (keys[i] == key) return i
            if (keys[i] == EMPTY_SLOT) return NOT_FOUND
        }
        for (i in 0 until value) {
            if (keys[i] == key) return i
            if (keys[i] == EMPTY_SLOT) return NOT_FOUND
        }
        return NOT_FOUND
    }

    private fun findEmpty(value: Int): Int {
        for (i in value until keys.size) {
            if (keys[i] == EMPTY_SLOT) return i
        }
        for (i in 0 until value) {
            if (keys[i] == EMPTY_SLOT) return i
        }
        throw RuntimeException("Overflowed :(")
    }

    companion object {

        const val NOT_FOUND = -1
        private val EMPTY_SLOT = null
        private const val LOAD_FACTOR = 0.8F

        private fun murmurHash3Mixer(value: Int): Int {
            var temp = value xor value ushr 16
            temp *= -2048144789
            temp = temp xor temp ushr 13
            temp *= -1028477387
            return temp xor temp ushr 16
        }
    }
}

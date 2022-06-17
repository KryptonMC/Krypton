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

import java.util.function.Predicate
import kotlin.math.max
import kotlin.math.min

@Suppress("UNCHECKED_CAST", "EqualsOrHashCode")
class SortedArraySet<T>(private val comparator: Comparator<T>, initialCapacity: Int) : AbstractMutableSet<T>() {

    private var contents = arrayOfNulls<Any>(initialCapacity) as Array<T?>
    override var size: Int = 0

    val first: T?
        get() = contents[0]
    val last: T?
        get() = contents[size - 1]

    fun get(element: T): T? {
        val index = findIndex(element)
        return if (index >= 0) contents[index] else null
    }

    override fun add(element: T): Boolean {
        val index = findIndex(element)
        if (index >= 0) return false
        addInternal(element, -index - 1)
        return true
    }

    override fun remove(element: T): Boolean {
        val index = findIndex(element)
        if (index >= 0) {
            removeInternal(index)
            return true
        }
        return false
    }

    override fun contains(element: T): Boolean = findIndex(element) >= 0

    override fun iterator(): MutableIterator<T> = ArrayIterator()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (comparator == (other as SortedArraySet<Any>).comparator) return size == other.size && contents.contentEquals(other.contents)
        return super.equals(other)
    }

    override fun toArray(): Array<T?> = contents.clone()

    override fun <U> toArray(a: Array<U?>): Array<U?> {
        if (a.size < size) return contents.copyOf(size) as Array<U?>
        System.arraycopy(contents, 0, a, 0, size)
        if (a.size > size) a[size] = null
        return a
    }

    override fun clear() {
        contents.fill(null, 0, size)
        size = 0
    }

    override fun removeIf(filter: Predicate<in T>): Boolean {
        var i = 0
        val len = size
        val backing = contents
        while (true) {
            if (i >= len) return false
            if (!filter.test(backing[i]!!)) {
                ++i
                continue
            }
            break
        }
        var lastIndex = i
        while (i < len) {
            val curr = backing[i]!!
            if (!filter.test(curr)) backing[lastIndex++] = curr
            ++i
        }
        backing.fill(null, lastIndex, len)
        size = lastIndex
        return true
    }

    private fun findIndex(element: T): Int = contents.binarySearch(element, comparator as Comparator<T?>, 0, size)

    private fun addInternal(element: T, index: Int) {
        grow(size + 1)
        if (index != size) System.arraycopy(contents, index, contents, index + 1, size - index)
        contents[index] = element
        ++size
    }

    private fun removeInternal(index: Int) {
        --size
        if (index != size) System.arraycopy(contents, index + 1, contents, index, size - index)
        contents[size] = null
    }

    private fun grow(minCapacity: Int) {
        if (minCapacity <= contents.size) return
        val capacity = if (contents.isEmpty()) {
            max(min(contents.size.toLong() + (contents.size shr 1).toLong(), 2147483639L), minCapacity.toLong()).toInt()
        } else if (minCapacity < 10) {
            10
        } else {
            minCapacity
        }
        val newArray = arrayOfNulls<Any>(capacity)
        System.arraycopy(contents, 0, newArray, 0, size)
        contents = newArray as Array<T?>
    }

    private inner class ArrayIterator : MutableIterator<T> {

        private var index = 0
        private var last = -1

        override fun hasNext(): Boolean = index < size

        override fun next(): T {
            if (index >= size) throw NoSuchElementException()
            last = index++
            return contents[last]!!
        }

        override fun remove() {
            check(last != -1)
            removeInternal(last)
            --index
            last = -1
        }
    }

    companion object {

        @JvmStatic
        fun <T : Comparable<T>> create(initialCapacity: Int): SortedArraySet<T> = SortedArraySet(naturalOrder(), initialCapacity)
    }
}

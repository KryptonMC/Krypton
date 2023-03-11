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
package org.kryptonmc.krypton.util.collection

import java.util.Arrays
import java.util.function.Predicate
import kotlin.math.max
import kotlin.math.min

class SortedArraySet<T>(private val comparator: Comparator<T>, initialCapacity: Int) : AbstractMutableSet<T>() {

    private var contents = castArray<T>(arrayOfNulls(initialCapacity))
    override var size: Int = 0
        private set

    fun first(): T? = contents[0]

    fun last(): T? = contents[size - 1]

    fun get(element: T): T? {
        val index = findIndex(element)
        return if (index >= 0) contents[index] else null
    }

    override fun add(element: T): Boolean {
        val index = findIndex(element)
        if (index >= 0) return false
        addInternal(element, computeInsertPosition(index))
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
        if (other is SortedArraySet<*> && comparator == other.comparator) {
            // This is safe because the equals method below uses `equals`, which doesn't need to care about the type.
            // Java wouldn't need this cast, but Kotlin doesn't allow Nothing to be used as a type parameter, even if it is fine to do so.
            @Suppress("UNCHECKED_CAST")
            other as SortedArraySet<Any>
            return size == other.size && contents.contentEquals(other.contents)
        }
        return super.equals(other)
    }

    override fun hashCode(): Int = contents.contentHashCode()

    override fun toArray(): Array<T?> = contents.clone()

    override fun <U> toArray(a: Array<U?>): Array<U?> {
        if (a.size < size) return Arrays.copyOf(contents, size, a.javaClass)
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
            if (filter.test(backing[i]!!)) break
            ++i
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

    private fun findIndex(element: T): Int = Arrays.binarySearch(contents, 0, size, element, comparator)

    private fun addInternal(element: T, index: Int) {
        grow(size + 1)
        if (index != size) System.arraycopy(contents, index, contents, index + 1, size - index)
        contents[index] = element
        ++size
    }

    private fun removeInternal(index: Int) {
        --size
        if (index != size) System.arraycopy(contents, index + 1, contents, index, size - index)
        // We null out the existing element to avoid memory leaks, as otherwise, if we kept a reference to it, it would never be GC'd.
        contents[size] = null
    }

    private fun grow(minCapacity: Int) {
        if (minCapacity <= contents.size) return
        val result = arrayOfNulls<Any>(computeNewCapacity(minCapacity))
        System.arraycopy(contents, 0, result, 0, size)
        contents = castArray(result)
    }

    private fun computeNewCapacity(minCapacity: Int): Int = when {
        contents.isEmpty() -> max(min(contents.size.toLong() + (contents.size shr 1).toLong(), 2147483639L), minCapacity.toLong()).toInt()
        minCapacity < 10 -> 10
        else -> minCapacity
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

        @JvmStatic
        private fun computeInsertPosition(index: Int): Int = -index - 1

        @JvmStatic
        private fun <T> castArray(input: Array<Any?>): Array<T?> {
            @Suppress("UNCHECKED_CAST") // This is safe because this array will always be null
            return input as Array<T?>
        }
    }
}

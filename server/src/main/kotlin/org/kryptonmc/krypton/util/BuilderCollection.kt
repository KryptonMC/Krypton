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

import java.util.Spliterator
import java.util.function.Predicate
import java.util.stream.Stream

/**
 * A highly specialised collection built for use in builders that only need to
 * write and iterate.
 *
 * This is probably not really necessary, but I felt like writing a specialised
 * collection for this use case.
 */
class BuilderCollection<E> : MutableCollection<E> {

    private var elements: Array<E?>
    override var size: Int = 0
        private set

    constructor() {
        @Suppress("UNCHECKED_CAST")
        elements = emptyArray<Any>() as Array<E?>
    }

    constructor(from: Collection<E>) {
        @Suppress("UNCHECKED_CAST")
        elements = arrayOfNulls<Any>(from.size) as Array<E?>
        if (from is List<*>) {
            from as List<E>
            for (i in from.indices) {
                elements[i] = from.get(i)
            }
        } else {
            var i = 0
            from.forEach { elements[i++] = it }
        }
    }

    override fun contains(element: E): Boolean = unsupported()

    override fun containsAll(elements: Collection<E>): Boolean = unsupported()

    override fun isEmpty(): Boolean = size == 0

    override fun add(element: E): Boolean {
        if (size == elements.size) elements = elements.copyOf(size * 2)
        elements[size++] = element
        return true
    }

    override fun addAll(elements: Collection<E>): Boolean = unsupported()

    override fun remove(element: E): Boolean = unsupported()

    override fun removeAll(elements: Collection<E>): Boolean = unsupported()

    override fun removeIf(filter: Predicate<in E>): Boolean = unsupported()

    override fun retainAll(elements: Collection<E>): Boolean = unsupported()

    override fun clear() {
        unsupported()
    }

    override fun iterator(): MutableIterator<E> = Iterator()

    override fun spliterator(): Spliterator<E> = unsupported()

    override fun stream(): Stream<E> = unsupported()

    override fun parallelStream(): Stream<E> = unsupported()

    private fun unsupported(): Nothing {
        throw UnsupportedOperationException("This list can only be written to or iterated over!")
    }

    private inner class Iterator : MutableIterator<E> {

        private var cursor = 0

        override fun hasNext(): Boolean = cursor < size

        override fun next(): E {
            if (cursor >= size) throw NoSuchElementException()
            return elements[cursor++]!!
        }

        override fun remove() {
            unsupported()
        }
    }
}

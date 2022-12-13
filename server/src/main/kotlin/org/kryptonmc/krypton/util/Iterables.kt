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

import com.google.common.collect.AbstractIterator
import com.google.common.collect.Iterators
import com.google.common.collect.UnmodifiableIterator

object Iterables {

    @JvmStatic
    fun <T> findRelative(iterable: Iterable<T>, value: T?, reversed: Boolean): T? =
        if (reversed) findPrevious(iterable, value) else findNext(iterable, value)

    @JvmStatic
    fun <T> findPrevious(iterable: Iterable<T>, value: T?): T? {
        val iterator = iterable.iterator()
        var current: T? = null
        var next: T
        while (iterator.hasNext()) {
            next = iterator.next()
            if (next === value) {
                if (current == null) current = if (iterator.hasNext()) Iterators.getLast(iterator) else value
                break
            }
            current = next
        }
        return current
    }

    @JvmStatic
    fun <T> findNext(iterable: Iterable<T>, value: T?): T {
        val iterator = iterable.iterator()
        val first = iterator.next()
        if (value != null) {
            var next = first
            while (next !== value) {
                if (iterator.hasNext()) next = iterator.next()
            }
            if (iterator.hasNext()) return iterator.next()
        }
        return first
    }

    @JvmStatic
    fun <T> filterNotNull(iterator: Iterator<T?>): UnmodifiableIterator<T> = object : AbstractIterator<T>() {
        override fun computeNext(): T? {
            while (iterator.hasNext()) {
                iterator.next()?.let { return it }
            }
            return endOfData()
        }
    }
}

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

fun <T> Iterable<T>.findRelative(value: T?, reversed: Boolean): T? = if (reversed) findPrevious(value) else findNext(value)

fun <T> Iterable<T>.findPrevious(value: T?): T? {
    val iterator = iterator()
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

fun <T> Iterable<T>.findNext(value: T?): T {
    val iterator = iterator()
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

fun <T> Iterator<T?>.filterNotNull(): UnmodifiableIterator<T> = object : AbstractIterator<T>() {
    override fun computeNext(): T? {
        while (this@filterNotNull.hasNext()) {
            val element = this@filterNotNull.next()
            if (element != null) return element
        }
        return endOfData()
    }
}

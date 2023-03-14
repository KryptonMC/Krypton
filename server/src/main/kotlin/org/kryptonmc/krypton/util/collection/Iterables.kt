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

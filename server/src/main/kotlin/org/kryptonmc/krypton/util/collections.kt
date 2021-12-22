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

import kotlinx.collections.immutable.PersistentCollection
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import java.util.function.ToIntFunction

fun <T> List<T>.toIntArray(converter: ToIntFunction<T>): IntArray {
    val array = IntArray(size)
    for (i in indices) {
        array[i] = converter.applyAsInt(get(i))
    }
    return array
}

fun <T, R> Iterable<T>.mapPersistentList(action: (T) -> R): PersistentList<R> = mapPersistentTo(persistentListOf<R>().builder(), action)

fun <T, R> Iterable<T>.mapPersistentSet(action: (T) -> R): PersistentSet<R> = mapPersistentTo(persistentSetOf<R>().builder(), action)

@Suppress("UNCHECKED_CAST")
fun <T, R, B : PersistentCollection.Builder<R>, C : PersistentCollection<R>> Iterable<T>.mapPersistentTo(builder: B, action: (T) -> R): C {
    for (element in this) {
        builder.add(action(element))
    }
    return builder.build() as C
}

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

fun <T> Iterable<T>.convertToList(): List<T> {
    if (this is List<T>) {
        if (isEmpty()) return emptyList()
        return this
    }
    if (this is Collection<T> && isEmpty()) return emptyList()
    return toList()
}

fun <T> Iterable<T>.convertToSet(): Set<T> {
    if (this is Set<T>) {
        if (isEmpty()) return emptySet()
        return this
    }
    if (this is Collection<T> && isEmpty()) return emptySet()
    return toSet()
}

fun <T> List<T>.minus(index: Int): List<T> = minus(get(index))

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

import java.util.concurrent.CompletableFuture

fun <V> List<CompletableFuture<V>>.sequence(): CompletableFuture<List<V>> = asSequence().fold(CompletableFuture.completedFuture(mutableListOf())) { acc, future ->
    future.thenCombine(acc) { value, list ->
        ArrayList<V>(list.size + 1).apply {
            addAll(list)
            add(value)
        }
    }
}

fun <V> List<CompletableFuture<V>>.sequenceFailFast(): CompletableFuture<List<V>> {
    val list = ArrayList<V?>(size)
    val futures = arrayOfNulls<CompletableFuture<*>>(size)
    val voidFuture = CompletableFuture<Void>()
    forEach {
        val listSize = list.size
        list.add(null)
        futures[listSize] = it.whenComplete { value, exception ->
            if (exception != null) voidFuture.completeExceptionally(exception) else list[listSize] = value
        }
    }
    return CompletableFuture.allOf(*futures).applyToEither(voidFuture) { list.filterNotNull() }
}

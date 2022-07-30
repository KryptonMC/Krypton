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

import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.function.Function

object Futures {

    @JvmStatic
    fun <V> sequenceFailFast(futures: List<CompletableFuture<out V>>): CompletableFuture<List<V>> {
        val result = CompletableFuture<List<V>>()
        return fallibleSequence(futures, result::completeExceptionally).applyToEither(result, Function.identity())
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    private fun <V> fallibleSequence(futures: List<CompletableFuture<out V>>, consumer: Consumer<Throwable>): CompletableFuture<List<V>> {
        val result = ArrayList<V?>(futures.size)
        val resultFutures = arrayOfNulls<CompletableFuture<*>>(futures.size)
        futures.forEach {
            val size = result.size
            result.add(null)
            resultFutures[size] = it.whenComplete { value, exception ->
                if (exception != null) consumer.accept(exception) else result[size] = value
            }
        }
        // God damn you Kotlin. Why can't you just be like Java here and pass arrays as-is to vararg parameters instead of copying the array?
        return CompletableFuture.allOf(*resultFutures).thenApply { result as ArrayList<V> }
    }
}

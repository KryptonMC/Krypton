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
package org.kryptonmc.krypton.util

import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.function.Function

object Futures {

    @JvmStatic
    fun <V> sequenceFailFast(futures: List<CompletableFuture<out V>>): CompletableFuture<List<V>> {
        val result = CompletableFuture<List<V>>()
        return fallibleSequence(futures) { result.completeExceptionally(it) }.applyToEither(result, Function.identity())
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
                if (exception != null) consumer.accept(exception) else result.set(size, value)
            }
        }
        return NoSpread.completableFutureAllOf(resultFutures).thenApply { result as ArrayList<V> }
    }
}

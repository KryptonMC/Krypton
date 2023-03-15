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

import org.jctools.queues.MpmcUnboundedXaddArrayQueue
import org.kryptonmc.krypton.network.buffer.BinaryBuffer
import org.kryptonmc.krypton.network.socket.NetworkServer
import java.lang.ref.SoftReference
import java.nio.ByteBuffer
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

class ObjectPool<T>(private val supplier: Supplier<T>, private val sanitizer: UnaryOperator<T>) {

    private val pool = MpmcUnboundedXaddArrayQueue<SoftReference<T>>(QUEUE_SIZE)

    fun get(): T {
        var result: T?
        var ref: SoftReference<T>? = pool.poll()
        while (ref != null) {
            result = ref.get()
            if (result != null) return result
            ref = pool.poll()
        }
        return supplier.get()
    }

    fun add(element: T) {
        val sanitized = sanitizer.apply(element)
        pool.offer(SoftReference(sanitized))
    }

    fun clear() {
        pool.clear()
    }

    fun count(): Int = pool.size

    fun hold(): Holder = Holder(get())

    fun <R> use(function: Function<T, R>): R {
        val element = get()
        try {
            return function.apply(element)
        } finally {
            add(element)
        }
    }

    inner class Holder(private val element: T) : AutoCloseable {

        private var closed = false

        fun get(): T {
            if (closed) error("Holder is closed")
            return element
        }

        override fun close() {
            if (!closed) {
                closed = true
                add(element)
            }
        }
    }

    companion object {

        private const val QUEUE_SIZE = 32_768
        private const val BUFFER_SIZE = 262_143

        @JvmField
        val BUFFER_POOL: ObjectPool<BinaryBuffer> = ObjectPool({ BinaryBuffer.sized(BUFFER_SIZE) }, { it.clear() })
        @JvmField
        val PACKET_POOL: ObjectPool<ByteBuffer> = ObjectPool({ ByteBuffer.allocateDirect(NetworkServer.MAX_PACKET_SIZE) }, { it.clear() })
    }
}

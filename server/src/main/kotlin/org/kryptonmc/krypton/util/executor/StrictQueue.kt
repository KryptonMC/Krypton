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
package org.kryptonmc.krypton.util.executor

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger

interface StrictQueue<T, F> {

    fun pop(): F?

    fun push(value: T): Boolean

    fun isEmpty(): Boolean

    fun size(): Int

    class FixedPriority(size: Int) : StrictQueue<IntRunnable, Runnable> {

        private val queues = Array<Queue<Runnable>>(size) { ConcurrentLinkedQueue() }
        private val size = AtomicInteger()

        override fun pop(): Runnable? {
            queues.forEach {
                val task = it.poll()
                if (task != null) {
                    size.decrementAndGet()
                    return task
                }
            }
            return null
        }

        override fun push(value: IntRunnable): Boolean {
            val priority = value.priority
            if (priority < queues.size && priority >= 0) {
                queues[priority].add(value)
                size.incrementAndGet()
                return true
            }
            throw IndexOutOfBoundsException("Priority $priority not supported! Expected range [0-${queues.size - 1}]!")
        }

        override fun isEmpty(): Boolean = size.get() == 0

        override fun size(): Int = size.get()
    }

    class IntRunnable(val priority: Int, private val task: Runnable) : Runnable {

        override fun run() {
            task.run()
        }
    }

    class Wrapper<T>(private val queue: Queue<T>) : StrictQueue<T, T> {

        override fun pop(): T? = queue.poll()

        override fun push(value: T): Boolean = queue.add(value)

        override fun isEmpty(): Boolean = queue.isEmpty()

        override fun size(): Int = queue.size
    }
}

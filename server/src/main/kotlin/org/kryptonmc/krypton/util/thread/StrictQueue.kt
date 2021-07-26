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
package org.kryptonmc.krypton.util.thread

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

sealed interface StrictQueue<T, F> {

    val size: Int

    fun push(task: T): Boolean

    fun pop(): F?

    fun isEmpty(): Boolean
}

class FixedPriorityQueue(queueCount: Int) : StrictQueue<PriorityTask, Runnable> {

    private val queues: List<Queue<Runnable>> = (0..queueCount).map { ConcurrentLinkedQueue() }
    override val size: Int
        get() = queues.sumOf { it.size }

    override fun push(task: PriorityTask): Boolean {
        queues[task.priority].add(task)
        return true
    }

    override fun pop(): Runnable? {
        queues.forEach { queue -> queue.poll()?.let { return it } }
        return null
    }

    override fun isEmpty() = queues.all { it.isEmpty() }
}

class StandardStrictQueue<T>(private val delegate: Queue<T>) : StrictQueue<T, T> {

    override val size: Int
        get() = delegate.size

    override fun push(task: T) = delegate.add(task)

    override fun pop(): T? = delegate.poll()

    override fun isEmpty() = delegate.isEmpty()
}

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

import org.kryptonmc.krypton.util.logger
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executor
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.atomic.AtomicInteger

class StandardScheduler<T>(
    override val name: String,
    private val executor: Executor,
    private val queue: StrictQueue<T, Runnable>
) : Scheduler<T>, Runnable {

    private val status = AtomicInteger(0)

    val size: Int
        get() = queue.size

    fun runAll() {
        try {
            pollUntil { true }
        } finally {
            setIdle()
            registerForExecution()
        }
    }

    override fun submit(task: T) {
        queue.push(task)
        registerForExecution()
    }

    override fun run() {
        try {
            pollUntil { it == 0 }
        } finally {
            setIdle()
            registerForExecution()
        }
    }

    override fun close() {
        do {
            val current = status.get()
        } while (!status.compareAndSet(current, current or CLOSED))
    }

    override fun toString() = "$name ${status.get()} ${queue.isEmpty()}"

    private fun canBeScheduled() = if (status.get() and CLOSED != 0) false else !queue.isEmpty()

    private fun scheduled() = status.get() and SCHEDULED != 0

    private fun setScheduled(): Boolean {
        do {
            val current = status.get()
            if (current and 3 != 0) return false
        } while (!status.compareAndSet(current, current or SCHEDULED))
        return true
    }

    private fun setIdle() {
        do {
            val current = status.get()
        } while (!status.compareAndSet(current, current and -3))
    }

    private fun pollUntil(predicate: (Int) -> Boolean): Int {
        var current = 0
        while (predicate(current) && poll()) {
            ++current
        }
        return current
    }

    private fun poll(): Boolean {
        if (!scheduled()) return false
        val task = queue.pop() ?: return false
        task.run()
        return true
    }

    private fun registerForExecution() {
        if (!canBeScheduled() || !setScheduled()) return
        try {
            executor.execute(this)
        } catch (exception: RejectedExecutionException) {
            // That's okay, let's give it another shot
            try {
                executor.execute(this)
            } catch (exception: RejectedExecutionException) {
                // Rejected again? damn.
                LOGGER.error("Failed to schedule tasks!", exception)
            }
        }
    }

    companion object {

        private val LOGGER = logger<StandardScheduler<*>>()
        private const val CLOSED = 1
        private const val SCHEDULED = 2

        fun create(name: String, executor: Executor) = StandardScheduler(name, executor, StandardStrictQueue(ConcurrentLinkedQueue()))
    }
}

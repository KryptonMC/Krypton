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

import it.unimi.dsi.fastutil.ints.Int2BooleanFunction
import org.apache.logging.log4j.LogManager
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executor
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.atomic.AtomicInteger

class ProcessorMailbox<T>(
    private val queue: StrictQueue<in T, out Runnable>,
    private val dispatcher: Executor,
    override val name: String
) : ProcessorHandle<T>, AutoCloseable, Runnable {

    private val status = AtomicInteger(0)

    private fun setAsScheduled(): Boolean {
        do {
            val value = status.get()
            if (value and SCHEDULED_BIT.inv() != 0) return false
        } while (!status.compareAndSet(value, value or SCHEDULED_BIT))
        return true
    }

    private fun setAsIdle() {
        do {
            val value = status.get()
        } while (!status.compareAndSet(value, value and -SCHEDULED_BIT.inv()))
    }

    private fun canBeScheduled(): Boolean {
        if (status.get() and CLOSED_BIT != 0) return false
        return !queue.isEmpty()
    }

    override fun close() {
        do {
            val value = status.get()
        } while (!status.compareAndSet(value, value or CLOSED_BIT))
    }

    private fun shouldProcess(): Boolean = status.get() and SCHEDULED_BIT != 0

    private fun pollTask(): Boolean {
        if (!shouldProcess()) return false
        val task = queue.pop() ?: return false
        task.run()
        return true
    }

    override fun run() {
        try {
            pollUntil { it == 0 }
        } finally {
            setAsIdle()
            registerForExecution()
        }
    }

    fun runAll() {
        try {
            pollUntil { true }
        } finally {
            setAsIdle()
            registerForExecution()
        }
    }

    override fun tell(message: T) {
        queue.push(message)
        registerForExecution()
    }

    private fun registerForExecution() {
        if (canBeScheduled() && setAsScheduled()) {
            try {
                dispatcher.execute(this)
            } catch (exception: RejectedExecutionException) {
                try {
                    dispatcher.execute(this)
                } catch (exception: RejectedExecutionException) {
                    LOGGER.error("Could not schedule mailbox $name!", exception)
                }
            }
        }
    }

    private fun pollUntil(function: Int2BooleanFunction): Int {
        var i = 0
        while (function.get(i) && pollTask()) {
            i++
        }
        return i
    }

    fun size(): Int = queue.size()

    fun hasWork(): Boolean = shouldProcess() && !queue.isEmpty()

    override fun toString(): String = "$name ${status.get()} ${queue.isEmpty()}"

    companion object {

        private val LOGGER = LogManager.getLogger()
        private const val CLOSED_BIT = 1
        private const val SCHEDULED_BIT = 2

        @JvmStatic
        fun create(dispatcher: Executor, name: String): ProcessorMailbox<Runnable> =
            ProcessorMailbox(StrictQueue.Wrapper(ConcurrentLinkedQueue()), dispatcher, name)
    }
}

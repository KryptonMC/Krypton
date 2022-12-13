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

import org.apache.logging.log4j.LogManager
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executor
import java.util.concurrent.locks.LockSupport
import java.util.function.BooleanSupplier
import java.util.function.Supplier

abstract class BlockableEventLoop<R : Runnable> protected constructor(override val name: String) : ProcessorHandle<R>, Executor {

    private val pendingTasks = ConcurrentLinkedQueue<R>()
    private var blockingCount = 0

    protected abstract fun wrapRunnable(runnable: Runnable): R

    protected abstract fun shouldRun(task: R): Boolean

    fun isSameThread(): Boolean = Thread.currentThread() === runningThread()

    protected abstract fun runningThread(): Thread

    protected open fun scheduleExecutables(): Boolean = !isSameThread()

    fun pendingTaskCount(): Int = pendingTasks.size

    fun <V> submit(supplier: Supplier<V>): CompletableFuture<V> {
        if (scheduleExecutables()) return CompletableFuture.supplyAsync(supplier, this)
        return CompletableFuture.completedFuture(supplier.get())
    }

    private fun submitAsync(task: Runnable): CompletableFuture<Void> = CompletableFuture.supplyAsync({
        task.run()
        null
    }, this)

    fun submit(task: Runnable): CompletableFuture<Void> {
        if (scheduleExecutables()) return submitAsync(task)
        task.run()
        return CompletableFuture.completedFuture(null)
    }

    fun executeBlocking(task: Runnable) {
        if (!isSameThread()) {
            submitAsync(task).join()
        } else {
            task.run()
        }
    }

    override fun tell(message: R) {
        pendingTasks.add(message)
        LockSupport.unpark(runningThread())
    }

    override fun execute(command: Runnable) {
        if (scheduleExecutables()) tell(wrapRunnable(command)) else command.run()
    }

    open fun executeIfPossible(task: Runnable) {
        execute(task)
    }

    protected fun dropAllTasks() {
        pendingTasks.clear()
    }

    protected fun runAllTasks() {
        while (pollTask()) {
            // Do nothing, pollTask does what we want to do
        }
    }

    open fun pollTask(): Boolean {
        val task = pendingTasks.peek() ?: return false
        if (blockingCount == 0 && !shouldRun(task)) return false
        doRunTask(pendingTasks.remove())
        return true
    }

    fun managedBlock(supplier: BooleanSupplier) {
        ++blockingCount
        try {
            while (!supplier.asBoolean) {
                if (!pollTask()) waitForTasks()
            }
        } finally {
            --blockingCount
        }
    }

    protected fun waitForTasks() {
        Thread.yield()
        LockSupport.parkNanos("waiting for tasks", 100000L)
    }

    protected open fun doRunTask(task: R) {
        try {
            task.run()
        } catch (exception: Exception) {
            LOGGER.error("Error executing task on $name!", exception)
        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
    }
}

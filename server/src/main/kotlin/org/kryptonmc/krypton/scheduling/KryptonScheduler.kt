/*
 * This file is part of the Krypton project, and is derived from the Velocity project,
 * licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2018 Velocity Contributors
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
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/proxy/src/main/java/com/velocitypowered/proxy/scheduler/VelocityScheduler.java
 */
package org.kryptonmc.krypton.scheduling

import com.google.common.collect.Multimaps
import org.kryptonmc.api.scheduling.Scheduler
import org.kryptonmc.api.scheduling.Task
import org.kryptonmc.api.scheduling.TaskRunnable
import org.kryptonmc.api.scheduling.TaskState
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.threadFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class KryptonScheduler(private val pluginManager: KryptonPluginManager) : Scheduler {

    private val executor: ExecutorService = Executors.newCachedThreadPool(threadFactory("Krypton Scheduler #%d"))
    private val timedExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
        threadFactory("Krypton Timed Scheduler")
    )
    private val tasksByPlugin = Multimaps.newMultimap(ConcurrentHashMap<Any, MutableCollection<KryptonTask>>()) {
        ConcurrentHashMap.newKeySet()
    }

    override fun run(plugin: Any, task: TaskRunnable) = schedule(plugin, 0, TimeUnit.MILLISECONDS, task)

    override fun schedule(plugin: Any, delay: Long, unit: TimeUnit, task: TaskRunnable) = schedule(plugin, delay, 0, unit, task)

    override fun schedule(plugin: Any, delay: Long, period: Long, unit: TimeUnit, task: TaskRunnable): Task {
        val scheduledTask = KryptonTask(plugin, task, delay, period, unit)
        tasksByPlugin.put(plugin, scheduledTask)
        scheduledTask.schedule()
        return scheduledTask
    }

    fun shutdown(): Boolean {
        synchronized(tasksByPlugin) { tasksByPlugin.values() }.forEach { it.cancel() }
        timedExecutor.shutdown()
        executor.shutdown()
        return executor.awaitTermination(10, TimeUnit.SECONDS)
    }

    private inner class KryptonTask(
        override val plugin: Any,
        val callable: TaskRunnable,
        val delay: Long,
        val period: Long,
        val unit: TimeUnit
    ) : Runnable, Task {

        private var future: ScheduledFuture<*>? = null
        private var currentTaskThread: Thread? = null

        override val state: TaskState
            get() {
                if (future == null) return TaskState.SCHEDULED
                if (future!!.isCancelled) return TaskState.INTERRUPTED
                if (future!!.isDone) return TaskState.COMPLETED
                return TaskState.SCHEDULED
            }

        fun schedule() {
            future = if (period == 0L) {
                timedExecutor.schedule(this, delay, unit)
            } else {
                timedExecutor.scheduleAtFixedRate(this, delay, period, unit)
            }
        }

        override fun cancel() {
            future?.cancel(false)
            currentTaskThread?.interrupt()
            finish()
        }

        override fun run() = executor.execute {
            currentTaskThread = Thread.currentThread()
            try {
                callable.run(this)
            } catch (exception: Exception) {
                if (exception is InterruptedException) {
                    Thread.currentThread().interrupt()
                    return@execute
                }
                val name = pluginManager.fromInstance(plugin)?.description?.name ?: "UNKNOWN"
                LOGGER.error("Plugin $name generated an exception whilst trying to execute task $callable!", exception)
            } finally {
                if (period == 0L) finish()
                currentTaskThread = null
            }
        }

        private fun finish() = tasksByPlugin[plugin]?.minusAssign(this)
    }

    companion object {

        private val LOGGER = logger("Scheduler")
    }
}

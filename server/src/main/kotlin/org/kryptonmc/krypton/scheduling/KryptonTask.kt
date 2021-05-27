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
package org.kryptonmc.krypton.scheduling

import org.kryptonmc.api.plugin.Plugin
import org.kryptonmc.api.scheduling.Task
import org.kryptonmc.api.scheduling.TaskState
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.util.logger
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

data class KryptonTask(
    val scheduler: KryptonScheduler,
    override val plugin: Plugin,
    val runnable: Runnable,
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

    internal fun schedule() {
        future = if (period == 0L) {
            scheduler.timedExecutor.schedule(this, delay, unit)
        } else {
            scheduler.timedExecutor.scheduleAtFixedRate(this, delay, period, unit)
        }
    }

    override fun cancel() {
        future?.cancel(false)
        currentTaskThread?.interrupt()
        finish()
    }

    override fun run() = scheduler.executor.execute {
        currentTaskThread = Thread.currentThread()
        try {
            runnable.run()
        } catch (exception: Exception) {
            if (exception is InterruptedException) {
                Thread.currentThread().interrupt()
                return@execute
            }
            Messages.SCHEDULE_ERROR.error(LOGGER, plugin.context.description.name, runnable)
        } finally {
            if (period == 0L) finish()
            currentTaskThread = null
        }
    }

    private fun finish() = scheduler.tasksByPlugin[plugin]?.minusAssign(this)

    companion object {

        private val LOGGER = logger("Scheduler")
    }
}

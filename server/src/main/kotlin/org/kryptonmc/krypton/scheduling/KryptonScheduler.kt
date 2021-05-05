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

import org.kryptonmc.krypton.api.plugin.Plugin
import org.kryptonmc.krypton.api.scheduling.Scheduler
import org.kryptonmc.krypton.api.scheduling.Task
import org.kryptonmc.krypton.util.concurrent.NamedThreadFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

object KryptonScheduler : Scheduler {

    override val executor: ExecutorService = Executors.newCachedThreadPool(NamedThreadFactory("Krypton Scheduler #%d"))
    internal val timedExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(NamedThreadFactory("Krypton Timed Scheduler"))
    internal val tasksByPlugin = ConcurrentHashMap<Plugin, MutableSet<Task>>()

    override fun run(plugin: Plugin, task: () -> Unit) = schedule(plugin, 0, TimeUnit.MILLISECONDS, task)

    override fun schedule(plugin: Plugin, delay: Long, unit: TimeUnit, task: () -> Unit) = schedule(plugin, delay, 0, unit, task)

    override fun schedule(plugin: Plugin, delay: Long, period: Long, unit: TimeUnit, task: () -> Unit): Task {
        val scheduledTask = KryptonTask(this, plugin, task, delay, period, unit)
        tasksByPlugin.getOrPut(plugin) { ConcurrentHashMap.newKeySet() } += scheduledTask
        scheduledTask.schedule()
        return scheduledTask
    }

    internal fun shutdown(): Boolean {
        synchronized(tasksByPlugin) { tasksByPlugin.values.toList() }.forEach { tasks ->
            tasks.forEach { it.cancel() }
        }
        timedExecutor.shutdown()
        executor.shutdown()
        return executor.awaitTermination(10, TimeUnit.SECONDS)
    }
}

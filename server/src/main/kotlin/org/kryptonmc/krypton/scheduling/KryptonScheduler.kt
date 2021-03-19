package org.kryptonmc.krypton.scheduling

import org.kryptonmc.krypton.api.plugin.Plugin
import org.kryptonmc.krypton.api.scheduling.Scheduler
import org.kryptonmc.krypton.api.scheduling.Task
import org.kryptonmc.krypton.concurrent.NamedThreadFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

object KryptonScheduler : Scheduler {

    override val executor: ExecutorService = Executors.newCachedThreadPool(NamedThreadFactory("Krypton Scheduler #%d"))
    internal val timedExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(NamedThreadFactory("Krypton Timed Scheduler"))
    internal val tasksByPlugin = mutableMapOf<Plugin, MutableSet<Task>>()

    override fun run(plugin: Plugin, task: () -> Unit) = schedule(plugin, 0, TimeUnit.MILLISECONDS, task)

    override fun schedule(plugin: Plugin, delay: Long, unit: TimeUnit, task: () -> Unit) = schedule(plugin, delay, 0, unit, task)

    override fun schedule(plugin: Plugin, delay: Long, period: Long, unit: TimeUnit, task: () -> Unit): Task {
        val scheduledTask = KryptonTask(this, plugin, task, delay, period, unit)
        tasksByPlugin.getOrPut(plugin) { mutableSetOf() } += scheduledTask
        scheduledTask.schedule()
        return scheduledTask
    }

    internal fun shutdown(): Boolean {
        tasksByPlugin.values.forEach { tasks -> tasks.forEach { it.cancel() } }
        timedExecutor.shutdown()
        executor.shutdown()
        return executor.awaitTermination(10, TimeUnit.SECONDS)
    }
}
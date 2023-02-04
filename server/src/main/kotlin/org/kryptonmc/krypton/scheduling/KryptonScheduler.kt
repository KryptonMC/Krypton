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
package org.kryptonmc.krypton.scheduling

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap
import org.jctools.queues.MpscUnboundedArrayQueue
import org.kryptonmc.api.scheduling.ExecutionType
import org.kryptonmc.api.scheduling.Scheduler
import org.kryptonmc.api.scheduling.Task
import org.kryptonmc.api.scheduling.TaskAction
import org.kryptonmc.api.scheduling.TaskTime
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.IntFunction
import java.util.function.Supplier

class KryptonScheduler : Scheduler {

    private val taskQueue = MpscUnboundedArrayQueue<KryptonTask>(64)
    // Tasks scheduled on a certain tick
    private val tickTaskQueue = Int2ObjectAVLTreeMap<MutableList<KryptonTask>>()

    private var currentTick = 0

    fun process() {
        synchronized(this) {
            currentTick++
            var tickToProcess: Int
            while (!tickTaskQueue.isEmpty()) {
                tickToProcess = tickTaskQueue.firstIntKey()
                if (tickToProcess > currentTick) break
                tickTaskQueue.remove(tickToProcess)?.forEach { taskQueue.relaxedOffer(it) }
            }
        }
        if (taskQueue.isEmpty) return
        taskQueue.drain { task ->
            if (!task.isAlive()) return@drain
            when (task.executionType) {
                ExecutionType.SYNCHRONOUS -> handleTask(task)
                ExecutionType.ASYNCHRONOUS -> ASYNC_EXECUTOR.submit { handleTask(task) }
            }
        }
    }

    override fun submitTask(task: Supplier<TaskAction>, executionType: ExecutionType): Task {
        val schedulerTask = KryptonTask(TASK_COUNTER.getAndIncrement(), task, executionType, this)
        handleTask(schedulerTask)
        return schedulerTask
    }

    override fun submitTask(task: Supplier<TaskAction>): Task = submitTask(task, DEFAULT_EXECUTION_TYPE)

    override fun buildTask(task: Runnable): Task.Builder = KryptonTask.Builder(this, task)

    fun resumeTask(task: KryptonTask) {
        if (task.tryResume()) taskQueue.relaxedOffer(task)
    }

    private fun safeExecute(task: KryptonTask) {
        when (task.executionType) {
            ExecutionType.SYNCHRONOUS -> taskQueue.offer(task)
            ExecutionType.ASYNCHRONOUS -> ASYNC_EXECUTOR.submit {
                if (!task.isAlive()) return@submit
                handleTask(task)
            }
        }
    }

    private fun handleTask(task: KryptonTask) {
        when (val action = task.task.get()) {
            is KryptonTaskAction.ScheduleAfter -> rescheduleTask(task, action.time)
            is KryptonTaskAction.ScheduleWhenComplete -> action.future.thenRun { safeExecute(task) }
            is KryptonTaskAction.Pause -> task.pause()
            is KryptonTaskAction.Cancel -> task.cancel()
        }
    }

    private fun rescheduleTask(task: KryptonTask, time: TaskTime) {
        when (time) {
            is KryptonTaskTime.Zero -> taskQueue.relaxedOffer(task)
            is KryptonTaskTime.DurationTime -> {
                val duration = time.duration
                SCHEDULER.schedule({ safeExecute(task) }, duration.toMillis(), TimeUnit.MILLISECONDS)
            }
            is KryptonTaskTime.Ticks -> {
                synchronized(this) {
                    val target = currentTick + time.ticks
                    tickTaskQueue.computeIfAbsent(target, IntFunction { ArrayList() }).add(task)
                }
            }
        }
    }

    companion object {

        @JvmField
        val DEFAULT_EXECUTION_TYPE: ExecutionType = ExecutionType.SYNCHRONOUS

        private val TASK_COUNTER = AtomicInteger()
        private val SCHEDULER = Executors.newScheduledThreadPool(1) { task -> Thread(task).apply { isDaemon = true } }
        private val ASYNC_EXECUTOR = ForkJoinPool.commonPool()
    }
}

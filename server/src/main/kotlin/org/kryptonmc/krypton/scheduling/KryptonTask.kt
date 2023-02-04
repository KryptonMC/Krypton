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

import it.unimi.dsi.fastutil.HashCommon
import org.kryptonmc.api.scheduling.ExecutionType
import org.kryptonmc.api.scheduling.Scheduler
import org.kryptonmc.api.scheduling.Task
import org.kryptonmc.api.scheduling.TaskAction
import org.kryptonmc.api.scheduling.TaskTime
import java.lang.invoke.MethodHandles
import java.util.function.Supplier

class KryptonTask(
    private val id: Int,
    val task: Supplier<TaskAction>,
    override val executionType: ExecutionType,
    private val owner: KryptonScheduler
) : Task {

    @Volatile
    private var paused = false
    @Volatile
    private var alive = true

    override fun isPaused(): Boolean = paused

    fun pause() {
        paused = true
    }

    override fun resume() {
        owner.resumeTask(this)
    }

    fun tryResume(): Boolean = PAUSED.compareAndSet(this, true, false)

    override fun isAlive(): Boolean = alive

    override fun cancel() {
        alive = false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return id == (other as KryptonTask).id
    }

    override fun hashCode(): Int = HashCommon.murmurHash3(id)

    override fun toString(): String = "KryptonTask(id=$id, task=$task, executionType=$executionType, owner=$owner)"

    class Builder(private val scheduler: Scheduler, private val task: Runnable) : Task.Builder {

        private var executionType = KryptonScheduler.DEFAULT_EXECUTION_TYPE
        private var delayAction: TaskAction = DEFAULT_DELAY_ACTION
        private var periodAction: TaskAction = DEFAULT_PERIOD_ACTION

        override fun executionType(type: ExecutionType): Task.Builder = apply { executionType = type }

        override fun delay(time: TaskTime): Task.Builder = apply { delayAction = KryptonTaskAction.ScheduleAfter(time) }

        override fun period(time: TaskTime): Task.Builder = apply { periodAction = KryptonTaskAction.ScheduleAfter(time) }

        override fun schedule(): Task {
            return scheduler.submitTask(object : Supplier<TaskAction> {
                var first = true

                override fun get(): TaskAction {
                    if (first) {
                        first = false
                        return delayAction
                    }
                    task.run()
                    return periodAction
                }
            }, executionType)
        }

        companion object {

            private val DEFAULT_DELAY_ACTION = KryptonTaskAction.ScheduleAfter(KryptonTaskTime.Zero)
            private val DEFAULT_PERIOD_ACTION = KryptonTaskAction.Cancel
        }
    }

    companion object {

        @JvmStatic
        private val PAUSED = MethodHandles.lookup().findVarHandle(KryptonTask::class.java, "paused", Boolean::class.javaPrimitiveType)
    }
}

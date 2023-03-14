/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

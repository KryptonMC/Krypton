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

import org.kryptonmc.api.scheduling.TaskAction
import org.kryptonmc.api.scheduling.TaskTime
import java.util.concurrent.CompletableFuture

sealed interface KryptonTaskAction : TaskAction {

    @JvmRecord
    data class ScheduleAfter(val time: TaskTime) : KryptonTaskAction

    @JvmRecord
    data class ScheduleWhenComplete(val future: CompletableFuture<*>) : KryptonTaskAction

    object Pause : KryptonTaskAction

    object Cancel : KryptonTaskAction

    object Factory : TaskAction.Factory {

        override fun scheduleAfter(time: TaskTime): TaskAction = ScheduleAfter(time)

        override fun scheduleWhenComplete(future: CompletableFuture<*>): TaskAction = ScheduleWhenComplete(future)

        override fun pause(): TaskAction = Pause

        override fun cancel(): TaskAction = Cancel
    }
}

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

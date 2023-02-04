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

import org.kryptonmc.api.scheduling.TaskTime
import java.time.Duration

sealed interface KryptonTaskTime : TaskTime {

    object Zero : KryptonTaskTime

    // Would be named "Duration", but that would annoyingly conflict with java.time.Duration.
    @JvmRecord
    data class DurationTime(val duration: Duration) : KryptonTaskTime

    @JvmRecord
    data class Ticks(val ticks: Int) : KryptonTaskTime

    object Factory : TaskTime.Factory {

        override fun zero(): TaskTime = Zero

        override fun duration(duration: Duration): TaskTime = DurationTime(duration)

        override fun ticks(ticks: Int): TaskTime = Ticks(ticks)
    }
}

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

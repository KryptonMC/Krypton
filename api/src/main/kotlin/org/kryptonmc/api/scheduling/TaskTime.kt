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
package org.kryptonmc.api.scheduling

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.TypeFactory
import java.time.Duration
import java.time.temporal.TemporalUnit

/**
 * An amount of time for a task. This is used for delay and repeat times.
 */
public interface TaskTime {

    @TypeFactory
    @ApiStatus.Internal
    public interface Factory {

        public fun zero(): TaskTime

        public fun duration(duration: Duration): TaskTime

        public fun ticks(ticks: Int): TaskTime
    }

    public companion object {

        /**
         * A task time that represents no time, for immediate execution.
         *
         * This will indicate to the scheduler that the action should happen
         * immediately, or as soon as possible.
         *
         * @return a task time representing no time
         */
        @JvmStatic
        public fun zero(): TaskTime = Krypton.factory<Factory>().zero()

        /**
         * A task time that represents the given [duration] amount of time.
         *
         * Executions will not depend on the server's tick speed, and will be
         * executed with a period of the given duration.
         *
         * The scheduler is not required to be as precise as a standard Java
         * scheduler would be (for example, ScheduledExecutorService), and if
         * such precision is required, you are advised to use your own such
         * scheduler.
         *
         * @param duration the duration
         * @return a task time representing the duration
         */
        @JvmStatic
        public fun duration(duration: Duration): TaskTime = Krypton.factory<Factory>().duration(duration)

        /**
         * A task time that represents the given amount of [ticks].
         *
         * Executions will depend on the server's tick speed, and will be
         * executed with a period of the given amount of ticks.
         *
         * Warning: Do not assume that all implementations use the same tick
         * speed, or even that a single implementation will use a constant
         * tick speed. If you need a constant, precise time, you are advised
         * to use your own scheduler.
         *
         * @param ticks the amount of ticks
         * @return a task time representing the amount of ticks
         */
        @JvmStatic
        public fun ticks(ticks: Int): TaskTime = Krypton.factory<Factory>().ticks(ticks)

        /**
         * A task time that represents the given [amount] of time in the
         * given [unit].
         *
         * This is a shortcut for [duration], therefore the same restrictions that
         * apply to that method apply to this one.
         *
         * @param amount the amount of time
         * @param unit the unit of time
         * @return a task time representing the amount in the unit
         */
        @JvmStatic
        public fun duration(amount: Long, unit: TemporalUnit): TaskTime = duration(Duration.of(amount, unit))

        /**
         * A task time that represents the given number of [hours].
         *
         * This is a shortcut for [duration], therefore the same restrictions that
         * apply to that method apply to this one.
         *
         * @param hours the number of hours
         * @return a task time representing the number of hours
         */
        @JvmStatic
        public fun hours(hours: Long): TaskTime = duration(Duration.ofHours(hours))

        /**
         * A task time that represents the given number of [minutes].
         *
         * This is a shortcut for [duration], therefore the same restrictions that
         * apply to that method apply to this one.
         *
         * @param minutes the number of minutes
         * @return a task time representing the number of minutes
         */
        @JvmStatic
        public fun minutes(minutes: Long): TaskTime = duration(Duration.ofMinutes(minutes))

        /**
         * A task time that represents the given number of [seconds].
         *
         * This is a shortcut for [duration], therefore the same restrictions that
         * apply to that method apply to this one.
         *
         * @param seconds the number of seconds
         * @return a task time representing the number of seconds
         */
        @JvmStatic
        public fun seconds(seconds: Long): TaskTime = duration(Duration.ofSeconds(seconds))

        /**
         * A task time that represents the given number of [millis].
         *
         * This is a shortcut for [duration], therefore the same restrictions that
         * apply to that method apply to this one.
         *
         * @param millis the number of milliseconds
         * @return a task time representing the number of milliseconds
         */
        @JvmStatic
        public fun millis(millis: Long): TaskTime = duration(Duration.ofMillis(millis))
    }
}

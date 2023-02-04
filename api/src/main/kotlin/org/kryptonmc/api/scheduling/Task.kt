/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scheduling

import java.time.Duration
import java.time.temporal.TemporalUnit

/**
 * A task that can be scheduled by the scheduler.
 *
 * The task has three states:
 * * Alive: The task is available for scheduling.
 * * Paused: The task is not available for scheduling, but can be made
 * available with [resume].
 * * Cancelled: The task is not available for scheduling, and cannot be made
 * available.
 */
public interface Task {

    /**
     * Indicates how this task will be executed by the scheduler.
     */
    public val executionType: ExecutionType

    /**
     * Checks if this task is alive, meaning it is not cancelled, and is
     * available for scheduling or currently being scheduled.
     *
     * @return whether this task is alive
     */
    public fun isAlive(): Boolean

    /**
     * Checks if this task is currently paused, meaning it will not be
     * executed by the scheduler.
     *
     * @return whether this task is paused
     */
    public fun isPaused(): Boolean

    /**
     * Resumes this task if it was paused, meaning it can be executed by the
     * scheduler again.
     */
    public fun resume()

    /**
     * Cancels this task, with immediate effect.
     *
     * If the scheduler is currently executing the task, it will not be
     * rescheduled. It may finish its execution, however this behaviour is
     * implementation-defined.
     */
    public fun cancel()

    /**
     * A builder for building a task.
     *
     * This builder is designed to provide a more familiar way of scheduling
     * tasks, using a more conventional delay and interval (period) system.
     * It is designed to be simple and easy to use, at the cost of flexibility.
     *
     * If you require greater flexibility over how the scheduler will execute
     * your task, you should use [Scheduler.submitTask].
     */
    public interface Builder {

        /**
         * Sets the execution type for the task.
         *
         * If this is not called, the implementation will choose which type is
         * best.
         *
         * @param type the execution type
         * @return this builder
         */
        public fun executionType(type: ExecutionType): Builder

        /**
         * Sets the initial execution delay for the task.
         *
         * This defines how long after the task's initial scheduling it will
         * first be ran.
         *
         * If this is not set, the task will be executed immediately.
         *
         * @param time the amount of time for the initial delay
         * @return this builder
         */
        public fun delay(time: TaskTime): Builder

        /**
         * Sets the initial execution delay for the task.
         *
         * @param duration the duration for the initial delay
         * @return this builder
         * @see delay
         */
        public fun delay(duration: Duration): Builder = delay(TaskTime.duration(duration))

        /**
         * Sets the initial execution delay for the task.
         *
         * @param amount the amount of time for the initial delay
         * @param unit the unit of time for the initial delay
         * @return this builder
         * @see delay
         */
        public fun delay(amount: Long, unit: TemporalUnit): Builder = delay(TaskTime.duration(amount, unit))

        /**
         * Sets the delay between subsequent executions of the task.
         *
         * This defines how long after each execution the task will be ran
         * again.
         *
         * If this is not set, the task will only run once.
         *
         * @param time the amount of time for the delay between executions
         * @return this builder
         */
        public fun period(time: TaskTime): Builder

        /**
         * Sets the delay between subsequent executions of the task.
         *
         * @param duration the duration for the delay between executions
         * @return this builder
         * @see period
         */
        public fun period(duration: Duration): Builder = period(TaskTime.duration(duration))

        /**
         * Sets the delay between subsequent executions of the task.
         *
         * @param amount the amount of time for the delay between executions
         * @param unit the unit of time for the delay between executions
         * @return this builder
         * @see period
         */
        public fun period(amount: Long, unit: TemporalUnit): Builder = period(TaskTime.duration(amount, unit))

        /**
         * Requests for the scheduler to schedule this task for execution.
         *
         * There is no defined period within which the scheduler must execute
         * the task. It may be executed immediately, it may be executed on the
         * next tick, or if the scheduler is especially busy, it may be
         * executed an arbitrary amount of time after it has been scheduled.
         *
         * @return the scheduled task
         */
        public fun schedule(): Task
    }
}

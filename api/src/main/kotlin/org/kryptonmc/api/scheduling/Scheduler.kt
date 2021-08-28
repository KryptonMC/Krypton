/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scheduling

import java.util.concurrent.TimeUnit

/**
 * The scheduler. This can be used to run or schedule tasks (a)synchronously
 */
interface Scheduler {

    /**
     * Runs the given [task] asynchronously once with no delay.
     *
     * @param plugin the plugin that requested to run the task
     * @param task the task to be ran
     * @return a scheduled [Task] that can be [cancelled][Task.cancel]
     */
    fun run(plugin: Any, task: TaskRunnable): Task

    /**
     * Schedules the given [task] asynchronously once with the given [delay]
     * in the given [unit] of time.
     *
     * @param plugin the plugin that requested to run the task
     * @param delay the delay before this task is ran
     * @param unit the time unit for the [delay]
     * @param task the task to be ran
     * @return a scheduled [Task] that can be [cancelled][Task.cancel]
     */
    fun schedule(plugin: Any, delay: Long, unit: TimeUnit, task: TaskRunnable): Task

    /**
     * Schedules the given [task] asynchronously repeatedly, with a [period] in between each
     * execution of the task, and starting after the given [delay], both in the given [unit]
     * of time.
     *
     * @param plugin the plugin requesting to schedule the task
     * @param delay the delay before this task is ran
     * @param period the period of time between running this task
     * @param unit the unit of time for the [delay] and [period]
     * @param task the task to be ran
     * @return a scheduled [Task] that can be [cancelled][Task.cancel]
     */
    fun schedule(plugin: Any, delay: Long, period: Long, unit: TimeUnit, task: TaskRunnable): Task
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scheduling

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.TypeFactory
import java.util.concurrent.CompletableFuture

/**
 * The action to take after a task has been executed.
 *
 * This allows for tasks to be rescheduled, parked, or stopped.
 */
public interface TaskAction {

    @TypeFactory
    @ApiStatus.Internal
    public interface Factory {

        public fun scheduleAfter(time: TaskTime): TaskAction

        public fun scheduleWhenComplete(future: CompletableFuture<*>): TaskAction

        public fun pause(): TaskAction

        public fun cancel(): TaskAction
    }

    public companion object {

        /**
         * An action that indicates the scheduler should schedule the task
         * for execution in the given amount of [time].
         *
         * This can be used for both initial schedules and reschedules.
         *
         * @param time the time to schedule the task for
         * @return a schedule action
         */
        @JvmStatic
        public fun scheduleAfter(time: TaskTime): TaskAction = Krypton.factory<Factory>().scheduleAfter(time)

        /**
         * An action that indicates the scheduler should schedule the task
         * when the given [future] is complete.
         *
         * This is useful for tasks that have to wait on resources or actions
         * to be completed before they can be safely executed.
         *
         * @param future the future to schedule a task when complete
         * @return a schedule after action
         */
        @JvmStatic
        public fun scheduleWhenComplete(future: CompletableFuture<*>): TaskAction = Krypton.factory<Factory>().scheduleWhenComplete(future)

        /**
         * An action that indicates the scheduler should pause the task,
         * meaning it will not execute until it is explicitly resumed
         * with [Task.resume].
         *
         * This is useful for tasks that need to be stopped and started
         * arbitrarily, and avoids the need to cancel the task and
         * reschedule it.
         *
         * @return a park action
         */
        @JvmStatic
        public fun pause(): TaskAction = Krypton.factory<Factory>().pause()

        /**
         * An action that indicates the scheduler should cancel the task,
         * meaning it can never be executed again.
         *
         * Once a task has been cancelled, it cannot be restarted under any
         * circumstances. If you wish to pause a task and resume it later,
         * use [pause] instead.
         *
         * @return an action that stops a task from executing
         */
        @JvmStatic
        public fun cancel(): TaskAction = Krypton.factory<Factory>().cancel()
    }
}

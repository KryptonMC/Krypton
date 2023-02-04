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

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.kryptonmc.api.scheduling.ExecutionType
import org.kryptonmc.api.scheduling.TaskAction
import org.kryptonmc.api.scheduling.TaskTime
import org.kryptonmc.krypton.testutil.Bootstrapping
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Supplier
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class SchedulerTest {

    @Test
    fun `ensure tick task executes after one tick is processed`() {
        val scheduler = KryptonScheduler()
        val result = AtomicBoolean(false)
        val task = scheduler.buildTask { result.set(true) }.delay(TaskTime.ticks(1)).schedule()

        assertFalse(result.get(), "Tick task should not be executed before scheduling begins")
        scheduler.process()
        assertTrue(result.get(), "Tick task should be executed after one tick is processed")
        assertFalse(task.isAlive(), "Tick task should be cancelled after execution")
    }

    @Test
    fun `ensure duration task executes after around specified time has passed`() {
        val scheduler = KryptonScheduler()
        val result = AtomicBoolean(false)
        scheduler.buildTask { result.set(true) }.delay(TaskTime.seconds(1)).schedule()

        Thread.sleep(100)
        scheduler.process()
        assertFalse(result.get(), "Duration task executed 900 milliseconds too early")

        Thread.sleep(1200)
        scheduler.process()
        assertTrue(result.get(), "Duration task with a delay of 1 second was not executed after 1.3 seconds")
    }

    @Test
    fun `ensure zero task executes as soon as possible`() {
        val scheduler = KryptonScheduler()
        val result = AtomicBoolean(false)
        // As specified in documentation, without a provided delay, the task defaults to instant execution.
        scheduler.buildTask { result.set(true) }.schedule()

        assertFalse(result.get(), "Immediate task should not be executed before scheduling begins")
        scheduler.process()
        assertTrue(result.get(), "Immediate task should be executed immediately after scheduling begins")

        result.set(false)
        scheduler.process()
        assertFalse(result.get(), "Immediate task should not be executed again")
    }

    @Test
    fun `ensure cancelling task does not reschedule it`() {
        val scheduler = KryptonScheduler()
        val result = AtomicBoolean(false)
        val task = scheduler.buildTask { result.set(true) }.schedule()

        assertTrue(task.isAlive(), "Task should be alive before cancelling")
        task.cancel()
        assertFalse(task.isAlive(), "Task should not be alive after being cancelled")
        scheduler.process()
        assertFalse(result.get(), "Task should not be executed after being cancelled")
    }

    @Test
    fun `ensure cancelling async delayed task does not reschedule it`() {
        val scheduler = KryptonScheduler()
        val result = AtomicBoolean(false)
        val task = scheduler.buildTask { result.set(true) }
            .delay(TaskTime.millis(1))
            .executionType(ExecutionType.ASYNCHRONOUS)
            .schedule()

        assertTrue(task.isAlive(), "Async task should be alive before cancelling")
        task.cancel()
        assertFalse(task.isAlive(), "Async task should not be alive after being cancelled")
        scheduler.process()

        // 10 milliseconds should be plenty of time such that a 1 millisecond delay task would be executed if it was scheduled.
        Thread.sleep(10L)
        assertFalse(result.get(), "Async task should not be executed after being cancelled")
    }

    @Test
    fun `ensure task only executes when not paused`() {
        val scheduler = KryptonScheduler()
        // Ignored paused task. Ensures that pausing and resuming one task does not affect other tasks.
        scheduler.submitTask(object : Supplier<TaskAction> {
            var first = true

            override fun get(): TaskAction {
                if (first) {
                    first = false
                    return TaskAction.pause()
                }
                fail("This paused task should never be executed")
            }
        }, ExecutionType.SYNCHRONOUS)

        val result = AtomicBoolean(false)
        val task = scheduler.submitTask(object : Supplier<TaskAction> {
            var first = true

            override fun get(): TaskAction {
                if (first) {
                    first = false
                    return TaskAction.pause()
                }
                result.set(true)
                return TaskAction.cancel()
            }
        })

        assertTrue(task.isPaused(), "Initially paused task should be paused")
        assertFalse(result.get(), "Initially paused task should not be executed while paused")
        task.resume()
        assertFalse(task.isPaused(), "Initially paused task should not be paused after resuming")
        assertFalse(result.get(), "Initially paused task should not be executed before scheduling begins")
        scheduler.process()
        assertFalse(task.isPaused(), "Initially paused task should not be paused after execution")
        assertTrue(result.get(), "Initially paused task should be executed after scheduling begins")
    }

    @Test
    fun `ensure task scheduled when future complete runs after future`() {
        val scheduler = KryptonScheduler()
        val future = CompletableFuture<Unit>()
        val result = AtomicBoolean(false)
        scheduler.submitTask(object : Supplier<TaskAction> {
            var first = true

            override fun get(): TaskAction {
                if (first) {
                    first = false
                    return TaskAction.scheduleWhenComplete(future)
                }
                result.set(true)
                return TaskAction.cancel()
            }
        })

        assertFalse(result.get(), "Future task should not be executed before future is complete")
        future.complete(null)
        assertFalse(result.get(), "Future task should not be executed before scheduling begins")
        scheduler.process()
        assertTrue(result.get(), "Future task should be executed after scheduling begins")
    }

    @Test
    fun `ensure async task always executes on different thread`() {
        val currentThread = Thread.currentThread()
        val scheduler = KryptonScheduler()
        val result = AtomicBoolean(false)
        scheduler.buildTask {
            assertNotEquals(currentThread, Thread.currentThread(), "Async task should not be executed on the same thread")
            result.set(true)
        }.executionType(ExecutionType.ASYNCHRONOUS).schedule()

        assertFalse(result.get(), "Async task should only be executed after scheduling begins")
        scheduler.process()

        // 250 milliseconds should be a reasonable amount of time to account for any delays in thread pool execution.
        Thread.sleep(250)
        assertTrue(result.get(), "Async task was not executed after 250 milliseconds")
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun `initialize factories for time and action`() {
            Bootstrapping.loadFactories()
        }
    }
}

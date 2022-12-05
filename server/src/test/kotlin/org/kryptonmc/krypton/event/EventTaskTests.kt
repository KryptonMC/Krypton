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
package org.kryptonmc.krypton.event

import org.junit.jupiter.api.Test
import org.kryptonmc.api.event.Continuation
import org.kryptonmc.api.event.EventTask
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater
import java.util.function.Consumer
import kotlin.test.assertTrue

class EventTaskTests {

    @Test
    fun `test resume when complete normal`() {
        val continuation = WitnessContinuation()
        EventTask.resumeWhenComplete(CompletableFuture.completedFuture(null)).execute(continuation)
        assertTrue(continuation.completedSuccessfully(), "Completed future did not complete successfully!")
    }

    @Test
    fun `test resume when complete exception`() {
        val continuation = WitnessContinuation()
        EventTask.resumeWhenComplete(CompletableFuture.failedFuture<Void>(Throwable())).execute(continuation)
        assertTrue(continuation.completedWithError(), "Failed future completed successfully!")
    }

    @Test
    fun `test resume when complete from other thread`() {
        val latch = CountDownLatch(1)
        val continuation = WitnessContinuation { latch.countDown() }
        EventTask.resumeWhenComplete(CompletableFuture.supplyAsync<Void> { null }).execute(continuation)
        latch.await()
        assertTrue(continuation.completedSuccessfully(), "Completed future did not complete successfully!")
    }

    @Test
    fun `test resume when failed from other thread`() {
        val latch = CountDownLatch(1)
        val continuation = WitnessContinuation { latch.countDown() }
        EventTask.resumeWhenComplete(CompletableFuture.supplyAsync<Void> { throw RuntimeException() }).execute(continuation)
        latch.await()
        assertTrue(continuation.completedWithError(), "Failed future completed successfully!")
    }

    @Test
    fun `test resume when complex chain failed from other thread`() {
        val latch = CountDownLatch(1)
        val continuation = WitnessContinuation { latch.countDown() }
        val async = CompletableFuture.supplyAsync<Void> { null }
            .thenAccept { throw RuntimeException() }
            .thenCompose { CompletableFuture.completedFuture(null) }
        EventTask.resumeWhenComplete(async).execute(continuation)
        latch.await()
        assertTrue(continuation.completedWithError(), "Failed future completed successfully!")
    }

    private class WitnessContinuation(private val onComplete: Consumer<Throwable?>? = null) : Continuation {

        @Volatile
        @Suppress("UnusedPrivateMember")
        private var status = INCOMPLETE

        fun completedSuccessfully(): Boolean = STATUS_UPDATER.get(this) == COMPLETE

        fun completedWithError(): Boolean = STATUS_UPDATER.get(this) == COMPLETE_WITH_EXCEPTION

        override fun resume() {
            check(STATUS_UPDATER.compareAndSet(this, INCOMPLETE, COMPLETE)) { "Continuation is already complete!" }
            onComplete?.accept(null)
        }

        override fun resumeWithException(exception: Throwable) {
            check(STATUS_UPDATER.compareAndSet(this, INCOMPLETE, COMPLETE_WITH_EXCEPTION)) { "Continuation is already complete!" }
            onComplete?.accept(exception)
        }

        companion object {

            private const val INCOMPLETE = 0
            private const val COMPLETE = 1
            private const val COMPLETE_WITH_EXCEPTION = 2

            private val STATUS_UPDATER = AtomicIntegerFieldUpdater.newUpdater(WitnessContinuation::class.java, "status")
        }
    }
}

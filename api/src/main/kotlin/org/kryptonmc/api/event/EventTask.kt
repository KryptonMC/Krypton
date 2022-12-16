/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/PaperMC/Velocity/blob/1761755d4dfc16cd020aee90c48761d98552531b/api/src/main/java/com/velocitypowered/api/event/EventTask.java
 */
package org.kryptonmc.api.event

import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

/**
 * A task that can be returned by an event handler that allows event handling
 * to be suspended and resumed at a later time, and executing event handlers
 * completely or partially asynchronously.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface EventTask {

    /**
     * Gets whether this task must be called asynchronously.
     *
     * If this is true, this task is guaranteed to be executed asynchronously
     * from the current thread. Otherwise, the task may be executed on the
     * current thread or asynchronously.
     *
     * @return whether this task must be called asynchronously.
     */
    public fun mustBeAsync(): Boolean

    /**
     * Runs this task with the given [continuation]. The continuation must be
     * notified when the task is complete, either with [Continuation.resume] in
     * the case of a successful execution, or
     * [Continuation.resumeWithException] in the case of a failed execution.
     *
     * It is not a requirement to notify the continuation during the execution
     * of this function, this can happen at a later point in time and from
     * another thread, but the continuation must **always** be notified when
     * the execution of the task is complete.
     *
     * @param continuation the continuation
     */
    public fun execute(continuation: Continuation)

    public companion object {

        /**
         * Creates a new event task that is guaranteed to execute the given
         * [task] asynchronously.
         *
         * @param task the task to execute
         * @return a new asynchronous event task
         */
        @JvmStatic
        public fun async(task: Runnable): EventTask = object : EventTask {
            override fun mustBeAsync(): Boolean = true

            override fun execute(continuation: Continuation) {
                task.run()
                continuation.resume()
            }
        }

        /**
         * Creates a new event task based on a continuation that executes the
         * given [task] synchronously or asynchronously.
         *
         * @param task the task to execute
         * @return a new event task
         */
        @JvmStatic
        public fun withContinuation(task: Consumer<Continuation>): EventTask = object : EventTask {
            override fun mustBeAsync(): Boolean = false

            override fun execute(continuation: Continuation) {
                task.accept(continuation)
            }
        }

        /**
         * Creates a new event task based on a continuation that resumes the
         * continuation when the given [future] is complete.
         *
         * @param future the future
         * @return a new event task
         */
        @JvmStatic
        public fun resumeWhenComplete(future: CompletableFuture<*>): EventTask = withContinuation {
            future.whenComplete { _, exception -> if (exception != null) it.resumeWithException(exception) else it.resume() }
        }
    }
}

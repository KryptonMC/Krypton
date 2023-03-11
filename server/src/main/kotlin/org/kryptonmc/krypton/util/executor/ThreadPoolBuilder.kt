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
package org.kryptonmc.krypton.util.executor

import java.time.Duration
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
sealed class ThreadPoolBuilder<B : ThreadPoolBuilder<B, E>, E : ExecutorService> {

    protected var corePoolSize = -1
    protected var maximumPoolSize = -1
    protected var keepAliveTime = 0L
    protected var unit = TimeUnit.MILLISECONDS
    protected var workQueue: BlockingQueue<Runnable>? = null
    protected var threadFactory: ThreadFactory? = null
    private var rejectedExecutionHandler: RejectedExecutionHandler? = null

    fun coreSize(size: Int): B = apply { corePoolSize = size } as B

    fun keepAlive(time: Long, unit: TimeUnit): B = apply {
        keepAliveTime = time
        this.unit = unit
    } as B

    fun keepAlive(duration: Duration): B = apply {
        keepAliveTime = duration.toNanos()
        unit = TimeUnit.NANOSECONDS
    } as B

    fun workQueue(queue: BlockingQueue<Runnable>): B = apply { workQueue = queue } as B

    fun factory(factory: ThreadFactory): B = apply { threadFactory = factory } as B

    fun rejectedExecutionHandler(handler: RejectedExecutionHandler): B = apply { rejectedExecutionHandler = handler } as B

    fun build(): E = build(threadFactory ?: Executors.defaultThreadFactory(), rejectedExecutionHandler)

    protected abstract fun build(factory: ThreadFactory, handler: RejectedExecutionHandler?): E

    class Standard internal constructor() : ThreadPoolBuilder<Standard, ExecutorService>() {

        fun maximumSize(size: Int): Standard = apply { maximumPoolSize = size }

        fun fixed(size: Int): Standard = apply {
            corePoolSize = size
            maximumPoolSize = size
            keepAliveTime = 0
            unit = TimeUnit.MILLISECONDS
        }

        fun single(): Standard = fixed(1)

        fun cached(): Standard = apply {
            corePoolSize = 0
            maximumPoolSize = Int.MAX_VALUE
            keepAliveTime = DEFAULT_CACHED_KEEP_ALIVE_TIME
            unit = TimeUnit.SECONDS
        }

        override fun build(factory: ThreadFactory, handler: RejectedExecutionHandler?): ExecutorService {
            val queue = workQueue ?: SynchronousQueue()
            if (handler != null) return ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, queue, factory, handler)
            return ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, queue, factory)
        }
    }

    class Scheduled internal constructor(size: Int) : ThreadPoolBuilder<Scheduled, ScheduledExecutorService>() {

        init {
            corePoolSize = size
        }

        override fun build(factory: ThreadFactory, handler: RejectedExecutionHandler?): ScheduledExecutorService {
            if (handler != null) return ScheduledThreadPoolExecutor(corePoolSize, factory, handler)
            return ScheduledThreadPoolExecutor(corePoolSize, factory)
        }
    }

    companion object {

        private const val DEFAULT_CACHED_KEEP_ALIVE_TIME = 60L

        @JvmStatic
        fun create(): Standard = Standard()

        @JvmStatic
        fun fixed(size: Int): Standard = Standard().fixed(size)

        @JvmStatic
        fun single(): Standard = Standard().single()

        @JvmStatic
        fun cached(): Standard = Standard().cached()

        @JvmStatic
        fun scheduled(size: Int): Scheduled = Scheduled(size)
    }
}

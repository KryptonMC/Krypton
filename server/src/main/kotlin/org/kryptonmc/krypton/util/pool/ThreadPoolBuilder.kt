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
package org.kryptonmc.krypton.util.pool

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
    protected var rejectedExecutionHandler: RejectedExecutionHandler? = null

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
            keepAliveTime = 60
            unit = TimeUnit.SECONDS
        }

        override fun build(factory: ThreadFactory, handler: RejectedExecutionHandler?): ExecutorService {
            if (handler != null) return ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue ?: SynchronousQueue(), factory, handler)
            return ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue ?: SynchronousQueue(), factory)
        }
    }

    class Scheduled internal constructor(size: Int) : ThreadPoolBuilder<Scheduled, ScheduledExecutorService>() {

        init {
            corePoolSize = size
        }

        override fun build(factory: ThreadFactory, handler: RejectedExecutionHandler?): ScheduledExecutorService {
            if (handler != null) return ScheduledThreadPoolExecutor(corePoolSize, threadFactory, handler)
            return ScheduledThreadPoolExecutor(corePoolSize, threadFactory)
        }
    }

    companion object {

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

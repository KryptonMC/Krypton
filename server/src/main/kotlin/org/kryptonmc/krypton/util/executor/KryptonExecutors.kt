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
package org.kryptonmc.krypton.util.executor

import com.google.common.util.concurrent.MoreExecutors
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.util.math.Maths
import java.util.concurrent.CompletionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinWorkerThread
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

object KryptonExecutors {

    private val LOGGER = LogManager.getLogger()
    private val WORKER_COUNT = AtomicInteger(1)
    private val BACKGROUND_EXECUTOR = createExecutor("Background Executor")

    @JvmStatic
    fun background(): ExecutorService = BACKGROUND_EXECUTOR

    @JvmStatic
    fun shutdownAll() {
        shutdown(BACKGROUND_EXECUTOR)
    }

    @JvmStatic
    private fun shutdown(executor: ExecutorService) {
        executor.shutdown()
        val wasShutdown = try {
            executor.awaitTermination(3L, TimeUnit.SECONDS)
        } catch (_: InterruptedException) {
            false
        }
        if (!wasShutdown) executor.shutdownNow()
    }

    @JvmStatic
    private fun createExecutor(name: String): ExecutorService {
        val processors = Maths.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, getMaxThreads())
        if (processors <= 0) return MoreExecutors.newDirectExecutorService()
        return ForkJoinPool(processors, {
            val workerThread = object : ForkJoinWorkerThread(it) {
                override fun onTermination(exception: Throwable?) {
                    if (exception != null) {
                        LOGGER.warn("${getName()} died.", exception)
                    } else {
                        LOGGER.debug("${getName()} shut down.")
                    }
                    super.onTermination(exception)
                }
            }
            workerThread.name = "Worker-$name-${WORKER_COUNT.getAndIncrement()}"
            workerThread
        }, KryptonExecutors::onThreadException, true)
    }

    @JvmStatic
    private fun getMaxThreads(): Int {
        val maxThreads = System.getProperty("krypton.backgroundThreads")
        if (maxThreads != null) {
            try {
                val value = maxThreads.toInt()
                if (value >= 1 && value <= 255) return value
                LOGGER.error("Wrong krypton.backgroundThreads property value $maxThreads! Should be an integer value between 1 and 255!")
            } catch (exception: NumberFormatException) {
                LOGGER.error("Failed to parse krypton.backgroundThreads property value $maxThreads! Should be an integer value between 1 and 255!")
            }
        }
        return 255
    }

    @JvmStatic
    private fun onThreadException(thread: Thread, exception: Throwable) {
        var temp = exception
        if (temp is CompletionException) temp = temp.cause!!
        LOGGER.error("Caught exception in thread $thread!", temp)
    }
}

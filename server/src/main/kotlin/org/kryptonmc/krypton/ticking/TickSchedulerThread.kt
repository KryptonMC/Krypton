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
package org.kryptonmc.krypton.ticking

import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.event.server.KryptonTickEndEvent
import org.kryptonmc.krypton.event.server.KryptonTickStartEvent
import java.util.concurrent.locks.LockSupport

class TickSchedulerThread(private val server: KryptonServer) : Thread("Krypton Tick Scheduler") {

    @Volatile
    private var tickCount = 0

    override fun run() {
        while (server.isRunning()) {
            val startTime = System.nanoTime()
            val startTimeMillis = System.currentTimeMillis()
            server.eventNode.fire(KryptonTickStartEvent(tickCount))

            try {
                server.tick(startTimeMillis)
            } catch (exception: Exception) {
                LOGGER.error("Exception thrown during main server tick!", exception)
            }

            val endTime = System.nanoTime()
            val tickDuration = endTime - startTime
            server.eventNode.fire(KryptonTickEndEvent(tickCount, tickDuration, endTime))
            tickCount++

            val waitTime = NANOS_PER_TICK - tickDuration
            LockSupport.parkNanos(waitTime)
        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger()

        private const val MILLIS_PER_SECOND = 1000L
        private const val NANOS_PER_MILLI = 1000L * 1000L
        private const val NANOS_PER_SECOND = MILLIS_PER_SECOND * NANOS_PER_MILLI

        private val TICKS_PER_SECOND = Integer.getInteger("krypton.tps", 20)
        @JvmField
        val NANOS_PER_TICK: Long = NANOS_PER_SECOND / TICKS_PER_SECOND
        @JvmField
        val MILLIS_PER_TICK: Long = MILLIS_PER_SECOND / TICKS_PER_SECOND
    }
}

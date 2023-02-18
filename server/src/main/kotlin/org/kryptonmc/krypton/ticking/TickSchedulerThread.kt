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

        private const val MILLIS_PER_SECOND = 1000
        private const val NANOS_PER_MILLI = 1000L * 1000L
        private const val NANOS_PER_SECOND = MILLIS_PER_SECOND * NANOS_PER_MILLI

        private val TICKS_PER_SECOND = Integer.getInteger("krypton.tps", 20)
        private val NANOS_PER_TICK = NANOS_PER_SECOND / TICKS_PER_SECOND
    }
}

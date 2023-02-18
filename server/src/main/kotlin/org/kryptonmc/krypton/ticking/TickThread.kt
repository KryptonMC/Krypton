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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.locks.LockSupport

class TickThread(number: Int) : Thread("Krypton Ticker $number") {

    @Volatile
    private var stopped = false

    private var latch: CountDownLatch? = null
    private var tickTime = 0L
    private val entries = ArrayList<TickDispatcher.DispatchContext>()

    override fun run() {
        LockSupport.park(this)
        while (!stopped) {
            try {
                tick()
            } catch (exception: Exception) {
                LOGGER.error("Error while ticking!", exception)
            }
            latch!!.countDown()
            LockSupport.park(this)
        }
    }

    private fun tick() {
        val time = tickTime
        for (entry in entries) {
            assert(entry.thread === this)
            val elements = entry.elements
            if (elements.isEmpty()) continue
            for (element in elements) {
                try {
                    element.tick(time)
                } catch (exception: Throwable) {
                    LOGGER.error("Error while ticking $element!", exception)
                }
            }
        }
    }

    fun entries(): MutableList<TickDispatcher.DispatchContext> = entries

    fun startTick(latch: CountDownLatch, tickTime: Long) {
        if (entries.isEmpty()) {
            // Nothing to tick
            latch.countDown()
            return
        }

        this.latch = latch
        this.tickTime = tickTime
        stopped = false
        LockSupport.unpark(this)
    }

    fun shutdown() {
        stopped = true
        LockSupport.unpark(this)
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
    }
}

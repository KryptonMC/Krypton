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

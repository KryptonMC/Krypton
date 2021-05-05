/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.util.thread

import org.kryptonmc.krypton.util.concurrent.DefaultUncaughtExceptionHandler
import org.kryptonmc.krypton.util.logger
import java.util.concurrent.atomic.AtomicInteger

/**
 * A generic named thread with a unique ID
 */
abstract class GenericThread(protected val name: String) : Runnable {

    @Volatile var isRunning = false; protected set
    protected var thread: Thread? = null

    @Synchronized
    open fun start(): Boolean {
        if (isRunning) return true
        isRunning = true
        thread = Thread(this, "$name #${UNIQUE_THREAD_ID.incrementAndGet()}").apply {
            uncaughtExceptionHandler = DefaultUncaughtExceptionHandler(LOGGER)
        }
        thread!!.start()
        LOGGER.info("Thread $name started")
        return true
    }

    @Synchronized
    open fun stop() {
        isRunning = false
        if (thread == null) return

        var waitSeconds = 0
        while (thread!!.isAlive) {
            thread!!.join(1000L)
            if (waitSeconds++ >= 5) {
                LOGGER.warn("Waited $waitSeconds seconds, attempting to force stop.")
                continue
            }
            if (!thread!!.isAlive) continue
            LOGGER.warn("Thread $this (${thread!!.state}) failed to exit after $waitSeconds second(s)")
            thread!!.interrupt()
        }
        LOGGER.info("Thread $name stopped.")
        thread = null
    }

    companion object {

        private val LOGGER = logger<GenericThread>()
        private val UNIQUE_THREAD_ID = AtomicInteger(0)
    }
}

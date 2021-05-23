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
package org.kryptonmc.krypton

import org.apache.logging.log4j.Logger
import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.util.concurrent.NamedUncaughtExceptionHandler
import org.kryptonmc.krypton.util.logger
import java.lang.management.ManagementFactory
import java.lang.management.ThreadInfo
import kotlin.math.min

/**
 * The server watchdog. It's watching you...
 *
 * Nah, all this class does is make sure that the server doesn't freeze forever, and shuts it down if it does.
 */
class WatchdogProcess(private val server: KryptonServer) : Thread("Krypton Watchdog") {

    init {
        uncaughtExceptionHandler = NamedUncaughtExceptionHandler(LOGGER)
        isDaemon = true
    }

    private val timeoutTime = server.config.watchdog.timeoutTime * 1000L
    private val earlyWarningInterval = min(server.config.watchdog.earlyWarningInterval, timeoutTime)
    private val earlyWarningDelay = min(server.config.watchdog.earlyWarningDelay, timeoutTime)

    private var lastEarlyWarning = 0L
    @Volatile private var lastTick = 0L
    @Volatile private var stopping = false
    @Volatile private var hasStarted = false

    fun tick(time: Long) {
        if (lastTick == 0L) hasStarted = true
        lastTick = time
    }

    fun shutdown() {
        stopping = true
    }

    override fun run() {
        if (DISABLE_WATCHDOG) return // Disable watchdog early if the flag is set
        if (timeoutTime <= 0) return // Disable watchdog early if timeout time is <= 0

        while (!stopping) {
            if (!hasStarted || lastTick == 0L || !server.isRunning) continue // Jump out early if the server hasn't started yet

            val currentTime = System.currentTimeMillis()
            if (currentTime <= lastTick + earlyWarningInterval) continue // Jump out if we don't need to do anything

            val isLongTimeout = currentTime > lastTick + timeoutTime || (!server.isRunning && currentTime > lastTick + 1000)
            if (!isLongTimeout && (earlyWarningInterval <= 0 || currentTime < lastEarlyWarning + earlyWarningInterval || currentTime < lastTick + earlyWarningDelay)) continue
            if (!isLongTimeout && !server.isRunning) continue
            lastEarlyWarning = currentTime

            if (isLongTimeout) {
                LOGGER.printBar(true)
                Messages.WATCHDOG.STOPPED.fatal(LOGGER, KryptonServerInfo.version, KryptonServerInfo.minecraftVersion)
            } else {
                Messages.WATCHDOG.HEADER.warn(LOGGER)
                Messages.WATCHDOG.WARNING.warn(LOGGER, (currentTime - lastTick) / 1000)
            }

            LOGGER.printBar(isLongTimeout)
            if (isLongTimeout) Messages.WATCHDOG.DUMP.SERVER.fatal(LOGGER) else Messages.WATCHDOG.DUMP.SERVER.warn(LOGGER)
            THREAD_BEAN.getThreadInfo(server.mainThread.id, Int.MAX_VALUE)?.dump(LOGGER, isLongTimeout)
            LOGGER.printBar(isLongTimeout)

            if (isLongTimeout) {
                Messages.WATCHDOG.DUMP.ALL.fatal(LOGGER)
                THREAD_BEAN.dumpAllThreads(true, true).forEach { it.dump(LOGGER, true) }
            } else {
                Messages.WATCHDOG.HEADER.warn(LOGGER)
            }
            LOGGER.printBar(isLongTimeout)

            if (isLongTimeout && server.isRunning) {
                if (server.config.watchdog.restartOnCrash) server.restart() else server.stop()
            }
        }
    }

    companion object {

        private val DISABLE_WATCHDOG = java.lang.Boolean.getBoolean("disable.watchdog")
        private val THREAD_BEAN = ManagementFactory.getThreadMXBean()
        private val LOGGER = logger("Watchdog (I'm watching you)")
    }
}

private fun ThreadInfo.dump(logger: Logger, fatal: Boolean) {
    logger.printBar(fatal)
    if (fatal) {
        Messages.WATCHDOG.DUMP.THREAD.fatal(logger, threadName)
        Messages.WATCHDOG.DUMP.INFO.fatal(logger, threadId, isSuspended, isInNative, threadState)
    } else {
        Messages.WATCHDOG.DUMP.THREAD.warn(logger, threadName)
        Messages.WATCHDOG.DUMP.INFO.warn(logger, threadId, isSuspended, isInNative, threadState)
    }

    if (lockedMonitors.isNotEmpty()) {
        if (fatal) {
            Messages.WATCHDOG.DUMP.MONITORS.fatal(logger)
            lockedMonitors.forEach { Messages.WATCHDOG.DUMP.MONITOR.fatal(logger, it.lockedStackFrame) }
        } else {
            Messages.WATCHDOG.DUMP.MONITORS.warn(logger)
            lockedMonitors.forEach { Messages.WATCHDOG.DUMP.MONITOR.warn(logger, it.lockedStackFrame) }
        }
    }

    if (fatal) {
        Messages.WATCHDOG.DUMP.STACK.fatal(logger)
        stackTrace.forEach { Messages.WATCHDOG.DUMP.STACK_ELEMENT.fatal(logger, it) }
    } else {
        Messages.WATCHDOG.DUMP.STACK.warn(logger)
        stackTrace.forEach { Messages.WATCHDOG.DUMP.STACK_ELEMENT.warn(logger, it) }
    }
}

private fun Logger.log(fatal: Boolean, message: String) = if (fatal) fatal(message) else warn(message)

private fun Logger.printBar(fatal: Boolean) = log(fatal, "-".repeat(76))

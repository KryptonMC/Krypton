/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton

import org.apache.logging.log4j.Logger
import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
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

    private val timeoutTime = server.config.other.timeoutTime * 1000L
    private val earlyWarningInterval = min(server.config.other.earlyWarningInterval, timeoutTime)
    private val earlyWarningDelay = min(server.config.other.earlyWarningDelay, timeoutTime)

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
                LOGGER.fatal("The server has stopped responding! This could be, but is not likely to be, an issue with Krypton.")
                LOGGER.fatal("If you see a plugin in the server thread dump below, then please report it to that author.")
                LOGGER.fatal("\t *Especially* if it looks like HTTP or MySQL operations are occurring")
                LOGGER.fatal("If you see a world save or edit, then it likely means you tried to do much more than your server can handle at once")
                LOGGER.fatal("\tIf this is the case, consider increasing timeout-time in the main configuration file. Please note, however, that this will replace this crash with LARGE lag spikes")
                LOGGER.fatal("If you are still unsure, or you think that this actually a Krypton issue, especially if you see org.kryptonmc at the top of the trace, please report this to https://github.com/KryptonMC/Krypton/issues")
                LOGGER.fatal("When reporting to Krypton, please ensure that you include all relevant console errors and thread dumps, as this will help us diagnose your issue easier.")
                LOGGER.fatal("Krypton version: ${KryptonServerInfo.version} (for Minecraft ${KryptonServerInfo.minecraftVersion})")
            } else {
                LOGGER.warn("---- DO NOT REPORT THIS TO KRYPTON! THIS IS NOT A BUG OR CRASH! ----")
                LOGGER.warn("The server has not responded for ${(currentTime - lastTick) / 1000} seconds! Creating thread dump...")
            }

            LOGGER.printBar(isLongTimeout)
            LOGGER.log(isLongTimeout, "Server thread dump (look for plugins here before reporting this to Krypton):")
            THREAD_BEAN.getThreadInfo(server.mainThread.id, Int.MAX_VALUE)?.dump(LOGGER, isLongTimeout)
            LOGGER.printBar(isLongTimeout)

            if (isLongTimeout) {
                LOGGER.fatal("Entire Thread Dump:")
                THREAD_BEAN.dumpAllThreads(true, true).forEach { it.dump(LOGGER, true) }
            } else {
                LOGGER.warn("---- DO NOT REPORT THIS TO KRYPTON! THIS IS NOT A BUG OR CRASH! ----")
            }
            LOGGER.printBar(isLongTimeout)

            if (isLongTimeout && server.isRunning) {
                if (server.config.other.restartOnCrash) server.restart() else server.stop()
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
    logger.log(fatal, "Current Thread: $threadName")
    logger.log(fatal, "\tPID: $threadId | Suspended: $isSuspended | Native: $isInNative | State: $threadState")

    if (lockedMonitors.isNotEmpty()) {
        logger.log(fatal, "\tThread is waiting on monitor(s):")
        lockedMonitors.forEach { logger.log(fatal, "\t\tLocked on: ${it.lockedStackFrame}") }
    }

    logger.log(fatal, "\tStack:")
    stackTrace.forEach { logger.log(fatal, "\t\t$it") }
}

private fun Logger.log(fatal: Boolean, message: String) = if (fatal) fatal(message) else warn(message)

private fun Logger.printBar(fatal: Boolean) = log(fatal, "-".repeat(76))

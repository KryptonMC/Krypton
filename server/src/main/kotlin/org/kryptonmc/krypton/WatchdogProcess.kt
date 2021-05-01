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
    private val restartOnCrash = server.config.other.restartOnCrash
    private val earlyWarningInterval = min(server.config.other.earlyWarningInterval, timeoutTime.toLong())
    private val earlyWarningDelay = min(server.config.other.earlyWarningDelay, timeoutTime.toLong())

    private var lastEarlyWarning = 0L
    @Volatile private var lastTick = 0L
    @Volatile private var stopping = false

    fun tick(time: Long) {
        if (lastTick == 0L) hasStarted = true
        lastTick = time
    }

    fun shutdown() {
        stopping = true
    }

    override fun run() {
        if (DISABLE_WATCHDOG) return // Disable watchdog early if the flag is set

        while (!stopping) {
            val currentTime = System.currentTimeMillis()
            if (!(lastTick != 0L && timeoutTime > 0 && hasStarted && (!server.isRunning || (currentTime > lastTick + earlyWarningInterval)))) continue
            val isLongTimeout = currentTime > lastTick + timeoutTime || (!server.isRunning && currentTime > lastTick + 1000)

            if (!isLongTimeout && (earlyWarningInterval <= 0 || !hasStarted || currentTime < lastEarlyWarning + earlyWarningInterval || currentTime < lastTick + earlyWarningDelay)) continue
            if (!isLongTimeout && !server.isRunning) continue
            lastEarlyWarning = currentTime

            if (isLongTimeout) {
                LOGGER.error("------------------------------")
                LOGGER.error("The server has stopped responding! This is (probably) not a Krypton issue.")
                LOGGER.error("If you see a plugin in the Server thread dump below, then please report it to that author")
                LOGGER.error("\t *Especially* if it looks like HTTP or MySQL operations are occurring")
                LOGGER.error("If you see a world save or edit, then it means you did far more than your server can handle at once")
                LOGGER.error("\t If this is the case, consider increasing timeout-time in the main config.conf under \"other\", but note that this will replace the crash with LARGE lag spikes")
                LOGGER.error("If you are unsure, or still think that this is a Krypton issue, please report this to https://github.com/KryptonMC/Krypton/issues")
                LOGGER.error("Please ensure you include ALL relevant console errors and thread dumps")
                LOGGER.error("Krypton version: ${KryptonServerInfo.version} (for Minecraft ${KryptonServerInfo.minecraftVersion})")
            } else {
                LOGGER.error("--- DO NOT REPORT THIS TO KRYPTON - THIS IS NOT A BUG OR CRASH - ${KryptonServerInfo.version} (MC ${KryptonServerInfo.minecraftVersion}) ---")
                LOGGER.error("The server has not responded for ${(currentTime - lastTick) / 1000} seconds! Creating thread dump")
            }

            LOGGER.error("------------------------------")
            LOGGER.error("Server thread dump (Look for plugins here before reporting this to Krypton!):")
            THREAD_BEAN.getThreadInfo(server.mainThread.id, Int.MAX_VALUE)?.dump(LOGGER)
            LOGGER.error("------------------------------")

            if (isLongTimeout) {
                LOGGER.error("Entire Thread Dump:")
                THREAD_BEAN.dumpAllThreads(true, true).forEach { it.dump(LOGGER) }
            } else {
                LOGGER.error("--- DO NOT REPORT THIS TO KRYPTON - THIS IS NOT A BUG OR CRASH ---")
            }
            LOGGER.error("------------------------------")

            if (isLongTimeout && server.isRunning) server.stop(restartOnCrash)
        }
    }

    companion object {

        @Volatile var hasStarted = false

        private val DISABLE_WATCHDOG = java.lang.Boolean.getBoolean("disable.watchdog")
        private val THREAD_BEAN = ManagementFactory.getThreadMXBean()
        private val LOGGER = logger("Watchdog (I'm watching you)")
    }
}

private fun ThreadInfo.dump(logger: Logger) {
    logger.error("------------------------------")
    logger.error("Current Thread: $threadName")
    logger.error("\tPID: $threadId | Suspended: $isSuspended | Native: $isInNative | State: $threadState")

    if (lockedMonitors.isNotEmpty()) {
        logger.error("\tThread is waiting on monitor(s):")
        lockedMonitors.forEach { logger.error("\t\tLocked on: ${it.lockedStackFrame}") }
    }

    logger.error("\tStack:")
    stackTrace.forEach { logger.error("\t\t$it") }
}

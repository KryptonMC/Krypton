package org.kryptonmc.krypton

import org.kryptonmc.krypton.util.concurrent.NamedUncaughtExceptionHandler
import org.kryptonmc.krypton.util.logger
import java.util.Locale
import kotlin.system.exitProcess

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

    private val tickThreshold = server.config.server.tickThreshold

    override fun run() {
        while (server.isRunning) {
            val tickTime = System.currentTimeMillis() - server.lastTickTime
            if (tickTime > tickThreshold) {
                LOGGER.fatal("A single server tick took ${"%.2f".format(Locale.ROOT, tickTime / 1000.0f)} seconds (threshold: $tickThreshold)!")
                LOGGER.fatal("Considering it crashed, shutting down...")
                exitProcess(1)
            }
        }
    }

    companion object {

        private val LOGGER = logger("Watchdog (I'm watching you)")
    }
}

package org.kryptonmc.krypton

import org.kryptonmc.krypton.concurrent.NamedUncaughtExceptionHandler
import org.kryptonmc.krypton.extension.logger
import java.util.*
import kotlin.system.exitProcess

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
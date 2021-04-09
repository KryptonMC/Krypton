package org.kryptonmc.krypton.util

import org.kryptonmc.krypton.concurrent.DefaultUncaughtExceptionHandler
import org.kryptonmc.krypton.extension.logger
import java.util.concurrent.atomic.AtomicInteger

abstract class GenericThread(protected val name: String) : Runnable {

    @Volatile
    var isRunning = false; protected set
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
package org.kryptonmc.krypton.concurrent

import org.apache.logging.log4j.Logger

class DefaultUncaughtExceptionHandler(private val logger: Logger) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        logger.error("Caught previously unhandled exception ", exception)
    }
}
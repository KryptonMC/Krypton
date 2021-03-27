package org.kryptonmc.krypton.concurrent

import org.apache.logging.log4j.Logger

class NamedUncaughtExceptionHandler(private val logger: Logger) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        logger.error("Caught previously unhandled exception:")
        logger.error(thread.name, exception)
    }
}
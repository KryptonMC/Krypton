package org.kryptonmc.krypton.util.concurrent

import org.apache.logging.log4j.Logger

/**
 * Used for catching uncaught exceptions in [Thread]s and logging them to a Log4J [Logger],
 * using the name of the [Thread].
 */
class NamedUncaughtExceptionHandler(private val logger: Logger) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        logger.error("Caught previously unhandled exception:")
        logger.error(thread.name, exception)
    }
}

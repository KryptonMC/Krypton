package org.kryptonmc.krypton.concurrent

import org.apache.logging.log4j.Logger

/**
 * Used for catching uncaught exceptions in [Thread]s and logging them to a Log4J [Logger],
 * using the name of the [Thread].
 *
 * @author Callum Seabrook
 */
class NamedUncaughtExceptionHandler(private val logger: Logger) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        logger.error("Caught previously unhandled exception:")
        logger.error(thread.name, exception)
    }
}
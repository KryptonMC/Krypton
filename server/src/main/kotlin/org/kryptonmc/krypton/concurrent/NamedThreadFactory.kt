package org.kryptonmc.krypton.concurrent

import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * A thread factory that automatically formats the specified [nameFormat] by replacing any
 * "%d" keys with the current thread number.
 *
 * For example, if you have 5 threads in a pool, and the name format is "My Thread #%d", the
 * first thread will be named "My Thread #1", the second will be named "My Thread #2", and so
 * on.
 *
 * @author Callum Seabrook
 */
class NamedThreadFactory(private val nameFormat: String) : ThreadFactory {

    private val threadNumber = AtomicInteger(1)
    private val backingFactory = Executors.defaultThreadFactory()

    override fun newThread(task: Runnable): Thread = backingFactory.newThread(task).apply {
        name = nameFormat.format(Locale.ROOT, threadNumber.getAndIncrement())
    }
}
package org.kryptonmc.krypton.concurrent

import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class NamedThreadFactory(private val nameFormat: String) : ThreadFactory {

    private val threadNumber = AtomicInteger(1)
    private val backingFactory = Executors.defaultThreadFactory()

    override fun newThread(task: Runnable): Thread = backingFactory.newThread(task).apply {
        name = nameFormat.format(Locale.ROOT, threadNumber.getAndIncrement())
    }
}
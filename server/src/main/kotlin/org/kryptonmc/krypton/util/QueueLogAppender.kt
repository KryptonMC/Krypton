package org.kryptonmc.krypton.util

import org.apache.logging.log4j.core.Filter
import org.apache.logging.log4j.core.Layout
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.appender.AbstractAppender
import org.apache.logging.log4j.core.config.plugins.Plugin
import org.apache.logging.log4j.core.config.plugins.PluginAttribute
import org.apache.logging.log4j.core.config.plugins.PluginElement
import org.apache.logging.log4j.core.config.plugins.PluginFactory
import org.apache.logging.log4j.core.layout.PatternLayout
import java.io.Serializable
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * An appender that creates a queue for log events and allows you to retrieve them
 */
@Plugin(name = "Queue", category = "Core", elementType = "appender", printObject = true)
class QueueLogAppender(
    name: String,
    filter: Filter?,
    layout: Layout<out Serializable>,
    ignoreExceptions: Boolean,
    private val queue: BlockingQueue<String>
) : AbstractAppender(name, filter, layout, ignoreExceptions) {

    override fun append(event: LogEvent) {
        if (queue.size >= MAXIMUM_CAPACITY) queue.clear()
        queue += layout.toSerializable(event).toString()
    }

    companion object {

        private const val MAXIMUM_CAPACITY = 250
        private val QUEUES = mutableMapOf<String, BlockingQueue<String>>()
        private val QUEUE_LOCK = ReentrantReadWriteLock()

        private val NULL_PATTERN_LAYOUT = PatternLayout.newBuilder().build()

        @PluginFactory
        @JvmStatic
        fun createAppender(
            @PluginAttribute("name") name: String?,
            @PluginAttribute("ignoreExceptions") ignore: String?,
            @PluginElement("Layout") layout: Layout<out Serializable>?,
            @PluginElement("Filters") filter: Filter?,
            @PluginAttribute("target") target: String?
        ): QueueLogAppender? {
            val ignoreExceptions = ignore.toBoolean()
            if (name == null) {
                LOGGER.error("No name provided for QueueLogAppender!")
                return null
            }

            QUEUE_LOCK.writeLock().lock()
            val queue = QUEUES.getOrPut(target ?: name) { LinkedBlockingQueue() }
            QUEUE_LOCK.writeLock().unlock()

            return QueueLogAppender(name, filter, layout ?: NULL_PATTERN_LAYOUT, ignoreExceptions, queue)
        }

        fun nextLogEvent(name: String): String? {
            QUEUE_LOCK.readLock().lock()
            val queue = QUEUES[name] ?: return null
            QUEUE_LOCK.readLock().unlock()
            return queue.take()
        }
    }
}

/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util

import org.apache.logging.log4j.core.Filter
import org.apache.logging.log4j.core.Layout
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.appender.AbstractAppender
import org.apache.logging.log4j.core.config.Property
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
    properties: Array<Property>,
    private val queue: BlockingQueue<String>
) : AbstractAppender(name, filter, layout, ignoreExceptions, properties) {

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
            @PluginAttribute("target") target: String?,
            @PluginAttribute("properties") properties: Array<Property>
        ): QueueLogAppender? {
            val ignoreExceptions = ignore.toBoolean()
            if (name == null) {
                LOGGER.error("No name provided for QueueLogAppender!")
                return null
            }

            QUEUE_LOCK.writeLock().lock()
            val queue = QUEUES.getOrPut(target ?: name) { LinkedBlockingQueue() }
            QUEUE_LOCK.writeLock().unlock()

            return QueueLogAppender(name, filter, layout ?: NULL_PATTERN_LAYOUT, ignoreExceptions, properties, queue)
        }

        fun nextLogEvent(name: String): String? {
            QUEUE_LOCK.readLock().lock()
            val queue = QUEUES[name] ?: return null
            QUEUE_LOCK.readLock().unlock()
            return queue.take()
        }
    }
}

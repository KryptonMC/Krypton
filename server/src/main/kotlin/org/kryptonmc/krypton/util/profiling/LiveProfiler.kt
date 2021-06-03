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
package org.kryptonmc.krypton.util.profiling

import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.profiling.entry.PathEntry
import org.kryptonmc.krypton.util.profiling.results.FilledProfileResults
import org.kryptonmc.krypton.util.profiling.results.ProfileResults
import org.kryptonmc.krypton.util.profiling.results.demangled

class LiveProfiler(
    private val tickTimeSource: () -> Int,
    private val warn: Boolean
) : CollectibleProfiler {

    private val startTime = REAL_TIME_SOURCE()
    private val startTimeTicks = tickTimeSource()

    private val paths = mutableListOf<String>()
    private val startTimes = LongArrayList()
    private val entries = mutableMapOf<String, LivePathEntry>()

    private var path = ""
    private var running = false
    private var currentEntry: LivePathEntry? = null; get() = field ?: entries.getOrPut(path) { LivePathEntry().apply { field = this } }

    override fun start() {
        if (running) {
            Messages.PROFILER.ERROR.STARTED.error(LOGGER)
            return
        }
        running = true
        path = ""
        paths.clear()
        push("root")
    }

    override fun end() {
        if (!running) {
            Messages.PROFILER.ERROR.ENDED.error(LOGGER)
            return
        }
        pop()
        running = false
        if (path.isNotEmpty()) Messages.PROFILER.ERROR.NOT_FULLY_POPPED.error(LOGGER, path.demangled)
    }

    override fun push(name: String) {
        if (!running) {
            Messages.PROFILER.ERROR.NOT_STARTED.error(LOGGER, Messages.PROFILER.PUSH.text(), name)
            return
        }
        if (path.isNotEmpty()) path += '\u001e'
        path += name
        paths += path
        startTimes += System.nanoTime()
        currentEntry = null
    }

    override fun push(name: () -> String) = push(name())

    override fun pop() {
        if (!running) {
            Messages.PROFILER.ERROR.NOT_STARTED.error(LOGGER, Messages.PROFILER.POP.text(), "")
            return
        }
        if (startTimes.isEmpty) {
            Messages.PROFILER.ERROR.TOO_MANY_POPS.error(LOGGER)
            return
        }
        val timeNow = System.nanoTime()
        val lastTime = startTimes.removeLong(startTimes.lastIndex)
        paths.removeLast()
        val difference = timeNow - lastTime
        currentEntry?.apply {
            duration += difference
            count++
        }
        if (warn && difference > WARNING_TIME_NANOS) {
            Messages.PROFILER.ERROR.TOO_LONG.warn(LOGGER, path.demangled, difference / 1_000_000.0)
        }
        path = if (paths.isEmpty()) "" else paths.last()
        currentEntry = null
    }

    override fun popPush(name: String) {
        pop()
        push(name)
    }

    override fun incrementCounter(name: String) {
        currentEntry?.counters?.addTo(name, 1L)
    }

    override fun incrementCounter(name: () -> String) = incrementCounter(name())

    override val results: ProfileResults get() = FilledProfileResults(entries, startTime, startTimeTicks, REAL_TIME_SOURCE(), tickTimeSource())

    private class LivePathEntry : PathEntry {

        override var duration = 0L
        override var count = 0L
        override val counters = Object2LongOpenHashMap<String>()
    }

    companion object {

        private const val WARNING_TIME_NANOS = 100_000_000
        private val REAL_TIME_SOURCE = System::nanoTime
        private val LOGGER = logger<LiveProfiler>()
    }
}

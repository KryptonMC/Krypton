package org.kryptonmc.krypton.util.profiling

import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
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
            LOGGER.error("Profiler tick has already been started! Perhaps we didn't call end?")
            return
        }
        running = true
        path = ""
        paths.clear()
        push("root")
    }

    override fun end() {
        if (!running) {
            LOGGER.error("Profiler tick has already ended! Perhaps we didn't call start?")
            return
        }
        pop()
        running = false
        if (path.isNotEmpty()) {
            LOGGER.error("Profiler tick was ended before path was fully popped (${path.demangled} remaining)! Perhaps push and pop are mismatched?")
        }
    }

    override fun push(name: String) {
        if (!running) {
            LOGGER.error("You need to start profiling before attempting to push data! Ignoring attempt to push $name")
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
            LOGGER.error("You need to start profiling before attempting to pop data! Ignoring pop attempt")
            return
        }
        if (startTimes.isEmpty) {
            LOGGER.error("Tried to pop one too many times! Perhaps push and pop are mismatched?")
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
            LOGGER.warn("Something's taking too long! ${path.demangled} too approximately ${difference / 1000000.0} ms")
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

        private const val WARNING_TIME_NANOS = 100000000
        private val REAL_TIME_SOURCE = System::nanoTime
        private val LOGGER = logger<LiveProfiler>()
    }
}

package org.kryptonmc.krypton.util.profiling

import org.kryptonmc.krypton.util.logger
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SingleTickProfiler(
    private val saveThreshold: Long,
    private val folder: File
) {

    private var tick = 0
    private lateinit var profiler: CollectibleProfiler

    fun start(): Profiler {
        profiler = LiveProfiler(this::tick, false)
        tick++
        return profiler
    }

    fun end() {
        if (profiler == DeadProfiler) return
        val results = profiler.results
        profiler = DeadProfiler
        if (results.duration >= saveThreshold) {
            val file = File(folder, "tick-results-$TIME_NOW_FORMATTED.txt")
            results.save(file)
            LOGGER.info("Recorded long tick -- wrote info to ${file.absolutePath}")
        }
    }

    companion object {

        private val TIME_NOW_FORMATTED: String
            get() = FORMATTER.format(LocalDateTime.now())

        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss")
        private val LOGGER = logger<SingleTickProfiler>()

        fun create(name: String): SingleTickProfiler? = null
    }
}

fun Profiler.decorate(other: SingleTickProfiler?): Profiler = other?.let { return tee(it.start(), this) } ?: this

package org.kryptonmc.krypton.util.profiling

import org.kryptonmc.krypton.util.logger
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SingleTickProfiler(
    private val saveThreshold: Long,
    private val folder: Path
) {

    private var tick = 0
    private lateinit var profiler: CollectibleProfiler

    fun start(): Profiler {
        profiler = LiveProfiler(::tick, false)
        tick++
        return profiler
    }

    fun end() {
        if (profiler == DeadProfiler) return
        val results = profiler.results
        profiler = DeadProfiler
        if (results.duration >= saveThreshold) {
//            val file = File(folder, "tick-results-$TIME_NOW_FORMATTED.txt")
            val file = folder.resolve("tick-results-$TIME_NOW_FORMATTED.txt")
            results.save(file)
            LOGGER.info("Recorded long tick -- wrote info to ${file.toAbsolutePath()}")
        }
    }

    companion object {

        private val TIME_NOW_FORMATTED: String
            get() = FORMATTER.format(LocalDateTime.now())

        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss")
        private val LOGGER = logger<SingleTickProfiler>()
    }
}

fun Profiler.decorate(other: SingleTickProfiler?): Profiler = other?.let { return tee(it.start(), this) } ?: this

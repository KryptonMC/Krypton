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

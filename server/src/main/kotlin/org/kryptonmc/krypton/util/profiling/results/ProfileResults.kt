package org.kryptonmc.krypton.util.profiling.results

import java.nio.file.Path

interface ProfileResults {

    val startTime: Long

    val startTimeTicks: Int

    val endTime: Long

    val endTimeTicks: Int

    val duration: Long get() = endTime - startTime

    val durationTicks: Int get() = endTimeTicks - startTimeTicks

    fun save(file: Path): Boolean
}

val String.demangled: String get() = replace("\u001e", ".")

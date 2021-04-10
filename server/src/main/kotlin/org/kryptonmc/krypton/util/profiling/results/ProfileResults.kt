package org.kryptonmc.krypton.util.profiling.results

import java.io.File

interface ProfileResults {

    val startTime: Long

    val startTimeTicks: Int

    val endTime: Long

    val endTimeTicks: Int

    val duration: Long
        get() = endTime - startTime

    val durationTicks: Int
        get() = endTimeTicks - startTimeTicks

    fun save(file: File): Boolean
}

val String.demangled: String
    get() = replace("\u001e", ".")
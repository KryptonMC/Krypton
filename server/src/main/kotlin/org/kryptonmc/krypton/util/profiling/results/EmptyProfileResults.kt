package org.kryptonmc.krypton.util.profiling.results

import java.io.File

object EmptyProfileResults : ProfileResults {

    override val startTime = 0L
    override val endTime = 0L
    override val startTimeTicks = 0
    override val endTimeTicks = 0

    override fun save(file: File) = false
}
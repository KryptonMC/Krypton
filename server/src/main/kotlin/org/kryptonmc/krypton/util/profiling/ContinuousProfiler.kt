package org.kryptonmc.krypton.util.profiling

import org.kryptonmc.krypton.util.profiling.results.ProfileResults

class ContinuousProfiler(private val tickTimeSource: () -> Int) {

    var profiler: CollectibleProfiler = DeadProfiler; private set

    val isEnabled: Boolean
        get() = profiler != DeadProfiler

    fun enable() {
        profiler = LiveProfiler(tickTimeSource, true)
    }

    fun disable() {
        profiler = DeadProfiler
    }

    val results: ProfileResults
        get() = profiler.results
}
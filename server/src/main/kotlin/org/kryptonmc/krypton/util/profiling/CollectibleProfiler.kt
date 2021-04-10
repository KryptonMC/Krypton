package org.kryptonmc.krypton.util.profiling

import org.kryptonmc.krypton.util.profiling.results.ProfileResults

interface CollectibleProfiler : Profiler {

    val results: ProfileResults
}
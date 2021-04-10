package org.kryptonmc.krypton.util.profiling.entry

import it.unimi.dsi.fastutil.objects.Object2LongMap

interface PathEntry {

    var duration: Long

    var count: Long

    val counters: Object2LongMap<String>
}
package org.kryptonmc.krypton.util.profiling.entry

import it.unimi.dsi.fastutil.objects.Object2LongMap
import it.unimi.dsi.fastutil.objects.Object2LongMaps

object EmptyPathEntry : PathEntry {

    override var duration = 0L
    override var count = 0L
    override val counters: Object2LongMap<String> = Object2LongMaps.emptyMap()
}
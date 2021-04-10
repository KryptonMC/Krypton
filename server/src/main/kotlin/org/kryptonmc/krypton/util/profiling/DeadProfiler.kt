package org.kryptonmc.krypton.util.profiling

import org.kryptonmc.krypton.util.profiling.results.EmptyProfileResults

object DeadProfiler : CollectibleProfiler {

    override val results = EmptyProfileResults

    override fun start() = Unit
    override fun end() = Unit
    override fun push(name: String) = Unit
    override fun push(name: () -> String) = Unit
    override fun pop() = Unit
    override fun popPush(name: String) = Unit
    override fun incrementCounter(name: String) = Unit
    override fun incrementCounter(name: () -> String) = Unit
}
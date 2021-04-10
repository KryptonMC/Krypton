package org.kryptonmc.krypton.util.profiling

interface Profiler {

    fun start()

    fun end()

    fun push(name: String)

    fun push(name: () -> String)

    fun pop()

    fun popPush(name: String)

    fun incrementCounter(name: String)

    fun incrementCounter(name: () -> String)
}

fun tee(first: Profiler, second: Profiler): Profiler {
    if (first == DeadProfiler) return second
    if (second == DeadProfiler) return first
    return TeeProfiler(first, second)
}

private class TeeProfiler(
    val first: Profiler,
    val second: Profiler
) : Profiler {

    override fun start() {
        first.start()
        second.start()
    }

    override fun end() {
        first.end()
        second.end()
    }

    override fun push(name: String) {
        first.push(name)
        second.push(name)
    }

    override fun push(name: () -> String) {
        first.push(name)
        second.push(name)
    }

    override fun pop() {
        first.pop()
        second.pop()
    }

    override fun popPush(name: String) {
        first.popPush(name)
        second.popPush(name)
    }

    override fun incrementCounter(name: String) {
        first.incrementCounter(name)
        second.incrementCounter(name)
    }

    override fun incrementCounter(name: () -> String) {
        first.incrementCounter(name)
        second.incrementCounter(name)
    }
}
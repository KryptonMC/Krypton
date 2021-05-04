package org.kryptonmc.krypton.util.profiling.results

import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
import org.kryptonmc.krypton.util.createDirectories
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.newOutputStream
import org.kryptonmc.krypton.util.profiling.entry.EmptyPathEntry
import org.kryptonmc.krypton.util.profiling.entry.PathEntry
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.file.Path
import java.util.Locale
import java.util.TreeMap

class FilledProfileResults(
    private val entries: Map<String, PathEntry>,
    override val startTime: Long,
    override val startTimeTicks: Int,
    override val endTime: Long,
    override val endTimeTicks: Int
) : ProfileResults {

    private fun timesFor(name: String): List<ResultField> {
        val temp = if (name.isNotEmpty()) name + PATH_SEPARATOR else name
        val root = entries["root"] ?: EmptyPathEntry
        val entry = entries[name] ?: EmptyPathEntry

        var totalDuration = entries.keys.sumBy {
            if (!temp.isChildOf(it)) return@sumBy 0L
            entries[it]?.duration ?: EmptyPathEntry.duration
        }

        val floatDuration = totalDuration.toFloat()
        if (totalDuration < entry.duration) totalDuration = entry.duration
        if (root.duration < totalDuration) root.duration = totalDuration

        val results = (entries.filter { temp.isChildOf(it.key) }.map { (key, value) ->
            val percentage = (value.duration * 100.0) / totalDuration
            val globalPercentage = (value.duration * 100.0) / root.duration
            ResultField(key.substring(temp.length), percentage, globalPercentage, value.count)
        } as MutableList<ResultField>).apply {
            if (totalDuration > floatDuration) {
                val percentage = ((totalDuration - floatDuration) * 100.0) / totalDuration
                val globalPercentage = ((totalDuration - floatDuration) * 100.0) / root.duration
                add(ResultField("unspecified", percentage, globalPercentage, entry.count))
            }
        }
        results.sort()
        results.add(0, ResultField(temp, 100.0, (totalDuration * 100.0) / root.duration, entry.count))
        return results
    }

    override fun save(file: Path): Boolean {
        file.parent.createDirectories()
        return try {
            OutputStreamWriter(file.newOutputStream(), Charsets.UTF_8).use { it.write(profilerResults) }
            true
        } catch (exception: Exception) {
            LOGGER.error("Could not save profiler results to $file", exception)
            false
        }
    }

    private val profilerResults: String
        get() = buildString {
            append("""
                ---- Krypton Profiler Results ----
                // $comment

                Krypton version: ${KryptonServerInfo.version}
                Minecraft version: ${KryptonServerInfo.minecraftVersion}
                Time span: ${duration / 1000000} ms
                Tick span: $durationTicks ticks
                // This is approximately ${"%.2f".format(Locale.ROOT, durationTicks / (duration / 1.0E9))} ticks per second. It should be 20 ticks per second

                --- BEGIN PROFILE DUMP ---
                """.trimIndent()).append("\n")
            appendProfilerResults(0, "root")
            append("--- END PROFILE DUMP --\n\n")
            val values = counterValues
            if (values.isEmpty()) return@buildString

            append("--- BEGIN COUNTER DUMP ---\n\n")
            appendCounters(values, durationTicks)
            append("--- END COUNTER DUMP ---\n\n")
        }

    private val counterValues: Map<String, CounterCollector>
        get() {
            val results = TreeMap<String, CounterCollector>()
            entries.forEach { (key, value) ->
                val counters = value.counters
                if (counters.isEmpty()) return@forEach
                val valueIterator = key.split(PATH_SEPARATOR).iterator()
                counters.forEach { results.getOrPut(it.key) { CounterCollector() }.add(valueIterator, it.value) }
            }
            return results
        }

    private val comment: String
        get() = try {
            COMMENTS[(System.nanoTime() % COMMENTS.size).toInt()]
        } catch (exception: Exception) {
            "Witty comment unavailable :("
        }

    private fun StringBuilder.appendProfilerResults(accumulator: Int, name: String) {
        val times = timesFor(name)
        val counters = entries[name]?.counters ?: EmptyPathEntry.counters
        counters.forEach { (key, value) -> indentLine(accumulator).append("#$key $value/${value / durationTicks}\n") }
        if (times.size < 3) return

        times.drop(1).forEach {
            indentLine(accumulator).append("${it.name}(${it.count}/")
                .append("${"%.0f".format(Locale.ROOT, (it.count / durationTicks).toFloat())}) - ")
                .append("${"%.2f".format(Locale.ROOT, it.percentage.toFloat())}%/")
                .append("${"%.2f".format(Locale.ROOT, it.globalPercentage.toFloat())}%\n")

            if (it.name == "unspecified") return@forEach
            try {
                appendProfilerResults(accumulator + 1, name + PATH_SEPARATOR + it.name)
            } catch (exception: Exception) {
                append("[[ EXCEPTION $exception ]]")
            }
        }
    }

    private fun StringBuilder.indentLine(number: Int) = apply {
        append("[%02d] ".format(number))
        repeat(number) { append("|   ") }
    }

    private fun StringBuilder.appendCounters(counters: Map<String, CounterCollector>, durationTicks: Int) = counters.forEach { (key, collector) ->
        append("-- Counter: $key --\n")
        appendCounterResults(0, "root", collector.children.getValue("root"), durationTicks)
        append("\n\n")
    }

    private fun StringBuilder.appendCounterResults(accumulator: Int, name: String, collector: CounterCollector, durationTicks: Int) {
        indentLine(accumulator).append("$name total: ${collector.value}/${collector.total}")
            .append(" average: ${collector.value / durationTicks}/${collector.total / durationTicks}\n")
        collector.children.entries.sortedWith(COUNTER_ENTRY_COMPARATOR)
            .forEach { appendCounterResults(accumulator + 1, it.key, it.value, durationTicks) }
    }

    private class CounterCollector {

        var value = 0L
        var total = 0L
        val children = mutableMapOf<String, CounterCollector>()

        fun add(names: Iterator<String>, value: Long) {
            total += value
            if (!names.hasNext()) {
                this.value += value
            } else {
                children.getOrPut(names.next()) { CounterCollector() }.add(names, value)
            }
        }
    }

    companion object {

        private const val PATH_SEPARATOR = '\u001e'
        private val COMMENTS = arrayOf(
            "Shiny numbers!", "Am I not running fast enough? :(", "I'm working as hard as I can!", "Will I ever be good enough for you? :(",
            "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers",
            "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?",
            "*cracks redstone whip*", "Maybe if you treated it better then it'll have more motivation to work faster! Poor server."
        )
        private val COUNTER_ENTRY_COMPARATOR = java.util.Map.Entry
            .comparingByValue<String, CounterCollector> { o1, o2 -> o1.total.compareTo(o2.total) }

        private val LOGGER = logger<FilledProfileResults>()
    }
}

private fun String.isChildOf(other: String) = other.length > length
        && other.startsWith(this)
        && other.indexOf(30.toChar(), length + 1) < 0

fun <T> Iterable<T>.sumBy(selector: (T) -> Long): Long {
    var sum = 0L
    for (element in this) sum += selector(element)
    return sum
}

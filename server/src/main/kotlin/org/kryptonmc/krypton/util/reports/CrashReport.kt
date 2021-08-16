/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util.reports

import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.util.createDirectories
import org.kryptonmc.krypton.util.logger
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.management.ManagementFactory
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletionException
import kotlin.io.path.outputStream

class CrashReport(val title: String, val exception: Throwable) {

    val systemDetails = CrashReportCategory("System Details")
    private val details = mutableListOf<CrashReportCategory>()

    private lateinit var saveFile: Path
    private var trackingStackTrace = true
    private var uncategorizedStackTrace = emptyArray<StackTraceElement?>()

    init {
        systemDetails["Implementation Name"] = { KryptonPlatform.name }
        systemDetails["Krypton Version"] = { KryptonPlatform.version }
        systemDetails["Minecraft Version"] = { KryptonPlatform.minecraftVersion }
        systemDetails["Operating System"] = { "${System.getProperty("os.name")} (${System.getProperty("os.arch")}) version ${System.getProperty("os.version")}" }
        systemDetails["Java Version"] = { "${System.getProperty("java.version")}, ${System.getProperty("java.vendor")}" }
        systemDetails["Java VM Version"] = { "${System.getProperty("java.vm.name")} (${System.getProperty("java.vm.info")}), ${System.getProperty("java.vm.vendor")}" }
        systemDetails["Memory"] = {
            val runtime = Runtime.getRuntime()
            val max = runtime.maxMemory()
            val total = runtime.totalMemory()
            val free = runtime.freeMemory()
            val maxMegabytes = max / 1024L / 1024L
            val totalMegabytes = total / 1024L / 1024L
            val freeMegabytes = free / 1024L / 1024L
            "$free bytes ($freeMegabytes MB) / $total bytes ($totalMegabytes MB) up to $max bytes ($maxMegabytes MB)"
        }
        systemDetails["CPU"] = Runtime.getRuntime().availableProcessors()
        systemDetails["JVM Flags"] = {
            val arguments = ManagementFactory.getRuntimeMXBean().inputArguments.filter { it.startsWith("-X") }
            "${arguments.size} total; ${arguments.joinToString(" ")}"
        }
    }

    private val detailString: String
        get() = buildString {
            if (!(uncategorizedStackTrace.isNotEmpty() || details.isEmpty())) uncategorizedStackTrace = details[0].stackTrace.sliceArray(0..1)
            if (uncategorizedStackTrace.isNotEmpty()) {
                append("-- Head --\n")
                append("Thread: ${Thread.currentThread().name}\n")
                append("Stack Trace:\n")
                uncategorizedStackTrace.forEach { append("\tat $it\n") }
            }
            details.forEach { append("${it.details}\n\n") }
            append(systemDetails.details)
        }

    val report: String
        get() = buildString {
            append("""
                ---- Krypton Crash Report ----
                // $COMMENT

                Time: ${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)}
                Description: $title
            """.trimIndent()).append("\n")
            append(exceptionMessage)
            append("\n").append("A detailed walkthrough of the error, its code path and all known details is as follows:").append("\n")
            append("-".repeat(87)).append("\n\n")
            append(detailString)
        }

    private val exceptionMessage: String
        get() {
            val stringWriter = StringWriter()
            val printWriter = PrintWriter(stringWriter)
            var exception = exception
            if (exception.message == null) {
                exception = when (exception) {
                    is NullPointerException -> NullPointerException(title)
                    is StackOverflowError -> StackOverflowError(title)
                    is OutOfMemoryError -> OutOfMemoryError(title)
                    else -> this.exception
                }
                exception.stackTrace = this.exception.stackTrace
            }
            printWriter.use { exception.printStackTrace(it) }
            return stringWriter.toString()
        }

    fun save(file: Path): Boolean {
        if (this::saveFile.isInitialized) return false
        if (file.parent != null) file.parent.createDirectories()

        try {
            OutputStreamWriter(file.outputStream(), Charsets.UTF_8).use { it.write(report) }
            saveFile = file
        } catch (exception: Exception) {
            LOGGER.error("Failed to save crash report to $file!", exception)
        }
        return true
    }

    fun addCategory(title: String, ignoredCallCount: Int = 1): CrashReportCategory {
        val category = CrashReportCategory(title)
        if (trackingStackTrace) {
            val callCount = category.fillStackTrace(ignoredCallCount)
            val trace = exception.stackTrace
            var previous: StackTraceElement? = null
            var next: StackTraceElement? = null

            val index = trace.size - callCount
            if (index < 0) println("Negative index in crash report handler (${trace.size}/$index)!")
            if (index in 0..trace.size) {
                previous = trace[index]
                if (trace.size + 1 - index < trace.size) next = trace[trace.size + 1 - index]
            }
            trackingStackTrace = category.validateStackTrace(previous, next)
            if (index > 0 && details.isNotEmpty()) {
                details.last().trimStackTrace(index)
            } else if (trace.size >= index && index in 0..trace.size) {
                uncategorizedStackTrace = arrayOfNulls(index)
                System.arraycopy(trace, 0, uncategorizedStackTrace, 0, uncategorizedStackTrace.size)
            } else {
                trackingStackTrace = false
            }
        }
        details += category
        return category
    }

    companion object {

        private val COMMENTS = arrayOf(
            "Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.",
            "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.",
            "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...",
            "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.",
            "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!",
            "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?",
            "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.",
            "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?",
            "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!",
            "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.",
            "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!",
            "But it works on my machine."
        )
        private val LOGGER = logger<CrashReport>()

        private val COMMENT: String get() = try {
            COMMENTS[(System.nanoTime() % COMMENTS.size).toInt()]
        } catch (exception: Exception) {
            "Witty comment unavailable :("
        }

        fun of(exception: Throwable, title: String): CrashReport {
            var temp = exception
            while (temp is CompletionException && temp.cause != null) temp = temp.cause!!
            return if (exception is ReportedException) {
                exception.report
            } else {
                CrashReport(title, exception)
            }
        }

        fun preload() {
            CrashReport("Don't panic!", Throwable()).report
        }
    }
}

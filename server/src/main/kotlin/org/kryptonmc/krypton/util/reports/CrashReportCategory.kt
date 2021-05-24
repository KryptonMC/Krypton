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

class CrashReportCategory(private val title: String) {

    private val entries = mutableListOf<Entry>()
    var stackTrace = emptyArray<StackTraceElement?>(); private set

    operator fun set(key: String, value: () -> String) {
        try {
            set(key, value())
        } catch (exception: Throwable) {
            set(key, exception)
        }
    }

    operator fun set(key: String, value: Any?) = entries.add(Entry(key, value))

    fun fillStackTrace(ignoredCallCount: Int): Int {
        val trace = Thread.currentThread().stackTrace
        if (trace.isEmpty()) return 0
        stackTrace = arrayOfNulls(trace.size - 3 - ignoredCallCount)
        System.arraycopy(trace, 3 + ignoredCallCount, stackTrace, 0, stackTrace.size)
        return stackTrace.size
    }

    fun validateStackTrace(previous: StackTraceElement?, next: StackTraceElement?): Boolean {
        if (stackTrace.isEmpty() || previous == null) return false
        val first = stackTrace[0]!!
        if (first.isNativeMethod != previous.isNativeMethod ||
            first.className != previous.className ||
            first.fileName != previous.fileName ||
            first.methodName != previous.methodName
        ) {
            return false
        }
        if (next == null == stackTrace.size > 1) return false
        if (next != null && stackTrace[1] != next) return false
        stackTrace[0] = previous
        return true
    }

    fun trimStackTrace(ignoredCallCount: Int) {
        val trace = arrayOfNulls<StackTraceElement>(stackTrace.size - ignoredCallCount)
        System.arraycopy(stackTrace, 0, trace, 0, trace.size)
        stackTrace = trace
    }

    val details: String
        get() = buildString {
            append("-- $title --\n")
            append("Details:")
            entries.forEach { append("\n\t${it.key}: ${it.value}") }
            if (stackTrace.isNotEmpty()) {
                append("\nStack Trace:")
                stackTrace.forEach { append("\n\tat $it") }
            }
        }

    private class Entry(val key: String, value: Any?) {

        val value = when (value) {
            null -> "~~NULL~~"
            is Throwable -> "~~ERROR~~ ${value::class.java.simpleName}: ${value.message}"
            else -> value.toString()
        }
    }
}

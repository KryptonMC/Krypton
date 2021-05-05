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
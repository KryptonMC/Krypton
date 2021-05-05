/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util.concurrent

import java.util.Locale
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * A thread factory that automatically formats the specified [nameFormat] by replacing any
 * "%d" keys with the current thread number.
 *
 * For example, if you have 5 threads in a pool, and the name format is "My Thread #%d", the
 * first thread will be named "My Thread #1", the second will be named "My Thread #2", and so
 * on.
 */
class NamedThreadFactory(private val nameFormat: String) : ThreadFactory {

    private val threadNumber = AtomicInteger(1)
    private val backingFactory = Executors.defaultThreadFactory()

    override fun newThread(task: Runnable): Thread = backingFactory.newThread(task).apply {
        name = nameFormat.format(Locale.ROOT, threadNumber.getAndIncrement())
    }
}

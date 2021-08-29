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
package org.kryptonmc.krypton.util

import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.ThreadFactory

inline fun threadFactory(builder: ThreadFactoryBuilder.() -> Unit): ThreadFactory = ThreadFactoryBuilder().apply(builder).build()

inline fun threadFactory(
    nameFormat: String,
    builder: ThreadFactoryBuilder.() -> Unit = {}
): ThreadFactory = ThreadFactoryBuilder().setNameFormat(nameFormat).apply(builder).build()

fun ThreadFactoryBuilder.nameFormat(format: String): ThreadFactoryBuilder = setNameFormat(format)

fun ThreadFactoryBuilder.daemon(daemon: Boolean): ThreadFactoryBuilder = setDaemon(daemon)

fun ThreadFactoryBuilder.daemon(): ThreadFactoryBuilder = setDaemon(true)

fun ThreadFactoryBuilder.priority(priority: Int): ThreadFactoryBuilder = setPriority(priority)

fun ThreadFactoryBuilder.uncaughtExceptionHandler(handler: Thread.UncaughtExceptionHandler): ThreadFactoryBuilder =
    setUncaughtExceptionHandler(handler)

fun ThreadFactoryBuilder.factory(factory: ThreadFactory): ThreadFactoryBuilder = setThreadFactory(factory)

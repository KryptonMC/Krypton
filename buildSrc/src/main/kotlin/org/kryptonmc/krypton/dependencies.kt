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
package org.kryptonmc.krypton

import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.kotlinx(module: String, version: String) = "org.jetbrains.kotlinx:kotlinx-$module:$version"

fun DependencyHandler.adventure(module: String) = "net.kyori:adventure-$module:${Versions.ADVENTURE}"

fun DependencyHandler.netty(module: String) = "io.netty:netty-$module:${Versions.NETTY}"

fun DependencyHandler.log4j(module: String) = "org.apache.logging.log4j:log4j-$module:${Versions.LOG4J}"

fun DependencyHandler.junit(
    module: String,
    subModule: String,
    version: String = Versions.JUNIT
) = "org.junit.$module:junit-$module-$subModule:$version"

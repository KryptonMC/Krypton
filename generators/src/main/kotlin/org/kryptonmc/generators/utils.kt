/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.generators

import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile

fun Path.tryCreateDirectories(): Path = catchAndReturnSelf { createDirectories() }

fun Path.tryCreateFile(): Path = catchAndReturnSelf { createFile() }

private fun <T> T.catchAndReturnSelf(action: () -> Unit): T = try {
    action()
    this
} catch (exception: Exception) {
    this
}

/**
 * Kotlinpoet likes to enforce certain restrictions when you use it to generate
 * code.
 *
 * Square believes that these are all conventional, and should be used by
 * everyone, however this angers me a little, since I want code to look how I
 * would write it manually, so that it is consistent with the rest of the
 * project.
 *
 * To get around the styling that Square enforces, I have created this
 * function, which is specifically designed to correct all of the styling that
 * is wrong.
 */
fun String.performReplacements(type: String, name: String): String =
    replace("@JvmField\n {4}public val (.*): $type =(\n {12})?(.*)(\n)?".toRegex(), "@JvmField public val $1: $type = $3")
        .replace("=  ", "= ")
        .replace("public object $name {\n", "public object $name {\n\n    // @formatter:off\n")
        .replace("\n    @JvmStatic", "\n\n    // @formatter:on\n    @JvmStatic")
        .replace("`", "")
        .replace("(.*)get(.*) =\n( )+(.*)".toRegex(), "$1get$2 = $4")
        .replace("import kotlin.String\n", "")
        .replace("import kotlin.Suppress\n", "")
        .replace("import kotlin.jvm.JvmField\n", "")
        .replace("import kotlin.jvm.JvmStatic\n", "")

/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.generators

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile

fun Path.tryCreateDirectories(): Path = catchAndReturnSelf { createDirectories() }

fun Path.tryCreateFile(): Path = catchAndReturnSelf { createFile() }

private inline fun <T> T.catchAndReturnSelf(action: () -> Unit): T = try {
    action()
    this
} catch (_: Exception) {
    this
}

fun Class<*>.staticFields(): Sequence<Field> = declaredFields.asSequence().filter { Modifier.isStatic(it.modifiers) }

fun <T> Class<*>.staticConstantFields(type: Class<T>): Sequence<Field> = staticFields().filter { type.isAssignableFrom(it.type) }

fun <T> Class<*>.collectFields(type: Class<T>): Sequence<CollectedField<T>> = staticConstantFields(type).map { CollectedField(it, type) }

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
    replace("@JvmField\n {4}public val (.*): RegistryReference<$type> =(\n {12})?(.*)(\n)?".toRegex(),
        "@JvmField\n    public val $1: RegistryReference<$type> = $3")
        .replace("=  ", "= ")
        .replace("public object $name {\n", "public object $name {\n\n    // @formatter:off\n")
        .replace("\n    @JvmStatic", "\n\n    // @formatter:on\n    @JvmStatic")
        .replace("`", "")
        .replace("(.*)of(.*) =\n( )+(.*)".toRegex(), "$1of$2 = $4")
        .replace("import kotlin.String\n", "")
        .replace("import kotlin.Suppress\n", "")
        .replace("import kotlin.jvm.JvmField\n", "")
        .replace("import kotlin.jvm.JvmStatic\n", "")

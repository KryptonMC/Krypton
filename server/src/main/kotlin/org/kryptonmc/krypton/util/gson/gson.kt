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
package org.kryptonmc.krypton.util.gson

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.io.path.reader
import kotlin.io.path.writer

fun Path.jsonReader(): JsonReader = JsonReader(reader())

fun Path.jsonWriter(): JsonWriter = JsonWriter(writer())

inline fun <T, C : MutableCollection<T>> JsonReader.readListTo(result: C, block: JsonReader.() -> T?): C {
    beginArray()
    while (hasNext()) {
        block()?.let(result::add)
    }
    endArray()
    return result
}

inline fun <T> JsonReader.readPersistentList(block: JsonReader.() -> T?): PersistentList<T> =
    readListTo(persistentListOf<T>().builder(), block).build()

inline fun JsonWriter.array(block: JsonWriter.() -> Unit) {
    beginArray()
    block()
    endArray()
}

inline fun <T> JsonWriter.array(iterable: Iterable<T>, block: JsonWriter.(T) -> Unit) {
    array { iterable.forEach { block(it) } }
}

inline fun <T> JsonWriter.array(stream: Stream<T>, crossinline block: JsonWriter.(T) -> Unit) {
    array { stream.forEach { block(it) } }
}

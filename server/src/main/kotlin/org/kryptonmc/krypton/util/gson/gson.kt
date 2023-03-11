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

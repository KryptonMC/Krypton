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
package org.kryptonmc.processor

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

@JvmRecord
data class SerializedPluginDescription(
    val id: String,
    val name: String,
    val version: String,
    val description: String,
    val authors: List<String>,
    val dependencies: List<SerializedDependency>,
    val main: String
) {

    init {
        require(id matches ID_REGEX) { "ID is invalid! Should match $ID_REGEX, was $id" }
    }

    companion object : TypeAdapter<SerializedPluginDescription>() {

        @JvmField
        val ID_REGEX: Regex = "[a-z][a-z0-9-_]{0,63}".toRegex()

        override fun read(reader: JsonReader): SerializedPluginDescription? {
            reader.beginObject()

            var id: String? = null
            var name = ""
            var version = "<UNDEFINED>"
            var description = ""
            val authors = mutableListOf<String>()
            val dependencies = mutableListOf<SerializedDependency>()
            var main: String? = null
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "id" -> id = reader.nextString()
                    "name" -> name = reader.nextString()
                    "version" -> version = reader.nextString()
                    "description" -> description = reader.nextString()
                    "authors" -> {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            authors.add(reader.nextString())
                        }
                        reader.endArray()
                    }
                    "dependencies" -> {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            val dependency = SerializedDependency.read(reader)
                            if (dependency != null) dependencies.add(dependency)
                        }
                        reader.endArray()
                    }
                    "main" -> main = reader.nextString()
                }
            }

            reader.endObject()
            if (id == null || main == null) return null
            return SerializedPluginDescription(id, name, version, description, authors, dependencies, main)
        }

        override fun write(writer: JsonWriter, value: SerializedPluginDescription) {
            writer.beginObject()
            writer.name("id")
            writer.value(value.id)

            if (value.name.isNotEmpty()) {
                writer.name("name")
                writer.value(value.name)
            }
            if (value.version.isNotEmpty()) {
                writer.name("version")
                writer.value(value.version)
            }
            if (value.description.isNotEmpty()) {
                writer.name("description")
                writer.value(value.description)
            }

            if (value.authors.isNotEmpty()) {
                writer.name("authors")
                writer.beginArray()
                value.authors.forEach { writer.value(it) }
                writer.endArray()
            }

            if (value.dependencies.isNotEmpty()) {
                writer.name("dependencies")
                writer.beginArray()
                value.dependencies.forEach { SerializedDependency.write(writer, it) }
                writer.endArray()
            }

            writer.name("main")
            writer.value(value.main)
            writer.endObject()
        }
    }
}

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
data class SerializedDependency(val id: String, val optional: Boolean) {

    companion object : TypeAdapter<SerializedDependency>() {

        override fun read(reader: JsonReader): SerializedDependency? {
            reader.beginObject()

            var id: String? = null
            var optional = false
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "id" -> id = reader.nextString()
                    "optional" -> optional = reader.nextBoolean()
                }
            }

            reader.endObject()
            if (id == null) return null
            return SerializedDependency(id, optional)
        }

        override fun write(writer: JsonWriter, value: SerializedDependency) {
            writer.beginObject()
            writer.name("id")
            writer.value(value.id)
            writer.name("optional")
            writer.value(value.optional)
            writer.endObject()
        }
    }
}

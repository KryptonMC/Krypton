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

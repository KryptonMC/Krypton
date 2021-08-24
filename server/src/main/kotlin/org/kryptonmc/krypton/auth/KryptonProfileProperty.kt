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
package org.kryptonmc.krypton.auth

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.kryptonmc.api.auth.ProfileProperty

data class KryptonProfileProperty(
    override val name: String,
    override val value: String,
    override val signature: String?
) : ProfileProperty {

    companion object : TypeAdapter<KryptonProfileProperty>() {

        override fun read(reader: JsonReader): KryptonProfileProperty? {
            reader.beginObject()

            var name: String? = null
            var value: String? = null
            var signature: String? = null
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "name" -> name = reader.nextString()
                    "value" -> value = reader.nextString()
                    "signature" -> signature = reader.nextString()
                }
            }

            reader.endObject()
            if (name == null || value == null) return null
            return KryptonProfileProperty(name, value, signature)
        }

        override fun write(writer: JsonWriter, value: KryptonProfileProperty) {
            writer.beginObject()
            writer.name("name")
            writer.value(value.name)
            writer.name("value")
            writer.value(value.value)

            if (value.signature != null) {
                writer.name("signature")
                writer.value(value.signature)
            }
            writer.endObject()
        }
    }
}

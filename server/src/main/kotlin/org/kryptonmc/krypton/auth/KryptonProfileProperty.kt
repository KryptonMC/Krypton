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
package org.kryptonmc.krypton.auth

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.auth.ProfileProperty
import java.io.StringReader

@JvmRecord
data class KryptonProfileProperty(
    override val name: String,
    override val value: String,
    override val signature: String?
) : ProfileProperty {

    override fun with(builder: ProfileProperty.Builder.() -> Unit): ProfileProperty = Builder(this).apply(builder).build()

    override fun withName(name: String): ProfileProperty = copy(name = name)

    override fun withValue(value: String): ProfileProperty = copy(value = value)

    override fun withSignature(signature: String?): ProfileProperty = copy(signature = signature)

    override fun toBuilder(): ProfileProperty.Builder = Builder(this)

    class Builder(property: ProfileProperty) : ProfileProperty.Builder {

        private var name = property.name
        private var value = property.value
        private var signature = property.signature

        override fun name(name: String): ProfileProperty.Builder = apply { this.name = name }

        override fun value(value: String): ProfileProperty.Builder = apply { this.value = value }

        override fun signature(signature: String?): ProfileProperty.Builder = apply { this.signature = signature }

        override fun build(): ProfileProperty = KryptonProfileProperty(name, value, signature)
    }

    object Factory : ProfileProperty.Factory {

        override fun of(name: String, value: String, signature: String?): ProfileProperty = KryptonProfileProperty(name, value, signature)
    }

    companion object : TypeAdapter<ProfileProperty>() {

        @JvmStatic
        fun fromJsonList(json: String): PersistentList<KryptonProfileProperty> {
            val reader = JsonReader(StringReader(json))
            return readList(reader)
        }

        @JvmStatic
        fun readList(reader: JsonReader): PersistentList<KryptonProfileProperty> {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return persistentListOf()
            }

            val builder = persistentListOf<KryptonProfileProperty>().builder()
            reader.beginArray()
            while (reader.hasNext()) {
                val instance = read(reader)
                if (instance != null) builder.add(instance)
            }
            reader.endArray()
            return builder.build()
        }

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
            if (name == null || value == null) return null // We can't complete a profile without these
            return KryptonProfileProperty(name, value, signature)
        }

        override fun write(writer: JsonWriter, value: ProfileProperty) {
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

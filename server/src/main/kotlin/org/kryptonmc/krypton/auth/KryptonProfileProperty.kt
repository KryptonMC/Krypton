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
package org.kryptonmc.krypton.auth

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.krypton.util.gson.readPersistentList
import java.io.StringReader

@JvmRecord
data class KryptonProfileProperty(override val name: String, override val value: String, override val signature: String?) : ProfileProperty {

    override fun withSignature(signature: String?): ProfileProperty = copy(signature = signature)

    override fun withoutSignature(): ProfileProperty = copy(signature = null)

    object Factory : ProfileProperty.Factory {

        override fun of(name: String, value: String, signature: String?): ProfileProperty = KryptonProfileProperty(name, value, signature)
    }

    object Adapter : TypeAdapter<ProfileProperty>() {

        @JvmStatic
        fun readJsonList(json: String): PersistentList<ProfileProperty> = readList(JsonReader(StringReader(json)))

        @JvmStatic
        fun readList(reader: JsonReader): PersistentList<ProfileProperty> {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return persistentListOf()
            }
            return reader.readPersistentList(::read)
        }

        override fun read(reader: JsonReader): ProfileProperty? {
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

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
package org.kryptonmc.plugins.whitelist.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.krypton.auth.KryptonGameProfile
import java.io.IOException
import java.util.UUID

object GameProfileAdapter : TypeAdapter<GameProfile>() {

    override fun read(reader: JsonReader): GameProfile {
        reader.beginObject()

        var uuid: UUID? = null
        var name: String? = null
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "uuid" -> uuid = UUID.fromString(reader.nextString())
                "name" -> name = reader.nextString()
            }
        }

        reader.endObject()
        if (uuid == null || name == null) {
            throw IOException("Invalid profile in whitelist file! Name or UUID was null! UUID: $uuid, Name: $name")
        }
        return KryptonGameProfile.basic(uuid, name)
    }

    override fun write(writer: JsonWriter, value: GameProfile) {
        writer.beginObject()
        writer.name("uuid").value(value.uuid.toString())
        writer.name("name").value(value.name)
        writer.endObject()
    }
}

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
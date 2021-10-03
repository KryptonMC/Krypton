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
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.krypton.util.MojangUUIDTypeAdapter
import java.util.UUID

@JvmRecord
data class KryptonGameProfile(
    override val uuid: UUID,
    override val name: String,
    override val properties: List<ProfileProperty>
) : GameProfile {

    override fun toString() = "GameProfile(name=$name,uuid=$uuid)"

    object Factory : GameProfile.Factory {

        override fun of(name: String, uuid: UUID, properties: List<ProfileProperty>): GameProfile = KryptonGameProfile(uuid, name, properties)
    }

    companion object : TypeAdapter<KryptonGameProfile>() {

        override fun read(reader: JsonReader): KryptonGameProfile? {
            reader.beginObject()

            var uuid: UUID? = null
            var name: String? = null
            val properties = mutableListOf<KryptonProfileProperty>()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "id" -> uuid = MojangUUIDTypeAdapter.read(reader)
                    "name" -> name = reader.nextString()
                    "properties" -> {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            val property = KryptonProfileProperty.read(reader)
                            if (property != null) properties.add(property)
                        }
                        reader.endArray()
                    }
                }
            }

            reader.endObject()
            if (uuid == null || name == null) return null
            return KryptonGameProfile(uuid, name, properties)
        }

        override fun write(writer: JsonWriter, value: KryptonGameProfile) {
            writer.beginObject()
            writer.name("id")
            MojangUUIDTypeAdapter.write(writer, value.uuid)
            writer.name("name")
            writer.value(value.name)

            if (value.properties.isNotEmpty()) {
                writer.name("properties")
                writer.beginArray()
                value.properties.forEach { KryptonProfileProperty.write(writer, it) }
                writer.endArray()
            }
            writer.endObject()
        }
    }
}

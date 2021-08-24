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
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID

class ProfileHolder(
    val profile: KryptonGameProfile,
    val expiryDate: ZonedDateTime,
    @Volatile var lastAccess: Long = 0L
) {

    object Adapter : TypeAdapter<ProfileHolder>() {

        override fun read(reader: JsonReader): ProfileHolder? {
            reader.beginObject()

            var name: String? = null
            var uuid: UUID? = null
            var expiryDate: ZonedDateTime? = null
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "name" -> name = reader.nextString()
                    "uuid" -> uuid = UUID.fromString(reader.nextString())
                    "expiresOn" -> expiryDate = try {
                        ZonedDateTime.parse(reader.nextString(), DATE_FORMATTER)
                    } catch (ignored: DateTimeParseException) {
                        null
                    }
                }
            }

            reader.endObject()
            if (name == null || uuid == null || expiryDate == null) return null
            return ProfileHolder(KryptonGameProfile(uuid, name, emptyList()), expiryDate)
        }

        override fun write(writer: JsonWriter, value: ProfileHolder) {
            writer.beginObject()
            writer.name("name")
            writer.value(value.profile.name)
            writer.name("uuid")
            writer.value(value.profile.uuid.toString())
            writer.name("expiresOn")
            writer.value(DATE_FORMATTER.format(value.expiryDate))
            writer.endObject()
        }
    }

    companion object {

        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
    }
}

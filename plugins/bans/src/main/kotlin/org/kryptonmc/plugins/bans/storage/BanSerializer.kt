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
package org.kryptonmc.plugins.bans.storage

import com.google.gson.JsonParseException
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.plugins.bans.api.Ban
import org.kryptonmc.plugins.bans.api.IpBan
import org.kryptonmc.plugins.bans.api.ProfileBan
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID

class BanSerializer {

    fun readProfileBan(reader: JsonReader): ProfileBan {
        reader.beginObject()

        var uuid: UUID? = null
        var name: String? = null
        var source: String? = null
        var reason: String? = null
        var created: OffsetDateTime? = null
        var expires: OffsetDateTime? = null

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "uuid" -> uuid = UUID.fromString(reader.nextString())
                "name" -> name = reader.nextString()
                "source" -> source = reader.nextString()
                "reason" -> reason = reader.nextString()
                "created" -> created = readDate(reader)
                "expires" -> expires = readDate(reader)
            }
        }

        reader.endObject()
        if (uuid == null || name == null) throw JsonParseException("Missing uuid or name in profile ban!")
        return ProfileBan(GameProfile.of(name, uuid), source, reason, created, expires)
    }

    fun readIpBan(reader: JsonReader): IpBan {
        reader.beginObject()

        var ip: String? = null
        var source: String? = null
        var reason: String? = null
        var created: OffsetDateTime? = null
        var expires: OffsetDateTime? = null

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "ip" -> ip = reader.nextString()
                "source" -> source = reader.nextString()
                "reason" -> reason = reader.nextString()
                "created" -> created = readDate(reader)
                "expires" -> expires = readDate(reader)
            }
        }

        reader.endObject()
        if (ip == null) throw JsonParseException("Missing uuid or name in profile ban!")
        return IpBan(ip, source, reason, created, expires)
    }

    private fun readDate(reader: JsonReader): OffsetDateTime? {
        return try {
            OffsetDateTime.parse(reader.nextString(), DATE_FORMATTER)
        } catch (_: DateTimeParseException) {
            null
        }
    }

    fun writeProfileBan(writer: JsonWriter, ban: ProfileBan) {
        writer.beginObject()
        writer.name("uuid").value(ban.profile.uuid.toString())
        writer.name("name").value(ban.profile.name)
        writeBan(writer, ban)
        writer.endObject()
    }

    fun writeIpBan(writer: JsonWriter, ban: IpBan) {
        writer.beginObject()
        writer.name("ip").value(ban.ip)
        writeBan(writer, ban)
        writer.endObject()
    }

    private fun writeBan(writer: JsonWriter, ban: Ban) {
        writer.name("source").value(ban.source)
        writer.name("reason").value(ban.reason)
        writer.name("created").value(ban.creationDate.format(DATE_FORMATTER))
        if (ban.expirationDate != null) {
            writer.name("expires").value(ban.expirationDate.format(DATE_FORMATTER))
        }
    }

    companion object {

        @JvmField
        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
    }
}

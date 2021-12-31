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
package org.kryptonmc.krypton.server.ban

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.collections.immutable.persistentListOf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.user.ban.Ban
import org.kryptonmc.api.user.ban.BanType
import org.kryptonmc.api.user.ban.BanTypes
import org.kryptonmc.krypton.auth.KryptonGameProfile
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import java.util.UUID

class BannedPlayerEntry(
    override val profile: GameProfile,
    creationDate: OffsetDateTime = OffsetDateTime.now(),
    source: Component = DEFAULT_SOURCE,
    expiryDate: OffsetDateTime? = null,
    reason: Component = DEFAULT_REASON
) : BanEntry<GameProfile>(profile, creationDate, source, expiryDate, reason), Ban.Profile {

    override val type: BanType = BanTypes.PROFILE

    override fun writeKey(writer: JsonWriter) {
        writer.name("uuid")
        writer.value(key.uuid.toString())
        writer.name("name")
        writer.value(key.name)
    }

    companion object {

        @JvmStatic
        fun read(reader: JsonReader): BannedPlayerEntry? {
            reader.beginObject()

            var name: String? = null
            var uuid: UUID? = null
            var creationDate: OffsetDateTime = OffsetDateTime.now()
            var source: Component = DEFAULT_SOURCE
            var expires: OffsetDateTime? = null
            var reason: Component = DEFAULT_REASON

            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "name" -> name = reader.nextString()
                    "uuid" -> uuid = UUID.fromString(reader.nextString())
                    "created" -> creationDate = try {
                        OffsetDateTime.parse(reader.nextString(), DATE_FORMATTER)
                    } catch (_: DateTimeParseException) {
                        OffsetDateTime.now()
                    }
                    "source" -> source = LegacyComponentSerializer.legacySection().deserialize(reader.nextString())
                    "expires" -> expires = try {
                        OffsetDateTime.parse(reader.nextString(), DATE_FORMATTER)
                    } catch (_: DateTimeParseException) {
                        null
                    }
                    "reason" -> reason = LegacyComponentSerializer.legacySection().deserialize(reader.nextString())
                }
            }

            reader.endObject()
            if (name == null || uuid == null) return null
            return BannedPlayerEntry(KryptonGameProfile(name, uuid, persistentListOf()), creationDate, source, expires, reason)
        }
    }
}

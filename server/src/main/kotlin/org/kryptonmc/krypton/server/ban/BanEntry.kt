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

import com.google.gson.JsonObject
import org.kryptonmc.krypton.server.ServerConfigEntry
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

open class BanEntry<T>(
    key: T,
    val creationDate: OffsetDateTime = OffsetDateTime.now(),
    val source: String = "(Unknown)",
    val expiryDate: OffsetDateTime? = null,
    val reason: String = "Banned by operator."
) : ServerConfigEntry<T>(key) {


    override fun isInvalid(): Boolean {
        return expiryDate?.isBefore(OffsetDateTime.now()) ?: false
    }

    override fun writeToJson(data: JsonObject) {
        data.addProperty("created", creationDate.format(DATE_FORMAT))
        data.addProperty("source", source)
        data.addProperty("expires", expiryDate?.format(DATE_FORMAT) ?: "forever")
        data.addProperty("reason", reason)
    }

    companion object {

        internal val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")

        internal fun <T> fromJson(key: T, data: JsonObject): BanEntry<T> {
            val creationDate = try {
                if (data.has("created")) DATE_FORMAT.parse(data.get("created").asString) else LocalDateTime.now()
            } catch (_: DateTimeParseException) {
                LocalDateTime.now()
            }
            val source = if (data.has("source")) data.get("source").asString else "(Unknown)"
            val expires = try {
                if (data.has("expires")) DATE_FORMAT.parse(data.get("expires").asString) else null
            } catch (_: DateTimeParseException) {
                null
            }
            val reason = if (data.has("reason")) data.get("reason").asString else "Banned by operator."
            return BanEntry(
                key,
                OffsetDateTime.from(creationDate),
                source,
                if (expires == null) null else OffsetDateTime.from(expires),
                reason
            )
        }

    }

}

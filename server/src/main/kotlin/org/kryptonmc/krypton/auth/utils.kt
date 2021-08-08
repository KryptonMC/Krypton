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

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import java.util.UUID

fun JsonElement.toProfileHolder(): ProfileHolder? {
    if (!isJsonObject) return null
    this as JsonObject
    val name = get("name")?.asString ?: return null
    val uuid = get("uuid")?.let { UUID.fromString(it.asString) } ?: return null
    val expiryDate = get("expiresOn")?.let {
        try {
            ZonedDateTime.parse(it.asString, ProfileHolder.DATE_FORMATTER)
        } catch (ignored: DateTimeParseException) {
            return null
        }
    } ?: return null
    return ProfileHolder(KryptonGameProfile(uuid, name, emptyList()), expiryDate)
}

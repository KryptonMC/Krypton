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
import org.kryptonmc.krypton.auth.GameProfile
import java.time.OffsetDateTime
import java.util.UUID

class BannedPlayerEntry(
    profile: GameProfile,
    creationDate: OffsetDateTime = OffsetDateTime.now(),
    source: String = "(Unknown)",
    expiryDate: OffsetDateTime? = null,
    reason: String = "Banned by operator."
) : BanEntry<GameProfile>(profile, creationDate, source, expiryDate, reason) {

    override fun writeToJson(data: JsonObject) {
        data.addProperty("uuid", key.uuid.toString())
        data.addProperty("name", key.name)
        super.writeToJson(data)
    }

    override fun toString(): String {
        return "BannedPlayerEntry(name=${key.name}, creationDate=${creationDate}, source=${source}, expires=${expiryDate}, reason=$reason)"
    }

    companion object {

        internal fun fromJson(data: JsonObject): BannedPlayerEntry {
            val entry = BanEntry.fromJson(data.toGameProfile(), data) //Get better null checking
            return BannedPlayerEntry(entry.key!!, entry.creationDate, entry.source, entry.expiryDate, entry.reason)
        }

        private fun JsonObject.toGameProfile(): GameProfile? {
            if (has("name") && has("uuid")) {
                val name = get("name").asString
                val uuid = try {
                    UUID.fromString(get("uuid").asString)
                } catch (_: IllegalArgumentException) {
                    return null
                }
                return GameProfile(uuid, name, listOf())
            }
            return null
        }

    }

}

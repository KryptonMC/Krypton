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
package org.kryptonmc.krypton.server

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.bardy.gsonkt.fromJson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.util.logger
import java.nio.file.Path
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.io.path.bufferedReader
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.writeText

class UserCache(private val path: Path) {

    private val client = OkHttpClient()
    private val request: (String) -> Request = {
        Request.Builder()
            .url("https://api.mojang.com/users/profiles/minecraft/$it")
            .get()
            .build()
    }
    private val gson = Gson()
    private val entries = mutableListOf<Entry>()
    private val LOGGER = logger<UserCache>()

    fun getProfileByName(name: String): GameProfile? {
        val entry = entries.firstOrNull { it.profile.name == name }
        return entry?.profile ?: findProfileByName(name)
    }

    fun getProfileByUUID(uuid: UUID): GameProfile? {
        val entry = entries.firstOrNull { it.profile.uuid == uuid }
        return entry?.profile
    }

    private fun findProfileByName(name: String): GameProfile? {
        val result = client.newCall(request(name)).execute().body!!.string()
        val data = gson.fromJson<JsonObject>(result) ?: return null
        if (data.has("error")) return null
        val gameProfile = GameProfile(
            UUID.fromString(
                StringBuilder(data.get("id").asString)
                    .insert(20, '-')
                    .insert(16, '-')
                    .insert(12, '-')
                    .insert(8, '-')
                    .toString()
            ), name, listOf()
        )
        entries.add(Entry(gameProfile, OffsetDateTime.now().plus(1, ChronoUnit.MONTHS)))
        save()
        return gameProfile
    }

    fun add(profile: GameProfile, expiryDate: OffsetDateTime = OffsetDateTime.now().plus(1, ChronoUnit.MONTHS)) {
        if (entries.firstOrNull { it.profile == profile } == null) entries.add(Entry(profile, expiryDate))
        save()
    }

    fun remove(entry: Entry) {
        entries.remove(entry)
        save()
    }

    fun load() {
        entries.clear()
        path.bufferedReader().use {
            val array = gson.fromJson<JsonArray>(it)
            for (jsonElement in array) {
                val data = jsonElement as JsonObject
                val entry = Entry.fromJson(data)
                if (entry == null) {
                    LOGGER.error("Couldn't parse user cache entry: $jsonElement")
                } else {
                    entries.add(entry)
                }
            }
            entries.sortBy { entry ->
                entry.expiryDate
            }
        }
    }

    fun save() {
        val profiles = JsonArray()

        for (entry in entries.take(1000)) {
            val data = JsonObject()
            entry.writeToJson(data)
            profiles.add(data)
        }

        path.bufferedWriter().use {
            gson.toJson(profiles, it)
        }
    }

    data class Entry(val profile: GameProfile, val expiryDate: OffsetDateTime = OffsetDateTime.now()) {

        internal fun writeToJson(data: JsonObject) {
            data.addProperty("name", profile.name)
            data.addProperty("uuid", profile.uuid.toString())
            data.addProperty("expiresOn", DATE_FORMAT.format(expiryDate))
        }

        companion object {

            fun fromJson(data: JsonObject): Entry? {
                val nameElement = data.get("name")?.asString ?: return null
                val uuidElement = data.get("uuid")?.asString ?: return null
                val uuid = UUID.fromString(uuidElement)
                val expiredDateElement = data.get("expiresOn")?.asString
                val expiredDate = OffsetDateTime.from(
                    if (expiredDateElement != null) DATE_FORMAT.parse(expiredDateElement) else OffsetDateTime.now()
                        .plus(1, ChronoUnit.MONTHS)
                )
                return Entry(GameProfile(uuid, nameElement, listOf()), expiredDate)
            }

        }

    }

    fun validatePath() {
        if (!path.exists()) {
            path.createFile()
            path.writeText("[]")
        } else {
            load()
        }
    }

    companion object {

        val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
    }

}

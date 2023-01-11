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
import org.apache.logging.log4j.Logger
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.plugins.bans.api.IpBan
import org.kryptonmc.plugins.bans.api.ProfileBan
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class BanStorage(private val path: Path, private val logger: Logger) {

    private val profileBans = HashMap<GameProfile, ProfileBan>()
    private val ipBans = HashMap<String, IpBan>()

    private val serializer = BanSerializer()
    // Used to avoid saving changes unless they are needed
    private var dirty = false

    private fun markDirty() {
        dirty = true
    }

    fun load() {
        if (!Files.exists(path)) return
        try {
            JsonReader(InputStreamReader(Files.newInputStream(path, StandardOpenOption.READ), Charsets.UTF_8)).use { loadData(it) }
        } catch (exception: IOException) {
            logger.warn("Failed to read bans file $path. You can delete it to force it to be recreated.", exception)
        } catch (exception: JsonParseException) {
            logger.warn("Failed to parse bans file $path. You can delete it to force it to be recreated.", exception)
        }
    }

    private fun loadData(reader: JsonReader) {
        if (!reader.hasNext()) return // File is empty. Just use defaults (not enabled, no whitelisted profiles or IPs)
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "profiles" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        val ban = serializer.readProfileBan(reader)
                        profileBans.put(ban.profile, ban)
                    }
                    reader.endArray()
                }
                "ips" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        val ban = serializer.readIpBan(reader)
                        ipBans.put(ban.ip, ban)
                    }
                    reader.endArray()
                }
            }
        }
        reader.endObject()
    }

    fun saveIfNeeded() {
        if (dirty) {
            save()
            dirty = false
        }
    }

    private fun save() {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path)
            } catch (exception: IOException) {
                logger.warn("Failed to create bans file $path. Bans will not be saved.", exception)
                return
            }
        }
        try {
            JsonWriter(OutputStreamWriter(Files.newOutputStream(path), Charsets.UTF_8)).use { saveData(it) }
        } catch (exception: IOException) {
            logger.warn("Failed to save bans file $path. Bans will not be saved.", exception)
        }
    }

    private fun saveData(writer: JsonWriter) {
        writer.beginObject()
        writer.name("profiles")
        run {
            writer.beginArray()
            profileBans.values.forEach { serializer.writeProfileBan(writer, it) }
            writer.endArray()
        }
        writer.name("ips")
        run {
            writer.beginArray()
            ipBans.values.forEach { serializer.writeIpBan(writer, it) }
            writer.endArray()
        }
        writer.endObject()
    }

    fun isBanned(profile: GameProfile): Boolean = profileBans.containsKey(profile)

    fun isBanned(ip: String): Boolean = ipBans.containsKey(ip)

    fun getBan(profile: GameProfile): ProfileBan? = profileBans.get(profile)

    fun getBan(ip: String): IpBan? = ipBans.get(ip)

    fun addBan(ban: ProfileBan) {
        profileBans.put(ban.profile, ban)
        markDirty()
    }

    fun addBan(ban: IpBan) {
        ipBans.put(ban.ip, ban)
        markDirty()
    }

    fun removeBan(profile: GameProfile) {
        profileBans.remove(profile)
        markDirty()
    }

    fun removeBan(ip: String) {
        ipBans.remove(ip)
        markDirty()
    }

    fun profileBans(): Collection<ProfileBan> = profileBans.values

    fun ipBans(): Collection<IpBan> = ipBans.values
}

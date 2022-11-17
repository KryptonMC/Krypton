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
package org.kryptonmc.krypton.server.ban

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.krypton.server.PersistentManager
import org.kryptonmc.krypton.util.array
import java.nio.file.Path

class BanManager(path: Path) : PersistentManager(path) {

    private val profiles = HashMap<GameProfile, KryptonProfileBan>()
    private val ips = HashMap<String, KryptonIpBan>()

    fun isBanned(profile: GameProfile): Boolean = profiles.containsKey(profile)

    fun isBanned(ip: String): Boolean = ips.containsKey(ip)

    fun get(profile: GameProfile): KryptonProfileBan? = profiles.get(profile)

    fun get(ip: String): KryptonIpBan? = ips.get(ip)

    fun add(ban: KryptonProfileBan) {
        if (isBanned(ban.profile)) return
        profiles.put(ban.profile, ban)
    }

    fun add(ban: KryptonIpBan) {
        if (isBanned(ban.ip)) return
        ips.put(ban.ip, ban)
    }

    fun remove(profile: GameProfile) {
        if (!isBanned(profile)) return
        profiles.remove(profile)
    }

    fun remove(ip: String) {
        if (!isBanned(ip)) return
        ips.remove(ip)
    }

    fun profiles(): Collection<KryptonProfileBan> = profiles.values

    fun ips(): Collection<KryptonIpBan> = ips.values

    override fun loadData(reader: JsonReader) {
        if (!reader.hasNext()) return // File is empty. Just use defaults (not enabled, no whitelisted profiles or IPs)
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "profiles" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        val ban = KryptonProfileBan.read(reader) ?: continue
                        profiles.put(ban.profile, ban)
                    }
                    reader.endArray()
                }
                "ips" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        val ban = KryptonIpBan.read(reader) ?: continue
                        ips.put(ban.ip, ban)
                    }
                    reader.endArray()
                }
            }
        }
        reader.endObject()
    }

    override fun saveData(writer: JsonWriter) {
        writer.beginObject()
        writer.name("profiles")
        writer.array { profiles.values.forEach { it.write(writer) } }
        writer.name("ips")
        writer.array { ips.values.forEach { it.write(writer) } }
        writer.endObject()
    }
}

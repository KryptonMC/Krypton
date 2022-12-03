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
package org.kryptonmc.krypton.server.whitelist

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.server.PersistentManager
import org.kryptonmc.krypton.util.array
import java.io.IOException
import java.nio.file.Path
import java.util.UUID

class WhitelistManager(path: Path) : PersistentManager(path) {

    var isEnabled: Boolean = false
    private val profiles = ArrayList<GameProfile>()
    private val ips = ArrayList<String>()

    fun isWhitelisted(profile: GameProfile): Boolean = profiles.contains(profile)

    fun isWhitelisted(ip: String): Boolean = ips.contains(ip)

    fun add(profile: GameProfile) {
        if (isWhitelisted(profile)) return
        profiles.add(profile)
        markDirty()
    }

    fun add(ip: String) {
        if (isWhitelisted(ip)) return
        ips.add(ip)
        markDirty()
    }

    fun remove(profile: GameProfile) {
        if (!isWhitelisted(profile)) return
        profiles.remove(profile)
        markDirty()
    }

    fun remove(ip: String) {
        if (!isWhitelisted(ip)) return
        ips.remove(ip)
        markDirty()
    }

    fun profiles(): List<GameProfile> = profiles

    fun ips(): List<String> = ips

    override fun loadData(reader: JsonReader) {
        if (!reader.hasNext()) return // File is empty. Just use defaults (not enabled, no whitelisted profiles or IPs)
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "enabled" -> isEnabled = reader.nextBoolean()
                "profiles" -> readValues(reader, profiles, ProfileAdapter::read)
                "ips" -> readValues(reader, ips, JsonReader::nextString)
            }
        }
        reader.endObject()
    }

    override fun saveData(writer: JsonWriter) {
        writer.beginObject()
        writer.name("enabled")
        writer.value(isEnabled)
        writer.name("profiles")
        writer.array(profiles) { ProfileAdapter.write(this, it) }
        writer.name("ips")
        writer.array(ips, JsonWriter::value)
        writer.endObject()
    }

    private object ProfileAdapter : TypeAdapter<GameProfile>() {

        override fun read(reader: JsonReader): GameProfile {
            reader.beginObject()

            var name: String? = null
            var uuid: UUID? = null
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "name" -> name = reader.nextString()
                    "uuid" -> uuid = UUID.fromString(reader.nextString())
                }
            }

            reader.endObject()
            if (name == null || uuid == null) {
                throw IOException("Invalid profile in whitelist file! Name or UUID was null! Name: $name, UUID: $uuid")
            }
            return KryptonGameProfile(name, uuid, persistentListOf())
        }

        override fun write(writer: JsonWriter, value: GameProfile) {
            writer.beginObject()
            writer.name("name").value(value.name)
            writer.name("uuid").value(value.uuid.toString())
            writer.endObject()
        }
    }

    companion object {

        // Would be JvmStatic but it seems that Kotlin decides to generate a bridge static method that uses invokespecial where it shouldn't.
        private inline fun <T> readValues(reader: JsonReader, result: MutableList<T>, readFunction: (JsonReader) -> T) {
            reader.beginArray()
            while (reader.hasNext()) {
                result.add(readFunction(reader))
            }
            reader.endArray()
        }
    }
}

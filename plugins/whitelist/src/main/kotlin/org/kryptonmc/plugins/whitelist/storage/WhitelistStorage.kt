/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.plugins.whitelist.storage

import com.google.common.collect.Collections2
import com.google.gson.JsonParseException
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.apache.logging.log4j.Logger
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.plugins.whitelist.util.GameProfileAdapter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.Collections

class WhitelistStorage(private val path: Path, private val logger: Logger) {

    private val profiles = ArrayList<GameProfile>()
    private val ips = ArrayList<String>()

    private var enabled = false
    // Used to avoid saving changes unless they are needed
    private var dirty = false

    private val profileNames = Collections.unmodifiableCollection(Collections2.transform(profiles) { it.name })

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
                "enabled" -> enabled = reader.nextBoolean()
                "profiles" -> readValues(reader, profiles, GameProfileAdapter::read)
                "ips" -> readValues(reader, ips, JsonReader::nextString)
            }
        }
        reader.endObject()
    }

    fun reload() {
        profiles.clear()
        ips.clear()
        load()
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
        writer.name("enabled")
        writer.value(enabled)
        writer.name("profiles")
        run {
            writer.beginArray()
            profiles.forEach { GameProfileAdapter.write(writer, it) }
            writer.endArray()
        }
        writer.name("ips")
        run {
            writer.beginArray()
            ips.forEach { writer.value(it) }
            writer.endArray()
        }
        writer.endObject()
    }

    fun isEnabled(): Boolean = enabled

    fun enable() {
        enabled = true
    }

    fun disable() {
        enabled = false
    }

    fun isWhitelisted(profile: GameProfile): Boolean = profiles.contains(profile)

    fun isWhitelisted(ip: String): Boolean = ips.contains(ip)

    fun whitelist(profile: GameProfile) {
        profiles.add(profile)
        markDirty()
    }

    fun whitelist(ip: String) {
        ips.add(ip)
        markDirty()
    }

    fun removeWhitelisted(profile: GameProfile) {
        profiles.remove(profile)
        markDirty()
    }

    fun removeWhitelisted(ip: String) {
        ips.remove(ip)
        markDirty()
    }

    fun whitelistedProfiles(): Collection<GameProfile> = profiles

    fun whitelistedIps(): Collection<String> = ips

    fun whitelistedProfileNames(): Collection<String> = profileNames

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

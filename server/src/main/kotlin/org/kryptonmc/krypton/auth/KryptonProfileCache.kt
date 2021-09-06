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

import com.google.gson.JsonParseException
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.coroutines.launch
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.auth.ProfileCache
import org.kryptonmc.krypton.IOScope
import org.kryptonmc.krypton.util.tryCreateFile
import org.kryptonmc.krypton.util.logger
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Path
import java.time.ZonedDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import kotlin.io.path.exists
import kotlin.io.path.reader
import kotlin.io.path.writer

class KryptonProfileCache(private val path: Path) : ProfileCache {

    private val profilesByName = ConcurrentHashMap<String, ProfileHolder>()
    private val profilesByUUID = ConcurrentHashMap<UUID, ProfileHolder>()
    private val operations = AtomicLong()
    override val profiles: Set<GameProfile>
        get() = profilesByUUID.values.mapTo(mutableSetOf()) {
            it.lastAccess = operations.incrementAndGet()
            it.profile
        }

    init {
        load().apply { reverse() }.forEach { safeAdd(it) }
    }

    fun add(profile: KryptonGameProfile) {
        val expiry = ZonedDateTime.now().plusMonths(1)
        safeAdd(ProfileHolder(profile, expiry))
        IOScope.launch { save() }
    }

    override fun get(name: String) = profilesByName[name.lowercase()]?.apply {
        lastAccess = operations.incrementAndGet()
    }?.profile

    override fun get(uuid: UUID) = profilesByUUID[uuid]?.apply { lastAccess = operations.incrementAndGet() }?.profile

    override fun iterator() = profilesByUUID.values.asSequence().map {
        it.lastAccess = operations.incrementAndGet()
        it.profile
    }.iterator()

    private fun safeAdd(holder: ProfileHolder) {
        val profile = holder.profile
        holder.lastAccess = operations.incrementAndGet()
        profilesByName[profile.name.lowercase()] = holder
        profilesByUUID[profile.uuid] = holder
    }

    private fun load(): MutableList<ProfileHolder> {
        val holders = mutableListOf<ProfileHolder>()
        if (!path.exists()) return holders
        try {
            JsonReader(path.reader()).use { reader ->
                reader.beginArray()
                while (reader.hasNext()) {
                    val holder = ProfileHolder.Adapter.read(reader)
                    if (holder != null) holders.add(holder)
                }
                reader.endArray()
            }
        } catch (ignored: FileNotFoundException) {
        } catch (exception: JsonParseException) {
            LOGGER.warn(
                "Failed to parse JSON data from $path. You can delete it to force the server to recreate it.",
                exception
            )
        } catch (exception: IOException) {
            LOGGER.warn(
                "Failed to read $path. You can delete it to force the server to recreate it.",
                exception
            )
        }
        return holders
    }

    private fun save() {
        path.tryCreateFile()
        try {
            JsonWriter(path.writer()).use { writer ->
                writer.beginArray()
                profilesByUUID.values.asSequence()
                    .sortedByDescending { it.lastAccess }
                    .take(MRU_LIMIT)
                    .forEach { ProfileHolder.Adapter.write(writer, it) }
                writer.endArray()
            }
        } catch (exception: IOException) {
            LOGGER.error("Error writing user cache file!", exception)
        }
    }

    companion object {

        private val LOGGER = logger<KryptonProfileCache>()
        private const val MRU_LIMIT = 1000
    }
}

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
package org.kryptonmc.krypton.auth

import com.google.common.collect.Lists
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.auth.ProfileCache
import org.kryptonmc.krypton.util.array
import org.kryptonmc.krypton.util.jsonReader
import org.kryptonmc.krypton.util.jsonWriter
import org.kryptonmc.krypton.util.readListTo
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.time.ZonedDateTime
import java.util.Collections
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import kotlin.io.path.exists

class KryptonProfileCache(private val path: Path) : ProfileCache {

    private val profilesByName = ConcurrentHashMap<String, ProfileHolder>()
    private val profilesByUUID = ConcurrentHashMap<UUID, ProfileHolder>()
    private val profileSet = ConcurrentHashMap.newKeySet<GameProfile>()
    private val operations = AtomicLong()
    private var dirty = false

    override val profiles: Collection<GameProfile>
        get() = Collections.unmodifiableCollection(profileSet)

    fun add(profile: GameProfile) {
        add(ProfileHolder(profile, ZonedDateTime.now().plusMonths(1)))
    }

    override fun get(name: String): GameProfile? = profilesByName.get(name)?.updateAndGetProfile(operations)

    override fun get(uuid: UUID): GameProfile? = profilesByUUID.get(uuid)?.updateAndGetProfile(operations)

    override fun iterator(): Iterator<GameProfile> = profiles.iterator()

    private fun add(holder: ProfileHolder) {
        val profile = holder.updateAndGetProfile(operations)
        profilesByName.put(profile.name, holder)
        profilesByUUID.put(profile.uuid, holder)
        profileSet.add(profile)
        markDirty()
    }

    fun loadAll() {
        if (!path.exists()) return
        val holders = ArrayList<ProfileHolder>()
        try {
            path.jsonReader().use { it.readListTo(holders, ProfileHolder::read) }
        } catch (exception: IOException) {
            LOGGER.warn("Failed to read $path. You can delete it to force the server to recreate it.", exception)
        } catch (exception: IllegalStateException) {
            LOGGER.warn("Failed to parse JSON data from $path. You can delete it to force the server to recreate it.", exception)
        }
        if (holders.isEmpty()) return
        Lists.reverse(holders).forEach(::add)
    }

    fun saveIfNeeded() {
        if (dirty) save()
        dirty = false
    }

    fun save() {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path)
            } catch (exception: IOException) {
                LOGGER.warn("Failed to create profile cache file $path.", exception)
                return
            }
        }
        try {
            path.jsonWriter().use { it.array(profilesByUUID.values.stream().sorted().limit(MRU_LIMIT), ProfileHolder::write) }
        } catch (exception: IOException) {
            LOGGER.error("Error writing user cache file!", exception)
        }
    }

    private fun markDirty() {
        dirty = true
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private const val MRU_LIMIT = 1000L
    }
}

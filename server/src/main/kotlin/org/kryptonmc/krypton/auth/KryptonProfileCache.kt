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

import com.google.common.collect.Collections2
import com.google.common.collect.Lists
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.auth.ProfileCache
import org.kryptonmc.krypton.util.gson.array
import org.kryptonmc.krypton.util.gson.jsonReader
import org.kryptonmc.krypton.util.gson.jsonWriter
import org.kryptonmc.krypton.util.gson.readListTo
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.time.ZonedDateTime
import java.util.Collections
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class KryptonProfileCache(private val path: Path) : ProfileCache {

    private val profilesByName = ConcurrentHashMap<String, ProfileHolder>()
    private val profilesByUUID = ConcurrentHashMap<UUID, ProfileHolder>()
    private val operations = AtomicLong()
    private var dirty = false

    override val profiles: Collection<GameProfile> =
        Collections.unmodifiableCollection(Collections2.transform(profilesByUUID.values) { it.profile })

    fun addProfile(profile: GameProfile) {
        addHolder(ProfileHolder(profile, ZonedDateTime.now().plusMonths(1)))
    }

    override fun getProfile(name: String): GameProfile? = updateAndGetProfile(profilesByName.get(name))

    override fun getProfile(uuid: UUID): GameProfile? = updateAndGetProfile(profilesByUUID.get(uuid))

    private fun updateAndGetProfile(holder: ProfileHolder?): GameProfile? {
        if (holder == null) return null
        holder.setLastAccess(operations.incrementAndGet())
        return holder.profile
    }

    override fun iterator(): Iterator<GameProfile> = profiles.iterator()

    private fun addHolder(holder: ProfileHolder) {
        holder.setLastAccess(operations.incrementAndGet())
        profilesByName.put(holder.profile.name, holder)
        profilesByUUID.put(holder.profile.uuid, holder)
        markDirty()
    }

    fun loadAll() {
        if (!Files.exists(path)) return
        val holders = ArrayList<ProfileHolder>()
        try {
            path.jsonReader().use { it.readListTo(holders, ProfileHolder.Adapter::read) }
        } catch (exception: IOException) {
            LOGGER.warn("Failed to read $path. You can delete it to force the server to recreate it.", exception)
        } catch (exception: IllegalStateException) {
            LOGGER.warn("Failed to parse JSON data from $path. You can delete it to force the server to recreate it.", exception)
        }
        if (holders.isEmpty()) return
        Lists.reverse(holders).forEach(::addHolder)
    }

    fun saveIfNeeded() {
        if (dirty) {
            save()
            dirty = false
        }
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
            path.jsonWriter().use { it.array(profilesByUUID.values.stream().sorted().limit(MRU_LIMIT), ProfileHolder.Adapter::write) }
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

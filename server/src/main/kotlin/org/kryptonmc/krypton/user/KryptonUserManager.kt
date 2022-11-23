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
package org.kryptonmc.krypton.user

import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry
import com.google.common.collect.MapMaker
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.user.User
import org.kryptonmc.api.user.UserManager
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.auth.requests.ApiService
import org.kryptonmc.krypton.util.DataConversion
import org.kryptonmc.krypton.util.executor.daemonThreadFactory
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class KryptonUserManager(private val server: KryptonServer) : UserManager {

    private val users: MutableMap<UUID, KryptonUser> = MapMaker().weakValues().makeMap()
    private val executor = Executors.newSingleThreadExecutor(daemonThreadFactory("Krypton User Data Loader"))

    fun updateUser(uuid: UUID, data: CompoundTag) {
        users.get(uuid)?.data = data
    }

    override fun get(uuid: UUID): User? = users.get(uuid)

    override fun get(name: String): User? {
        val profile = server.profileCache.get(name) ?: return null
        return users.get(profile.uuid)
    }

    override fun load(uuid: UUID): CompletableFuture<User?> {
        val existing = get(uuid)
        if (existing != null) return CompletableFuture.completedFuture(existing)
        val cachedProfile = server.profileCache.get(uuid)
        if (cachedProfile != null) return CompletableFuture.supplyAsync({ loadUser(cachedProfile) }, executor)
        return ApiService.profile(uuid).thenApplyAsync(::loadUser, executor)
    }

    override fun load(name: String): CompletableFuture<User?> {
        val existing = get(name)
        if (existing != null) return CompletableFuture.completedFuture(existing)
        val cachedProfile = server.profileCache.get(name)
        if (cachedProfile != null) return CompletableFuture.supplyAsync({ loadUser(cachedProfile) }, executor)
        return ApiService.profile(name).thenApplyAsync(::loadUser, executor)
    }

    private fun loadUser(profile: GameProfile?): User? {
        if (profile == null) return null
        val file = server.playerManager.dataManager.folder.resolve("${profile.uuid}.dat")
        val nbt = try {
            TagIO.read(file, TagCompression.GZIP)
        } catch (_: Exception) {
            return null
        }

        val version = if (nbt.contains("DataVersion", IntTag.ID)) nbt.getInt("DataVersion") else -1
        // We won't upgrade data if use of the data converter is disabled.
        if (version < KryptonPlatform.worldVersion && !server.config.advanced.useDataConverter) {
            DataConversion.sendWarning(LOGGER, "data for user with UUID ${profile.uuid}")
            error("Tried to load old user from version $version when data conversion is disabled!")
        }

        return KryptonUser(profile, DataConversion.upgrade(nbt, MCTypeRegistry.PLAYER, version), server)
    }

    companion object {

        private val LOGGER = logger<KryptonUserManager>()
    }
}

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

    override fun getUser(uuid: UUID): User? = users.get(uuid)

    override fun getUser(name: String): User? {
        val profile = server.profileCache.getProfile(name) ?: return null
        return users.get(profile.uuid)
    }

    override fun loadUser(uuid: UUID): CompletableFuture<User?> {
        val existing = getUser(uuid)
        if (existing != null) return CompletableFuture.completedFuture(existing)
        val cachedProfile = server.profileCache.getProfile(uuid)
        if (cachedProfile != null) return CompletableFuture.supplyAsync({ loadUser(cachedProfile) }, executor)
        return ApiService.lookupProfileById(uuid).thenApplyAsync({ loadUser(it) }, executor)
    }

    override fun loadUser(name: String): CompletableFuture<User?> {
        val existing = getUser(name)
        if (existing != null) return CompletableFuture.completedFuture(existing)
        val cachedProfile = server.profileCache.getProfile(name)
        if (cachedProfile != null) return CompletableFuture.supplyAsync({ loadUser(cachedProfile) }, executor)
        return ApiService.lookupProfileByName(name).thenApplyAsync({ loadUser(it) }, executor)
    }

    private fun loadUser(profile: GameProfile?): User? {
        if (profile == null) return null
        val file = server.playerManager.dataFolder().resolve("${profile.uuid}.dat")
        val nbt = try {
            TagIO.read(file, TagCompression.GZIP)
        } catch (_: Exception) {
            return null
        }

        val version = if (nbt.contains("DataVersion", IntTag.ID)) nbt.getInt("DataVersion") else -1
        val data = if (version < KryptonPlatform.worldVersion) DataConversion.upgrade(nbt, MCTypeRegistry.PLAYER, version) else nbt

        return KryptonUser(profile, data, server)
    }
}

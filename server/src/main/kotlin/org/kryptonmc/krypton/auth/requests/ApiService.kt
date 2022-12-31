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
package org.kryptonmc.krypton.auth.requests

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.util.uuid.MojangUUIDTypeAdapter
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

/**
 * Used to make requests to the Mojang API.
 */
object ApiService {

    private const val USERNAME_BASE_URL = "https://api.mojang.com/users/profiles/minecraft/"
    private const val UUID_BASE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/"
    private val EXPIRE = Duration.ofMinutes(5)

    private val client = HttpClient.newHttpClient()
    private val usernameCache = createCache<String> { USERNAME_BASE_URL + it }
    private val uuidCache = createCache<UUID> { UUID_BASE_URL + MojangUUIDTypeAdapter.toString(it) }

    /**
     * Requests the profile with the given [name] from the Mojang API.
     */
    @JvmStatic
    fun lookupProfileByName(name: String): CompletableFuture<GameProfile?> = usernameCache.get(name)

    /**
     * Requests the profile with the given [uuid] from the Mojang API.
     */
    @JvmStatic
    fun lookupProfileById(uuid: UUID): CompletableFuture<GameProfile?> = uuidCache.get(uuid)

    @JvmStatic
    private fun loadProfile(url: String, executor: Executor): CompletableFuture<GameProfile?> =
        client.sendAsync(HttpRequest.newBuilder(URI(url)).build(), BodyHandlers.ofString())
            .thenApplyAsync({ KryptonGameProfile.Adapter.fromJson(it.body()) }, executor)

    @JvmStatic
    private inline fun <K> createCache(crossinline toUrl: (K) -> String): AsyncLoadingCache<K, GameProfile> =
        Caffeine.newBuilder().expireAfterWrite(EXPIRE).maximumSize(128).buildAsync { key, executor -> loadProfile(toUrl(key), executor) }
}

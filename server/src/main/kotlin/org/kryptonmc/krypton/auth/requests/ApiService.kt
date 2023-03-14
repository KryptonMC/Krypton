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

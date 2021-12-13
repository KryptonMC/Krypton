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
package org.kryptonmc.krypton.auth.requests

import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.util.MojangUUIDTypeAdapter
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

/**
 * Used to make requests to the Mojang API.
 */
object ApiService {

    private val client = HttpClient.newHttpClient()

    /**
     * Requests the profile with the given [name] from the Mojang API.
     */
    fun profile(name: String, executor: Executor? = null): CompletableFuture<KryptonGameProfile?> {
        val request = HttpRequest.newBuilder()
            .uri(URI("https://api.mojang.com/users/profiles/minecraft/$name"))
            .build()
        val future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        if (executor != null) return future.thenApplyAsync({ KryptonGameProfile.fromJson(it.body()) }, executor)
        return future.thenApplyAsync { KryptonGameProfile.fromJson(it.body()) }
    }

    /**
     * Requests the profile with the given [uuid] from the Mojang API.
     */
    fun profile(uuid: UUID, executor: Executor? = null): CompletableFuture<KryptonGameProfile?> {
        val id = MojangUUIDTypeAdapter.toString(uuid)
        val request = HttpRequest.newBuilder()
            .uri(URI("https://sessionserver.mojang.com/session/minecraft/profile/$id"))
            .build()
        val future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        if (executor != null) return future.thenApplyAsync({ KryptonGameProfile.fromJson(it.body()) }, executor)
        return future.thenApplyAsync { KryptonGameProfile.fromJson(it.body()) }
    }
}

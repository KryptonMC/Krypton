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

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.util.crypto.Encryption
import org.kryptonmc.krypton.util.logger
import java.math.BigInteger
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

/**
 * Used for authenticating users and caching their profiles.
 */
object SessionService {

    private val SERVER_ID_BYTES = ByteArray(0)
    private const val BASE_URL = "https://sessionserver.mojang.com/session/minecraft/hasJoined"
    private val LOGGER = logger<SessionService>()

    private val client = HttpClient.newHttpClient()
    private val profiles: Cache<String, KryptonGameProfile> = Caffeine.newBuilder()
        .expireAfterWrite(6, TimeUnit.HOURS)
        .maximumSize(128)
        .build()

    /**
     * Authenticates a user with Mojang.
     */
    @JvmStatic
    fun hasJoined(username: String, secret: ByteArray, ip: String): KryptonGameProfile? {
        val cachedProfile = profiles.getIfPresent(username)
        if (cachedProfile != null) return cachedProfile

        val shaDigest = MessageDigest.getInstance("SHA-1")
        shaDigest.update(SERVER_ID_BYTES)
        shaDigest.update(secret)
        shaDigest.update(Encryption.publicKey.encoded)
        val serverId = shaDigest.hexDigest()

        val request = HttpRequest.newBuilder()
            .uri(URI("$BASE_URL?username=$username&serverId=$serverId&ip=$ip"))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 200) { // Ensures no content responses are also a failure.
            LOGGER.debug("Error authenticating $username! Code: ${response.statusCode()}, body: ${response.body()}")
            LOGGER.error("Failed to verify username $username!")
            return null
        }

        val profile = KryptonGameProfile.Adapter.fromJson(response.body())
        LOGGER.info("UUID of player ${profile.name} is ${profile.uuid}.")
        profiles.put(profile.name, profile)
        return profile
    }
}

// This is Yggdrasil's strange way of digesting to hex.
private fun MessageDigest.hexDigest(): String = BigInteger(digest()).toString(16)

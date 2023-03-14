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

import com.github.benmanes.caffeine.cache.Caffeine
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.util.crypto.Encryption
import java.math.BigInteger
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.security.MessageDigest
import java.security.PublicKey
import java.util.concurrent.TimeUnit

/**
 * Used for authenticating users and caching their profiles.
 */
object SessionService {

    private const val OK_RESPONSE_CODE = 200 // HTTP response code for 'OK' response.
    private const val BASE_URL = "https://sessionserver.mojang.com/session/minecraft/hasJoined"
    private val LOGGER = LogManager.getLogger()

    private val client = HttpClient.newHttpClient()
    private val profiles = Caffeine.newBuilder().expireAfterWrite(6, TimeUnit.HOURS).maximumSize(128).build<String, GameProfile>()

    @JvmStatic
    fun encodeServerInfo(serverId: String, secret: ByteArray, publicKey: PublicKey): String {
        val shaDigest = MessageDigest.getInstance("SHA-1")
        shaDigest.update(serverId.encodeToByteArray())
        shaDigest.update(secret)
        shaDigest.update(publicKey.encoded)
        return hexDigest(shaDigest)
    }

    /**
     * Authenticates a user with Mojang.
     */
    @JvmStatic
    fun hasJoined(username: String, secret: ByteArray, ip: String): GameProfile? {
        val cachedProfile = profiles.getIfPresent(username)
        if (cachedProfile != null) return cachedProfile

        val serverId = encodeServerInfo("", secret, Encryption.publicKey)
        val request = HttpRequest.newBuilder(URI("$BASE_URL?username=$username&serverId=$serverId&ip=$ip")).build()
        val response = client.send(request, BodyHandlers.ofString())
        if (response.statusCode() != OK_RESPONSE_CODE) { // Ensures no content responses are also a failure.
            LOGGER.debug("Error authenticating $username! Code: ${response.statusCode()}, body: ${response.body()}")
            LOGGER.error("Failed to verify username $username!")
            return null
        }

        val profile = KryptonGameProfile.Adapter.fromJson(response.body())
        LOGGER.info("UUID of player ${profile.name} is ${profile.uuid}.")
        profiles.put(profile.name, profile)
        return profile
    }

    @JvmStatic
    private fun hexDigest(digest: MessageDigest): String = BigInteger(digest.digest()).toString(16)
}

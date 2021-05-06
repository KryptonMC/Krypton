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

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.auth.exceptions.AuthenticationException
import org.kryptonmc.krypton.util.encryption.Encryption
import org.kryptonmc.krypton.util.encryption.hexDigest
import org.kryptonmc.krypton.util.logger
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.MessageDigest
import java.time.Duration

/**
 * Retrofit interface for querying Mojang's session server.
 *
 * Luckily, the server only has to send one request.
 */
interface MojangSessionService {

    /**
     * Notify the session server that the player with the specified [username]
     * has joined the server.
     *
     * This is the only request required to authenticate a user with Mojang.
     *
     * The [serverId] field is calculated by hashing the [ServerInfo.SERVER_ID][server's ID]
     * in ASCII, the shared secret and the encoded version of the server's public key,
     * then digesting that to hex by creating a `BigInteger` from the [MessageDigest.digest]
     * function, then converting it to a base 16 string (see [MessageDigest.hexDigest])
     *
     * @param username the username of the user we want to authenticate
     * @param serverId the server ID, see above
     * @param ip the server's IP
     * @return a [Call] representing the result of making this request
     */
    @GET("session/minecraft/hasJoined")
    fun hasJoined(
        @Query("username") username: String,
        @Query("serverId") serverId: String,
        @Query("ip") ip: String
    ): Call<GameProfile>
}

/**
 * Used for authenticating users and caching their profiles.
 */
object SessionService {

    private const val SESSION_SERVER_BASE_URL = "https://sessionserver.mojang.com/"

    private val sessionService = Retrofit.Builder()
        .baseUrl(SESSION_SERVER_BASE_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(MojangSessionService::class.java)

    private val profiles: Cache<String, GameProfile> = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofHours(6))
        .build()

    /**
     * Authenticate a user with Mojang.
     *
     * @param username the username to authenticate
     * @param secret the shared secret
     * @param key the server's public key
     * @param ip the server's IP address (string format)
     * @return a new [GameProfile] for the user
     * @throws AuthenticationException if the response from Mojang was unsuccessful (authentication failed)
     */
    fun authenticateUser(username: String, secret: ByteArray, ip: String): GameProfile {
        val cachedProfile = profiles.getIfPresent(username)
        if (cachedProfile != null) return cachedProfile

        val shaDigest = MessageDigest.getInstance("SHA-1")
        shaDigest.update(ServerInfo.SERVER_ID.toByteArray(Charsets.US_ASCII))
        shaDigest.update(secret)
        shaDigest.update(Encryption.publicKey.encoded)
        val serverId = shaDigest.hexDigest()

        val response = sessionService.hasJoined(username, serverId, ip).execute()
        if (!response.isSuccessful) {
            LOGGER.debug("Error authenticating $username! Code: ${response.code()}, body: ${response.errorBody()}")
            LOGGER.error("Failed to verify username $username!")
            throw AuthenticationException()
        }

        val profile = requireNotNull(response.body())
        LOGGER.info("UUID of player ${profile.name} is ${profile.uuid}")
        profiles.put(profile.name, profile)
        return profile
    }

    private val LOGGER = logger<SessionService>()
}

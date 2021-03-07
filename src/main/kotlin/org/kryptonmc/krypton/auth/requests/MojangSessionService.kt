package org.kryptonmc.krypton.auth.requests

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import org.kryptonmc.krypton.auth.GameProfile
import okhttp3.MediaType.Companion.toMediaType
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.auth.exceptions.AuthenticationException
import org.kryptonmc.krypton.encryption.hexDigest
import org.kryptonmc.krypton.extension.logger
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.MessageDigest
import java.security.PublicKey
import java.time.Duration

interface MojangSessionService {

    @GET("session/minecraft/hasJoined")
    fun hasJoined(
        @Query("username") username: String,
        @Query("serverId") serverId: String,
        @Query("ip") ip: String
    ): Call<GameProfile>
}

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

    fun authenticateUser(username: String, secret: ByteArray, key: PublicKey, ip: String): GameProfile {
        val cachedProfile = profiles.getIfPresent(username)
        if (cachedProfile != null) return cachedProfile

        val shaDigest = MessageDigest.getInstance("SHA-1")
        shaDigest.update(ServerInfo.SERVER_ID.toByteArray(Charsets.US_ASCII))
        shaDigest.update(secret)
        shaDigest.update(key.encoded)
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
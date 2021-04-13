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

/**
 * Retrofit interface for querying Mojang's session server.
 *
 * Luckily, the server only has to send one request.
 *
 * @author Callum Seabrook
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
 *
 * @author Callum Seabrook
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
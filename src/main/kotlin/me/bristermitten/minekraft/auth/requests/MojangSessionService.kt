package me.bristermitten.minekraft.auth.requests

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import me.bristermitten.minekraft.auth.GameProfile
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

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

    fun hasJoined(username: String, serverId: String, ip: String) = sessionService.hasJoined(username, serverId, ip)
}
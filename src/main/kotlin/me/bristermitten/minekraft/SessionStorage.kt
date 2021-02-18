package me.bristermitten.minekraft

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import me.bristermitten.minekraft.auth.GameProfile
import me.bristermitten.minekraft.auth.requests.MojangSessionService
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

object SessionStorage {

    val sessions: MutableSet<Session> = ConcurrentHashMap.newKeySet()

    val profiles: Cache<String, GameProfile> = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofHours(6))
        .build()
}
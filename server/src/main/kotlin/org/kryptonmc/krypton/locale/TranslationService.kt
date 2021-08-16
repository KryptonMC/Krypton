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
package org.kryptonmc.krypton.locale

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.bardy.gsonkt.fromJson
import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.util.logger
import java.io.Closeable
import java.io.FilterInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.Locale

object TranslationRequester {

    private const val DATA_URL = "https://data.kryptonmc.org"
    private const val MAX_BUNDLE_SIZE = 1_048_576L // 1MB

    private val client = HttpClient.newHttpClient()

    fun metadata(): MetadataResponse {
        val request = HttpRequest.newBuilder()
            .uri(URI("$DATA_URL/data/translations"))
            .build()
        return GSON.fromJson(client.send(request, HttpResponse.BodyHandlers.ofString()).body())
    }

    fun download(id: String, file: Path) {
        try {
            val request = HttpRequest.newBuilder()
                .uri(URI("$DATA_URL/translation/$id"))
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofByteArray())
            if (response.statusCode() !in 200..299) return
            LimitedInputStream(response.body().inputStream(), MAX_BUNDLE_SIZE).use {
                Files.copy(it, file, StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (exception: IOException) {
            LOGGER.warn("Unable to download translations", exception)
        }
    }

    private val LOGGER = logger<TranslationRequester>()
}

data class MetadataResponse(val cacheMaxAge: Long, val languages: List<LanguageInfo>) {

    companion object : JsonDeserializer<MetadataResponse> {

        override fun deserialize(src: JsonElement, type: Type, context: JsonDeserializationContext): MetadataResponse {
            src as JsonObject
            val cacheMaxAge = src["cacheMaxAge"].asLong
            val languages = src["languages"].asJsonArray.map { LanguageInfo(it.asJsonObject) }
            return MetadataResponse(cacheMaxAge, languages)
        }
    }
}

data class LanguageInfo(val id: String, val name: String, val locale: Locale, val progress: Int, val contributors: List<String>) {

    constructor(data: JsonObject) : this(
        data["id"].asString,
        data["name"].asString,
        data["tag"].asString.toLocale()!!,
        data["progress"].asInt,
        data["contributors"].asJsonArray.map { it.asJsonObject["name"].asString }
    )
}

private class LimitedInputStream(input: InputStream, private val limit: Long) : FilterInputStream(input), Closeable {

    private var count = 0L

    private fun checkLimit() {
        if (count > limit) throw IOException("Limit exceeded!")
    }

    override fun read(): Int {
        val result = super.read()
        if (result != -1) {
            count++
            checkLimit()
        }
        return result
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        val result = super.read(b, off, len)
        if (result > 0) {
            count += result
            checkLimit()
        }
        return result
    }
}

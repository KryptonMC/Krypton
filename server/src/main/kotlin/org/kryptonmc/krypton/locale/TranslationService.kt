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

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.kryptonmc.krypton.util.logger
import java.io.Closeable
import java.io.FilterInputStream
import java.io.IOException
import java.io.InputStream
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
        return MetadataResponse.fromJson(client.send(request, HttpResponse.BodyHandlers.ofString()).body())
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

    companion object : TypeAdapter<MetadataResponse>() {

        override fun read(reader: JsonReader): MetadataResponse {
            reader.beginObject()

            var cacheMaxAge = -1L
            val languages = mutableListOf<LanguageInfo>()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "cacheMaxAge" -> cacheMaxAge = reader.nextLong()
                    "languages" -> {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            val language = LanguageInfo.read(reader)
                            if (language != null) languages.add(language)
                        }
                        reader.endArray()
                    }
                }
            }

            reader.endObject()
            return MetadataResponse(cacheMaxAge, languages)
        }

        override fun write(writer: JsonWriter, value: MetadataResponse) {
            error("MetadataResponse objects should never be written!")
        }
    }
}

data class LanguageInfo(val id: String, val name: String, val locale: Locale, val progress: Int, val contributors: List<String>) {

    companion object : TypeAdapter<LanguageInfo>() {

        override fun read(reader: JsonReader): LanguageInfo? {
            reader.beginObject()

            var id: String? = null
            var name: String? = null
            var tag: String? = null
            var progress = -1
            val contributors = mutableListOf<String>()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "id" -> id = reader.nextString()
                    "name" -> name = reader.nextString()
                    "tag" -> tag = reader.nextString()
                    "progress" -> progress = reader.nextInt()
                    "contributors" -> readContributors(reader, contributors)
                }
            }

            reader.endObject()
            if (id == null || name == null || tag == null || progress == -1) return null
            return LanguageInfo(id, name, Locale.forLanguageTag(tag), progress, contributors)
        }

        override fun write(writer: JsonWriter, value: LanguageInfo) {
            error("LanguageInfo objects should never be written!")
        }

        private fun readContributors(reader: JsonReader, result: MutableList<String>) {
            reader.beginArray()
            while (reader.hasNext()) {
                val name = readContributor(reader)
                if (name != null) result.add(name)
            }
            reader.endArray()
        }

        private fun readContributor(reader: JsonReader): String? {
            reader.beginObject()
            var name: String? = null
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "name" -> name = reader.nextString()
                    "translated" -> reader.nextInt()
                }
            }
            reader.endObject()
            return name
        }
    }
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

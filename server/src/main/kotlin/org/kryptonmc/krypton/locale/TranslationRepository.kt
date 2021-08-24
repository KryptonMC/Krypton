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

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.kryptonmc.krypton.util.createDirectories
import java.util.concurrent.ForkJoinPool
import kotlin.io.path.exists
import kotlin.io.path.reader
import kotlin.io.path.writer

object TranslationRepository {

    private const val CACHE_MAX_AGE = 900_000 // 15 minutes in milliseconds

    val metadata: MetadataResponse
        get() = TranslationRequester.metadata()

    fun scheduleRefresh() = ForkJoinPool.commonPool().execute { refresh() }

    private fun refresh() {
        val translationsDirectory = TranslationManager.TRANSLATIONS_DIRECTORY.apply { createDirectories() }

        val repoStatusFile = translationsDirectory.resolve("repository.json")
        val lastRefresh = if (repoStatusFile.exists()) {
            JsonReader(repoStatusFile.reader()).use { reader ->
                reader.beginObject()
                if (reader.nextName() != "lastRefresh") return@use 0L
                val lastRefresh = reader.nextLong()
                reader.endObject()
                lastRefresh
            }
        } else {
            0L
        }

        val timeSinceLastRefresh = System.currentTimeMillis() - lastRefresh
        if (timeSinceLastRefresh <= CACHE_MAX_AGE) return

        val metadata = metadata
        if (timeSinceLastRefresh <= metadata.cacheMaxAge) return

        downloadAndInstall(metadata.languages)
    }

    private fun downloadAndInstall(languages: List<LanguageInfo>) {
        val translationsDirectory = TranslationManager.TRANSLATIONS_DIRECTORY.apply { createDirectories() }
        languages.forEach { TranslationRequester.download(it.locale.toString(), translationsDirectory.resolve("${it.locale}.properties")) }

        val repoStatusFile = translationsDirectory.resolve("repository.json")
        JsonWriter(repoStatusFile.writer()).use { writer ->
            writer.beginObject()
            writer.name("lastRefresh")
            writer.value(System.currentTimeMillis())
            writer.endObject()
        }

        TranslationManager.reload()
    }
}

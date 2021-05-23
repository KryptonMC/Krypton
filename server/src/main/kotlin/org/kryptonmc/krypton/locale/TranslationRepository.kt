package org.kryptonmc.krypton.locale

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.util.createDirectories
import java.util.concurrent.ForkJoinPool
import kotlin.io.path.bufferedReader
import kotlin.io.path.bufferedWriter
import kotlin.io.path.exists

object TranslationRepository {

    private const val CACHE_MAX_AGE = 900_000 // 15 minutes in milliseconds

    fun scheduleRefresh() = ForkJoinPool.commonPool().execute { refresh() }

    private fun refresh() {
        val translationsDirectory = TranslationManager.TRANSLATIONS_DIRECTORY.apply { createDirectories() }

        val repoStatusFile = translationsDirectory.resolve("repository.json")
        val lastRefresh = if (repoStatusFile.exists()) {
            repoStatusFile.bufferedReader().use {
                val status = GSON.fromJson(it, JsonObject::class.java)
                if (status.has("lastRefresh")) status["lastRefresh"].asLong else 0L
            }
        } else 0L

        val timeSinceLastRefresh = System.currentTimeMillis() - lastRefresh
        if (timeSinceLastRefresh <= CACHE_MAX_AGE) return

        val metadata = metadata
        if (timeSinceLastRefresh <= metadata.cacheMaxAge) return

        downloadAndInstall(metadata.languages, true)
    }

    fun downloadAndInstall(languages: List<LanguageInfo>, updateStatus: Boolean) {
        val translationsDirectory = TranslationManager.TRANSLATIONS_DIRECTORY.apply { createDirectories() }
        languages.forEach { TranslationRequester.download(it.locale.toString(), translationsDirectory.resolve("${it.locale}.properties")) }

        if (updateStatus) {
            val repoStatusFile = translationsDirectory.resolve("repository.json")
            repoStatusFile.bufferedWriter().use {
                val status = JsonObject().apply { add("lastRefresh", JsonPrimitive(System.currentTimeMillis())) }
                GSON.toJson(status, it)
            }
        }

        TranslationManager.reload()
    }

    val metadata: MetadataResponse
        get() = TranslationRequester.metadata()
}

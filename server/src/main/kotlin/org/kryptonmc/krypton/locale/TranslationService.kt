package org.kryptonmc.krypton.locale

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import org.apache.logging.log4j.Logger
import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.util.logger
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Streaming
import java.io.Closeable
import java.io.FilterInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.Locale

interface TranslationService {

    @GET("data/translations")
    fun data(): Call<MetadataResponse>

    @GET("translation/{id}")
    @Streaming
    fun download(@Path("id") id: String): Call<ResponseBody>
}

object TranslationRequester {

    private const val DATA_URL = "https://data.kryptonmc.org/"
    private const val MAX_BUNDLE_SIZE = 1048576L // 1MB

    private val service = Retrofit.Builder()
        .baseUrl(DATA_URL)
        .addConverterFactory(GsonConverterFactory.create(GSON))
        .build()
        .create<TranslationService>()

    fun metadata() = service.data().executeSuccess(LOGGER)

    fun download(id: String, file: java.nio.file.Path) {
        try {
            val request = service.download(id).execute()
            if (!request.isSuccessful) return
            LimitedInputStream(request.body()!!.byteStream(), MAX_BUNDLE_SIZE).use {
                Files.copy(it, file, StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (exception: IOException) {
            LOGGER.warn("Unable to download translations", exception)
        }
    }

    private val LOGGER = logger<TranslationRequester>()
}

class MetadataResponse(val cacheMaxAge: Long, val languages: List<LanguageInfo>) {

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

private fun <T> Call<T>.executeSuccess(logger: Logger): T {
    val response = execute()
    require(response.isSuccessful) { "${request()} unsuccessful! Code: ${response.code()}, Error body: ${response.errorBody()?.string()}".apply { logger.error(this) } }
    return response.body()!!
}

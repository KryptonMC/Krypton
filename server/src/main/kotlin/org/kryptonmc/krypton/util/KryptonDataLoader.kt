package org.kryptonmc.krypton.util

import com.google.gson.Gson
import com.google.gson.JsonObject
import me.bardy.gsonkt.fromJson
import org.kryptonmc.krypton.KryptonPlatform

abstract class KryptonDataLoader(fileSuffix: String) {

    private val fileName = "${KryptonPlatform.minecraftVersionPath}_$fileSuffix.json"
    @Volatile
    private var isLoaded = false

    protected abstract fun load(data: JsonObject)

    fun init() {
        if (isLoaded) {
            LOGGER.warn("Attempted to load data twice!")
            return
        }

        val inputStream = ClassLoader.getSystemResourceAsStream(fileName)
            ?: error("Could not find $fileName bundled in JAR! Please report to Krypton!")
        val data = GSON.fromJson<JsonObject>(inputStream.reader())
        load(data)
        isLoaded = true
    }

    companion object {

        protected val GSON = Gson()
        private val LOGGER = logger<KryptonDataLoader>()
    }
}

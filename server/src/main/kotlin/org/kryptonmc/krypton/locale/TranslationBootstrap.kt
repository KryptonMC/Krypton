package org.kryptonmc.krypton.locale

import com.google.gson.JsonObject
import me.bardy.gsonkt.fromJson
import net.kyori.adventure.key.Key
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.TranslationRegistry
import org.kryptonmc.krypton.GSON
import java.text.MessageFormat
import java.util.Locale

object TranslationBootstrap {

    val REGISTRY = TranslationRegistry.create(Key.key("krypton", "minecraft_translations"))
    private val UNSUPPORTED_FORMAT_REGEX = "%(\\d+\\\$)?[\\d.]*[df]".toRegex()

    fun init() {
        val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream("en_us.json")
            ?: error("Unable to find built-in Minecraft locale file in JAR!")
        val json = GSON.fromJson<JsonObject>(inputStream.reader())
        json.entrySet().forEach {
            val key = it.key
            val value = it.value.asString.replace(UNSUPPORTED_FORMAT_REGEX, "%\$1s").replaceMinecraftPattern()
            REGISTRY.register(key, Locale.US, MessageFormat(value, Locale.US))
        }
        GlobalTranslator.get().addSource(REGISTRY)
    }
}

private val UNSUPPORTED_MINECRAFT_PATTERN = "(%+)((\\\\d+\\\$)?[\\\\d.]*[df]|s)".toRegex()

private fun String.replaceMinecraftPattern(): String {
    var index = -1
    return UNSUPPORTED_MINECRAFT_PATTERN.replace(this) {
        index++
        "{$index}"
    }
}

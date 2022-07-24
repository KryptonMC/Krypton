/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.util

import com.google.gson.stream.JsonReader
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.renderer.TranslatableComponentRenderer
import net.kyori.adventure.translation.TranslationRegistry
import java.text.MessageFormat
import java.util.Locale

object TranslationBootstrap {

    // A regex for an old format that was used previously by Minecraft.
    // This comes from vanilla's Language, and is only here because it's what vanilla does.
    private val UNSUPPORTED_FORMAT_REGEX = "%(\\d+\\\$)?[\\d.]*[df]".toRegex()

    @JvmField
    val REGISTRY: TranslationRegistry = TranslationRegistry.create(Key.key("krypton", "minecraft_translations"))
    private val RENDERER = TranslatableComponentRenderer.usingTranslationSource(REGISTRY)
    @Volatile
    private var bootstrapped = false

    @JvmStatic
    fun init() {
        if (bootstrapped) return
        val inputStream = checkNotNull(ClassLoader.getSystemResourceAsStream("en_us.json")) {
            "Unable to find built-in Minecraft locale file in JAR!"
        }
        JsonReader(inputStream.reader()).use { reader ->
            reader.beginObject()
            while (reader.hasNext()) {
                val key = reader.nextName()
                val value = reader.nextString().replace(UNSUPPORTED_FORMAT_REGEX, "%\$1s")
                REGISTRY.register(key, Locale.US, MessageFormat(value, Locale.US))
            }
            reader.endObject()
        }
        bootstrapped = true
    }

    @JvmStatic
    fun render(message: Component): Component = RENDERER.render(message, Locale.ENGLISH)
}

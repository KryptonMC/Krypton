/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.locale

import com.google.gson.stream.JsonReader
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.renderer.TranslatableComponentRenderer
import net.kyori.adventure.translation.TranslationRegistry
import java.text.MessageFormat
import java.util.Locale

object MinecraftTranslationManager {

    // A regex for an old format that was used previously by Minecraft.
    // This comes from vanilla's Language, and is only here because it's what vanilla does.
    private val UNSUPPORTED_FORMAT_REGEX = "%(\\d+\\\$)?[\\d.]*[df]".toRegex()

    @JvmField
    val REGISTRY: TranslationRegistry = TranslationRegistry.create(Key.key("krypton", "minecraft_translations"))
    private val RENDERER = TranslatableComponentRenderer.usingTranslationSource(REGISTRY)

    @JvmStatic
    fun init() {
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
    }

    @JvmStatic
    fun render(message: Component): Component = RENDERER.render(message, Locale.ENGLISH)
}

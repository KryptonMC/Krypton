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

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.KryptonRegistries

abstract class KryptonDataLoader<T>(fileSuffix: String, protected val registry: KryptonRegistry<T>) {

    private val fileName = "${KryptonPlatform.dataVersionPrefix}_$fileSuffix.json"
    @Volatile
    private var isLoaded = false

    protected abstract fun create(key: Key, value: JsonObject): T

    protected open fun register(key: Key, value: T) {
        KryptonRegistries.register(registry, key, value)
    }

    protected open fun preload() {
        // empty by default
    }

    fun init() {
        if (isLoaded) {
            LOGGER.warn("Attempted to load data twice!")
            return
        }

        val inputStream = checkNotNull(ClassLoader.getSystemResourceAsStream(fileName)) {
            "Could not find $fileName bundled in JAR! Please report to Krypton!"
        }
        preload()
        GSON.fromJson(inputStream.reader(), JsonObject::class.java).entrySet().forEach { (entryKey, value) ->
            val key = Key.key(entryKey)
            if (!registry.containsKey(key)) register(key, create(key, value as JsonObject))
        }
        isLoaded = true
    }

    companion object {

        protected val GSON = Gson()
        private val LOGGER = LogManager.getLogger()
    }
}

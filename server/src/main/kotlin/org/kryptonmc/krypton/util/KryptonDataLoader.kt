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
import me.bardy.gsonkt.fromJson
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.krypton.KryptonPlatform

abstract class KryptonDataLoader<T : Any>(fileSuffix: String, protected val registry: Registry<T>) {

    private val fileName = "${KryptonPlatform.dataVersionPrefix}_$fileSuffix.json"
    @Volatile
    private var isLoaded = false

    protected abstract fun create(key: Key, value: JsonObject): T

    protected open fun register(key: Key, value: T) {
        registry.register(key, value)
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
        load(GSON.fromJson(inputStream.reader()))
    }

    fun load(data: JsonObject) {
        preload()
        data.entrySet().forEach { (key, value) ->
            val namespacedKey = Key.key(key)
            if (registry.contains(namespacedKey)) return@forEach
            register(namespacedKey, create(namespacedKey, value as JsonObject))
        }
        isLoaded = true
    }

    companion object {

        protected val GSON = Gson()
        private val LOGGER = logger<KryptonDataLoader<*>>()
    }
}

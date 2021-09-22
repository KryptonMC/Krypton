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

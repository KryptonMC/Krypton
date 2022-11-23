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
package org.kryptonmc.krypton.server

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.kryptonmc.krypton.util.jsonReader
import org.kryptonmc.krypton.util.jsonWriter
import org.kryptonmc.krypton.util.logger
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

abstract class PersistentManager(private val path: Path) {

    private var dirty = false

    fun load() {
        if (!Files.exists(path)) return
        try {
            path.jsonReader().use(::loadData)
        } catch (exception: IOException) {
            LOGGER.warn("Failed to read $path. You can delete it to force the server to recreate it.", exception)
        } catch (exception: IllegalStateException) {
            LOGGER.warn("Failed to parse JSON data from $path. You can delete it to force the server to recreate it.", exception)
        }
    }

    protected abstract fun loadData(reader: JsonReader)

    fun saveIfNeeded() {
        if (dirty) save()
        dirty = false
    }

    fun save() {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path)
            } catch (exception: IOException) {
                LOGGER.warn("Failed to create whitelist file $path. Whitelist will not be saved.", exception)
                return
            }
        }
        try {
            path.jsonWriter().use(::saveData)
        } catch (exception: IOException) {
            LOGGER.warn("Failed to save whitelist file $path. Whitelist will not be saved.", exception)
        }
    }

    protected abstract fun saveData(writer: JsonWriter)

    protected fun markDirty() {
        dirty = true
    }

    companion object {

        private val LOGGER = logger<PersistentManager>()
    }
}

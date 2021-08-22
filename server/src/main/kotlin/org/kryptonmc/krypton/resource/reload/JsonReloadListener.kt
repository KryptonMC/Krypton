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
package org.kryptonmc.krypton.resource.reload

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.resource.ResourceManager
import org.kryptonmc.krypton.util.logger
import java.io.IOException

abstract class JsonReloadListener(
    private val gson: Gson,
    private val directory: String
) : SimpleReloadListener<Map<Key, JsonElement>>() {

    override fun prepare(manager: ResourceManager): Map<Key, JsonElement> {
        val resources = mutableMapOf<Key, JsonElement>()
        val length = directory.length + 1
        manager.list(directory) { it.endsWith(PATH_SUFFIX) }.forEach { resourceKey ->
            val path = resourceKey.value()
            val key = Key.key(resourceKey.namespace(), path.substring(length, path.length - PATH_SUFFIX_LENGTH))
            try {
                manager(key).use { resource ->
                    resource.inputStream.reader().use { reader ->
                        gson.fromJson(reader, JsonElement::class.java)?.let {
                            check(resources.put(key, it) == null) { "Duplicate data file found with ID $key! Ignoring..." }
                        } ?: LOGGER.error("Failed to load data file $key from $resourceKey as it's null or empty!")
                    }
                }
            } catch (exception: Exception) {
                if (exception !is IllegalArgumentException && exception !is IOException && exception !is JsonParseException) throw exception
                LOGGER.error("Failed to parse data file $key from $resourceKey!", exception)
            }
        }
        return resources
    }

    companion object {

        private const val PATH_SUFFIX = ".json"
        private const val PATH_SUFFIX_LENGTH = PATH_SUFFIX.length
        private val LOGGER = logger<JsonReloadListener>()
    }
}

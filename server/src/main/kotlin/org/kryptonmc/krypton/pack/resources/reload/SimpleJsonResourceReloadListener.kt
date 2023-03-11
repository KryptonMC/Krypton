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
package org.kryptonmc.krypton.pack.resources.reload

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.stream.JsonReader
import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.pack.resources.ResourceManager
import java.io.IOException

abstract class SimpleJsonResourceReloadListener(
    private val gson: Gson,
    private val directory: String
) : SimplePreparableReloadListener<Map<Key, JsonElement>>() {

    override fun prepare(manager: ResourceManager): Map<Key, JsonElement> {
        val result = HashMap<Key, JsonElement>()
        manager.listResources(directory) { it.value().endsWith(PATH_SUFFIX) }.forEach {
            val key = it.key
            val path = key.value()
            val location = Key.key(key.namespace(), path.substring(directory.length + 1, path.length - PATH_SUFFIX_LENGTH))
            try {
                it.value.openAsReader().use { reader ->
                    val json = gson.getAdapter(JsonElement::class.java).read(JsonReader(reader))
                    if (json != null) {
                        check(result.put(location, json) == null) { "Duplicate data file ignored with ID $location!" }
                    } else {
                        LOGGER.error("Failed to load data file $location from $key as it's null or empty!")
                    }
                }
            // Kotlin please add multicatch soon
            } catch (exception: IllegalArgumentException) {
                logReadError(location, key, exception)
            } catch (exception: IOException) {
                logReadError(location, key, exception)
            } catch (exception: JsonParseException) {
                logReadError(location, key, exception)
            }
        }
        return result
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private const val PATH_SUFFIX = ".json"
        private const val PATH_SUFFIX_LENGTH = PATH_SUFFIX.length

        @JvmStatic
        private fun logReadError(location: Key, key: Key, exception: Exception) {
            LOGGER.error("Failed to parse data file $location from $key!", exception)
        }
    }
}

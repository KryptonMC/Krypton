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
package org.kryptonmc.krypton.pack

import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.pack.metadata.MetadataSectionSerializer
import org.kryptonmc.krypton.util.gson.GsonHelper
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

abstract class AbstractPackResources protected constructor(private val name: String, private val builtin: Boolean) : PackResources {

    final override fun packId(): String = name

    final override fun isBuiltin(): Boolean = builtin

    final override fun <T> getMetadataSection(serializer: MetadataSectionSerializer<T>): T? {
        val resource = getRootResource(PackResources.PACK_META) ?: return null
        return resource.get().use { getMetadataFromStream(serializer, it) }
    }

    companion object {

        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        fun <T> getMetadataFromStream(serializer: MetadataSectionSerializer<T>, input: InputStream): T? {
            val json = try {
                BufferedReader(InputStreamReader(input, Charsets.UTF_8)).use(GsonHelper::parse)
            } catch (exception: Exception) {
                LOGGER.error("Failed to load ${serializer.metadataSectionName()} metadata!", exception)
                return null
            }
            if (!json.has(serializer.metadataSectionName())) return null
            return try {
                serializer.fromJson(json.getAsJsonObject(serializer.metadataSectionName()))
            } catch (exception: Exception) {
                LOGGER.error("Failed to load ${serializer.metadataSectionName()} metadata!", exception)
                null
            }
        }
    }
}

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

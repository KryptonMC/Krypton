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

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.pack.metadata.MetadataSerializer
import org.kryptonmc.krypton.util.GsonHelper
import org.kryptonmc.krypton.util.logger
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Path

abstract class AbstractPackResources(protected val path: Path) : PackResources {

    final override val name: String
        get() = path.fileName.toString()

    protected abstract fun hasResource(path: String): Boolean

    protected abstract fun getResource(path: String): InputStream

    final override fun hasResource(location: Key): Boolean = hasResource(convertPath(location))

    final override fun getRootResource(fileName: String): InputStream? {
        require(!fileName.contains("/") && !fileName.contains("\\")) { "Root resources can only be file names, not paths!" }
        return getResource(fileName)
    }

    final override fun getResource(location: Key): InputStream = getResource(convertPath(location))

    final override fun <T> getMetadata(serializer: MetadataSerializer<T>): T? =
        getResource(PackResources.PACK_META).use { getMetadataFromStream(serializer, it) }

    protected fun logWarning(namespace: String) {
        LOGGER.warn("Ignoring non-lowercase namespace $namespace for data pack at ${path.fileName}")
    }

    companion object {

        private val LOGGER = logger<AbstractPackResources>()

        @JvmStatic
        fun <T> getMetadataFromStream(serializer: MetadataSerializer<T>, input: InputStream): T? {
            val json = try {
                BufferedReader(InputStreamReader(input, Charsets.UTF_8)).use(GsonHelper::parse)
            } catch (exception: Exception) {
                LOGGER.error("Failed to load ${serializer.name} metadata!", exception)
                return null
            }
            if (!json.has(serializer.name)) return null
            return try {
                serializer.fromJson(json.getAsJsonObject(serializer.name))
            } catch (exception: Exception) {
                LOGGER.error("Failed to load ${serializer.name} metadata!", exception)
                null
            }
        }

        @JvmStatic
        private fun convertPath(location: Key): String = "${PackResources.DATA_FOLDER_NAME}/${location.namespace()}/${location.key()}"

        @JvmStatic
        protected fun relativizePath(first: Path, second: Path): String = first.toUri().relativize(second.toUri()).path
    }
}

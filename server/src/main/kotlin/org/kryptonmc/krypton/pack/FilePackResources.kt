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

import com.google.common.base.Splitter
import kotlinx.collections.immutable.persistentSetOf
import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.util.Keys
import java.io.IOException
import java.io.InputStream
import java.nio.file.Path
import java.util.function.Predicate
import java.util.zip.ZipFile

class FilePackResources(path: Path) : AbstractPackResources(path) {

    private var zipFile: ZipFile? = null
    override val namespaces: Set<String> by lazy { collectNamespaces(this) }

    override fun hasResource(path: String): Boolean {
        return try {
            getOrCreateZipFile().getEntry(path) != null
        } catch (_: IOException) {
            false
        }
    }

    override fun getResource(path: String): InputStream {
        val file = getOrCreateZipFile()
        val entry = file.getEntry(path) ?: throw ResourcePackFileNotFoundException(this.path, path)
        return file.getInputStream(entry)
    }

    override fun getResources(namespace: String, path: String, predicate: Predicate<Key>): Collection<Key> {
        val file = try {
            getOrCreateZipFile()
        } catch (_: IOException) {
            return persistentSetOf()
        }
        val entries = file.entries()
        val result = persistentSetOf<Key>().builder()
        val namespacedPrefix = "${PackResources.DATA_FOLDER_NAME}/$namespace/"
        val prefix = "$namespacedPrefix$path/"
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            if (entry.isDirectory || entry.name.endsWith(PackResources.METADATA_EXTENSION) || !entry.name.startsWith(prefix)) continue
            val name = entry.name.substring(namespacedPrefix.length)
            val key = Keys.create(namespace, name)
            if (key == null) {
                LOGGER.warn("Invalid path in data pack: $namespace:$name, ignoring...")
            } else if (predicate.test(key)) {
                result.add(key)
            }
        }
        return result.build()
    }

    override fun close() {
        try {
            zipFile?.close()
        } catch (ignored: Exception) {
            // Just ignore
        }
        zipFile = null
    }

    private fun getOrCreateZipFile(): ZipFile {
        if (zipFile == null) zipFile = ZipFile(path.toFile())
        return zipFile!!
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private val SPLITTER = Splitter.on("/").omitEmptyStrings().limit(3)

        @JvmStatic
        private fun collectNamespaces(resources: FilePackResources): Set<String> {
            val zipFile = try {
                resources.getOrCreateZipFile()
            } catch (_: IOException) {
                return emptySet()
            }
            val entries = zipFile.entries()
            val namespaces = persistentSetOf<String>().builder()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                if (!entry.name.startsWith("${PackResources.DATA_FOLDER_NAME}/")) continue
                val parts = SPLITTER.split(entry.name).toList()
                if (parts.size <= 1) continue
                val namespace = parts.get(1)
                if (namespace == namespace.lowercase()) namespaces.add(namespace) else resources.logWarning(namespace)
            }
            return namespaces.build()
        }
    }
}

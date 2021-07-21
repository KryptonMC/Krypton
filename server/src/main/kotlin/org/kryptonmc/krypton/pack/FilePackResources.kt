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
package org.kryptonmc.krypton.pack

import com.google.common.base.Splitter
import net.kyori.adventure.key.Key
import java.io.File
import java.io.IOException
import java.nio.file.Path
import java.util.zip.ZipFile

class FilePackResources(private val file: File, path: Path) : AbstractPackResources(path) {

    private val zipFile by lazy { ZipFile(file) }

    override fun resource(path: String) = zipFile.getEntry(path)?.let { zipFile.getInputStream(it) } ?: throw PackFileNotFoundException(root, path)

    override fun contains(path: String) = try {
        zipFile.getEntry(path) != null
    } catch (exception: IOException) {
        false
    }

    override fun resources(namespace: String, value: String, maxDepth: Int, predicate: (String) -> Boolean): Collection<Key> {
        val zip = try {
            zipFile
        } catch (exception: IOException) {
            return emptySet()
        }
        val entries = zip.entries()
        val keys = mutableListOf<Key>()
        val directory = "data/$namespace/"
        val prefix = "$directory$value/"
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            if (entry.isDirectory) continue
            if (entry.name.endsWith(PackResources.METADATA_EXTENSION) || !entry.name.startsWith(prefix)) continue
            val file = entry.name.substring(directory.length)
            val parts = file.split("/")
            if (parts.size >= maxDepth + 1 && predicate(parts.last())) keys.add(Key.key(namespace, file))
        }
        return keys
    }

    override val namespaces: Set<String>
        get() {
            val zip = try {
                zipFile
            } catch (exception: IOException) {
                return emptySet()
            }
            val entries = zip.entries()
            val namespaces = mutableSetOf<String>()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val name = entry.name
                if (!name.startsWith("data/")) continue
                val parts = SPLITTER.splitToList(name)
                if (parts.size <= 1) continue
                val namespace = parts.first()
                if (namespace == namespace.lowercase()) namespaces.add(namespace) else warn(namespace)
            }
            return namespaces
        }

    override fun close() {
        try {
            zipFile.close()
        } catch (ignored: IOException) {}
    }

    protected fun finalize() {
        close()
    }

    companion object {

        private val SPLITTER = Splitter.on('/').omitEmptyStrings().limit(3)
    }
}

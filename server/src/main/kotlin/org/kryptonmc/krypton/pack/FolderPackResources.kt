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

import com.google.common.base.CharMatcher
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.util.logger
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Predicate

class FolderPackResources(path: Path) : AbstractPackResources(path) {

    override val namespaces: Set<String> by lazy { collectNamespaces(this, path) }

    override fun hasResource(path: String): Boolean = resolvePath(path) != null

    override fun getResource(path: String): InputStream {
        val resolved = resolvePath(path) ?: throw ResourcePackFileNotFoundException(this.path, path)
        return Files.newInputStream(resolved)
    }

    override fun getResources(namespace: String, path: String, predicate: Predicate<Key>): Collection<Key> {
        val dataFolder = this.path.resolve("data")
        val output = persistentListOf<Key>().builder()
        listResources(dataFolder.resolve(namespace).resolve(path), namespace, output, "$path/", predicate)
        return output.build()
    }

    override fun close() {
        // Nothing to close
    }

    private fun listResources(path: Path, namespace: String, output: MutableList<Key>, name: String, predicate: Predicate<Key>) {
        try {
            Files.list(path).forEach {
                if (Files.isDirectory(it)) {
                    listResources(it, namespace, output, name + it.fileName.toString() + "/", predicate)
                    return@forEach
                }
                if (it.fileName.toString().endsWith(PackResources.METADATA_EXTENSION)) return@forEach
                val fullName = name + it.fileName.toString()
                val key = Keys.create(namespace, fullName)
                if (key == null) {
                    LOGGER.warn("Invalid path in data pack: $namespace:$fullName, ignoring...")
                } else if (predicate.test(key)) {
                    output.add(key)
                }
            }
        } catch (_: Exception) {
            // Do nothing, let it fall through
        }
    }

    private fun resolvePath(name: String): Path? {
        try {
            val resolved = path.resolve(name)
            if (Files.isRegularFile(resolved) && validatePath(resolved, name)) return resolved
        } catch (_: IOException) {
            // Do nothing, fall through
        }
        return null
    }

    companion object {

        private val LOGGER = logger<FolderPackResources>()
        private val ON_WINDOWS = System.getProperty("os.name").lowercase().contains("win")
        private val BACKSLASH_MATCHER = CharMatcher.`is`('\\')

        @JvmStatic
        fun validatePath(path: Path, name: String): Boolean {
            var canonical = path.toFile().canonicalPath
            if (ON_WINDOWS) canonical = BACKSLASH_MATCHER.replaceFrom(canonical, '/')
            return canonical.endsWith(name)
        }

        @JvmStatic
        private fun collectNamespaces(resources: FolderPackResources, path: Path): Set<String> {
            val namespaces = persistentSetOf<String>().builder()
            val dataFolder = path.resolve("data")
            try {
                Files.list(dataFolder).filter(Files::isDirectory).forEach {
                    val namespace = relativizePath(dataFolder, it)
                    if (namespace == namespace.lowercase()) {
                        namespaces.add(namespace.substring(0, namespace.length - 1))
                    } else {
                        resources.logWarning(namespace)
                    }
                }
            } catch (_: Exception) {
                // Do nothing, let it fall through
            }
            return namespaces.build()
        }
    }
}

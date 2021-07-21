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

import com.google.common.base.CharMatcher
import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.util.list
import org.kryptonmc.krypton.util.logger
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.inputStream
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

class FolderPackResources(path: Path) : AbstractPackResources(path) {

    override fun resource(path: String) = resolve(path)?.inputStream() ?: throw PackFileNotFoundException(root, path)

    override fun resources(namespace: String, path: String, maxDepth: Int, predicate: (String) -> Boolean): Collection<Key> {
        val relative = root.resolve("data")
        val output = mutableListOf<Key>()
        listResources(relative.resolve(namespace).resolve(path), maxDepth, namespace, "$path/", predicate, output)
        return output
    }

    override fun contains(path: String) = resolve(path) != null

    override fun close() = Unit

    private fun resolve(name: String): Path? = try {
        root.resolve(name).takeIf { it.isRegularFile() && it.validate(name) }
    } catch (exception: IOException) {
        null
    }

    private fun listResources(path: Path, amount: Int, namespace: String, key: String, predicate: (String) -> Boolean, output: MutableList<Key>): Unit = try {
        path.list().forEach {
            if (it.isDirectory()) {
                if (amount > 0) listResources(it, amount - 1, namespace, "$key$it/", predicate, output)
                return@forEach
            }
            if (!it.endsWith(".mcmeta") && predicate(it.toString())) try {
                output.add(Key.key(namespace, key + it.toString()))
            } catch (exception: InvalidKeyException) {
                LOGGER.error(exception.message)
            }
        }
    } catch (ignored: Exception) {}

    override val namespaces: Set<String>
        get() {
            val namespaces = mutableSetOf<String>()
            val dataFolder = root.resolve("data")
            try {
                dataFolder.list()
            } catch (exception: Exception) {
                return namespaces
            }.forEach {
                val relative = dataFolder.toUri().relativize(it.toUri()).path
                if (relative == relative.lowercase()) namespaces.add(relative.dropLast(1)) else warn(relative)
            }
            return namespaces
        }

    companion object {

        private val LOGGER = logger<FolderPackResources>()
    }
}

private val ON_WINDOWS = System.getProperty("os.name").lowercase().contains("win")
private val BACKSLASH_MATCHER = CharMatcher.`is`('\\')

fun Path.validate(ending: String): Boolean {
    var canonical = toFile().canonicalPath
    if (ON_WINDOWS) canonical = BACKSLASH_MATCHER.replaceFrom(canonical, "/")
    return canonical.endsWith(ending)
}

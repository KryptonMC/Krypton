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

import com.google.common.base.Joiner
import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.util.FileUtil
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.util.Keys
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Supplier

class PathPackResources(name: String, private val root: Path, builtin: Boolean) : AbstractPackResources(name, builtin) {

    override fun getRootResource(vararg path: String): Supplier<InputStream>? {
        FileUtil.validatePath(path)
        val resolved = FileUtil.resolvePath(root, ImmutableLists.ofArray(path))
        return if (Files.exists(resolved)) Supplier { Files.newInputStream(resolved) } else null
    }

    override fun getResource(packType: PackType, location: Key): Supplier<InputStream> =
        getResource(location, root.resolve(packType.directory).resolve(location.namespace()))

    override fun listResources(packType: PackType, namespace: String, path: String, output: PackResources.ResourceOutput) {
        FileUtil.decomposePath(path).get()
            .ifLeft { listPath(namespace, root.resolve(packType.directory).resolve(namespace), it, output) }
            .ifRight { LOGGER.error("Invalid path $path! ${it.message}") }
    }

    override fun namespaces(packType: PackType): Set<String> {
        val namespaces = HashSet<String>()
        val directory = root.resolve(packType.directory)

        try {
            Files.newDirectoryStream(directory).use { stream ->
                for (path in stream) {
                    val namespace = path.fileName.toString()
                    if (namespace == namespace.lowercase()) {
                        namespaces.add(namespace)
                    } else {
                        LOGGER.warn("Ignored non-lowercase namespace $namespace in $root.")
                    }
                }
            }
        } catch (_: NoSuchFileException) {
            // This should be impossible, but it's okay to ignore it.
        } catch (exception: IOException) {
            LOGGER.error("Failed to list path $directory!", exception)
        }
        return namespaces
    }

    override fun close() {
        // Nothing to close
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private val PATH_JOINER = Joiner.on('/')

        @JvmStatic
        private fun getResource(location: Key, root: Path): Supplier<InputStream> = FileUtil.decomposePath(location.value()).get().map(
            { returnFileIfExists(FileUtil.resolvePath(root, it)) },
            {
                LOGGER.error("Invalid path $location! ${it.message}")
                null
            }
        )

        @JvmStatic
        private fun returnFileIfExists(path: Path): Supplier<InputStream>? =
            if (Files.exists(path)) Supplier { Files.newInputStream(path) } else null

        @JvmStatic
        fun listPath(namespace: String, root: Path, segments: List<String>, output: PackResources.ResourceOutput) {
            val directory = FileUtil.resolvePath(root, segments)
            try {
                Files.find(directory, Int.MAX_VALUE, { _, attributes -> attributes.isRegularFile }).use { files ->
                    files.forEach { file ->
                        val fullPath = PATH_JOINER.join(root.relativize(file))
                        val location = Keys.create(namespace, fullPath)
                        if (location == null) {
                            LOGGER.error("Invalid path in pack: $namespace:$fullPath! Ignoring...")
                        } else {
                            output.accept(location, Supplier { Files.newInputStream(file) })
                        }
                    }
                }
            } catch (_: NoSuchFileException) {
                // This should be impossible, but it's okay to ignore it.
            } catch (exception: IOException) {
                LOGGER.error("Failed to list path $directory!", exception)
            }
        }
    }
}

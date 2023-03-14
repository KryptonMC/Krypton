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

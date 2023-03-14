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
package org.kryptonmc.krypton.pack.repository

import net.kyori.adventure.text.Component
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.pack.FilePackResources
import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.pack.PackType
import org.kryptonmc.krypton.pack.PathPackResources
import org.kryptonmc.krypton.util.FileUtil
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.util.function.BiConsumer
import java.util.function.Consumer

class FolderRepositorySource(private val folder: Path, private val packType: PackType, private val packSource: PackSource) : RepositorySource {

    override fun loadPacks(action: Consumer<Pack>) {
        try {
            FileUtil.createDirectoriesSafe(folder)
            discoverPacks(folder, false) { path, resources ->
                val name = nameFromPath(path)
                val pack = Pack.readMetaAndCreate("file/$name", Component.text(name), false, resources, packType, Pack.Position.TOP, packSource)
                if (pack != null) action.accept(pack)
            }
        } catch (exception: IOException) {
            LOGGER.error("Failed to list packs in $folder!", exception)
        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        private fun nameFromPath(path: Path): String = path.fileName.toString()

        @JvmStatic
        fun discoverPacks(path: Path, builtin: Boolean, action: BiConsumer<Path, Pack.ResourcesSupplier>) {
            Files.newDirectoryStream(path).use { stream ->
                stream.forEach {
                    val resources = detectPackResources(it, builtin)
                    if (resources != null) action.accept(it, resources)
                }
            }
        }

        @JvmStatic
        private fun detectPackResources(path: Path, builtin: Boolean): Pack.ResourcesSupplier? {
            val attributes = try {
                Files.readAttributes(path, BasicFileAttributes::class.java)
            } catch (_: NoSuchFileException) {
                return null
            } catch (exception: IOException) {
                LOGGER.warn("Failed to read properties of $path. Ignoring...", exception)
                return null
            }
            if (attributes.isDirectory && Files.isRegularFile(path.resolve(PackResources.PACK_META))) {
                return Pack.ResourcesSupplier { PathPackResources(it, path, builtin) }
            }
            if (attributes.isRegularFile && path.fileName.toString().endsWith(".zip")) {
                val system = path.fileSystem
                if (system == FileSystems.getDefault()) {
                    val file = path.toFile()
                    return Pack.ResourcesSupplier { FilePackResources(it, file, builtin) }
                }
            }
            LOGGER.info("Found non-pack entry $path. Ignoring...")
            return null
        }
    }
}

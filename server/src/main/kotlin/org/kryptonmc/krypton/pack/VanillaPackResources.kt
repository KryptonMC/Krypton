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

import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.pack.metadata.MetadataSerializer
import org.kryptonmc.krypton.pack.metadata.PackMetadata
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.util.logger
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.net.URL
import java.nio.file.FileSystemAlreadyExistsException
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class VanillaPackResources(val metadata: PackMetadata, vararg namespaces: String) : PackResources {

    override val namespaces: Set<String> = namespaces.asIterable().toImmutableSet()
    override val name: String
        get() = "Default"

    override fun hasResource(location: Key): Boolean {
        val path = createPath(location)
        return try {
            isResourceUrlValid(path, VanillaPackResources::class.java.getResource(path))
        } catch (exception: IOException) {
            false
        }
    }

    override fun getRootResource(fileName: String): InputStream? {
        require(!fileName.contains("/") && !fileName.contains("\\")) { "Root resources can only be file names, not paths!" }
        return VanillaPackResources::class.java.getResourceAsStream("/$fileName")
    }

    override fun getResource(location: Key): InputStream = getResourceAsStream(location) ?: throw FileNotFoundException(location.value())

    override fun getResources(namespace: String, path: String, predicate: Predicate<Key>): Collection<Key> {
        val result = persistentSetOf<Key>().builder()
        try {
            if (ROOT_DIRECTORY != null) {
                getResources(result, namespace, ROOT_DIRECTORY, name, predicate)
            } else {
                LOGGER.error("Cannot access vanilla data pack root directory!")
            }
        } catch (_: NoSuchFileException) {
        } catch (_: FileNotFoundException) {
        } catch (exception: IOException) {
            LOGGER.error("Failed to get a list of all vanilla resources!", exception)
        }
        return result.build()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getMetadata(serializer: MetadataSerializer<T>): T? {
        return try {
            getRootResource(PackResources.PACK_META).use {
                if (it != null) {
                    val metadata = AbstractPackResources.getMetadataFromStream(serializer, it)
                    if (metadata != null) return metadata
                }
                if (serializer === PackMetadata.Serializer) metadata as T else null
            }
        } catch (exception: FileNotFoundException) {
            if (serializer === PackMetadata.Serializer) metadata as T else null
        } catch (exception: RuntimeException) {
            if (serializer === PackMetadata.Serializer) metadata as T else null
        }
    }

    override fun close() {
        // Nothing to close
    }

    private fun getResourceAsStream(location: Key): InputStream? {
        val path = createPath(location)
        return try {
            val url = VanillaPackResources::class.java.getResource(path)
            if (isResourceUrlValid(path, url)) url.openStream() else null
        } catch (_: IOException) {
            VanillaPackResources::class.java.getResourceAsStream(path)
        }
    }

    companion object {

        private val LOGGER = logger<VanillaPackResources>()
        private val ROOT_DIRECTORY = resolveRootDirectory()

        @JvmStatic
        private fun resolveRootDirectory(): Path? = synchronized(VanillaPackResources::class.java) {
            val verificationFile = VanillaPackResources::class.java.getResource("/${PackResources.DATA_FOLDER_NAME}/.mcassetsroot")
            if (verificationFile == null) {
                LOGGER.error("Failed to find vanilla data pack verification file (.mcassetsroot) in classpath!")
                return null
            }
            try {
                val uri = verificationFile.toURI()
                if (uri.scheme != "jar" && uri.scheme != "file") LOGGER.warn("Vanilla data pack URL $uri uses unexpected schema")
                return safeGetPath(uri).parent
            } catch (exception: Exception) {
                LOGGER.error("Failed to resolve path to vanilla assets!", exception)
            }
            return null
        }

        @JvmStatic
        private fun safeGetPath(uri: URI): Path {
            try {
                return Path.of(uri)
            } catch (_: FileSystemNotFoundException) {
                // Ignore, fall through, since we will try and create one later
            } catch (exception: Throwable) {
                LOGGER.warn("Unable to get path for $uri!", exception)
            }
            try {
                FileSystems.newFileSystem(uri, emptyMap<String, Any>())
            } catch (_: FileSystemAlreadyExistsException) {
                // Ignore, fall through
            }
            return Path.of(uri)
        }

        @JvmStatic
        private fun createPath(location: Key): String = "/${PackResources.DATA_FOLDER_NAME}/${location.namespace()}/${location.value()}"

        @JvmStatic
        @OptIn(ExperimentalContracts::class)
        private fun isResourceUrlValid(path: String, url: URL?): Boolean {
            contract { returns(true) implies (url != null) }
            return url != null && (url.protocol == "jar" || FolderPackResources.validatePath(Path.of(url.toURI()), path))
        }

        @JvmStatic
        private fun getResources(output: MutableCollection<Key>, namespace: String, path: Path, name: String, predicate: Predicate<Key>) {
            val folder = path.resolve(namespace)
            Files.walk(folder.resolve(name)).use { files ->
                val mapper = BiConsumer<Path, Consumer<Key>> { value, consumer ->
                    val fileName = folder.relativize(value).toString().replace("\\\\", "/")
                    val key = Keys.create(namespace, fileName)
                    if (key == null) {
                        LOGGER.error("Invalid path in data pack: $namespace:$fileName!")
                    } else {
                        consumer.accept(key)
                    }
                }
                files.filter { !it.endsWith(PackResources.METADATA_EXTENSION) && Files.isRegularFile(it) }
                    .mapMulti(mapper)
                    .filter(predicate)
                    .forEach(output::add)
            }
        }
    }
}

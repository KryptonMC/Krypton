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

import com.google.common.collect.ImmutableMap
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.util.ImmutableSets
import java.io.IOException
import java.net.URI
import java.nio.file.FileSystemAlreadyExistsException
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.EnumMap

class VanillaPackResourcesBuilder {

    private val rootPaths = LinkedHashSet<Path>()
    private val pathsForType = EnumMap<PackType, MutableSet<Path>>(PackType::class.java)
    private var metadata = BuiltInMetadata.of()
    private val namespaces = HashSet<String>()

    private fun validateDirectoryPath(path: Path): Boolean {
        if (!Files.exists(path)) return false
        require(Files.isDirectory(path)) { "Path ${path.toAbsolutePath()} is not a directory!" }
        return true
    }

    private fun pushRootPath(path: Path) {
        if (validateDirectoryPath(path)) rootPaths.add(path)
    }

    private fun pushPathForType(type: PackType, path: Path) {
        if (validateDirectoryPath(path)) pathsForType.computeIfAbsent(type) { LinkedHashSet() }.add(path)
    }

    fun pushJarResources(): VanillaPackResourcesBuilder = apply {
        ROOT_DIRECTORY_BY_TYPE.forEach { (type, path) ->
            pushRootPath(path.parent)
            pushPathForType(type, path)
        }
    }

    fun pushClasspathResources(type: PackType, clazz: Class<*>): VanillaPackResourcesBuilder = apply {
        val entries = try {
            clazz.classLoader.getResources(type.directory + "/")
        } catch (_: IOException) {
            null
        }

        while (entries != null && entries.hasMoreElements()) {
            val url = entries.nextElement()
            try {
                val uri = url.toURI()
                if (uri.scheme == "file") {
                    val path = Path.of(uri)
                    pushRootPath(path.parent)
                    pushPathForType(type, path)
                }
            } catch (exception: Exception) {
                LOGGER.error("Failed to extract path from $url!", exception)
            }
        }
    }

    fun pushUniversalPath(path: Path): VanillaPackResourcesBuilder = apply {
        pushRootPath(path)
        PackType.values().forEach { pushPathForType(it, path.resolve(it.directory)) }
    }

    fun pushAssetPath(type: PackType, path: Path): VanillaPackResourcesBuilder = apply {
        pushRootPath(path)
        pushPathForType(type, path)
    }

    fun metadata(metadata: BuiltInMetadata): VanillaPackResourcesBuilder = apply { this.metadata = metadata }

    fun exposeNamespaces(vararg namespaces: String): VanillaPackResourcesBuilder = apply { this.namespaces.addAll(namespaces.asList()) }

    fun build(): VanillaPackResources {
        val pathsByType = EnumMap<PackType, List<Path>>(PackType::class.java)
        PackType.values().forEach { pathsByType.put(it, copyAndReverse(pathsForType.getOrDefault(it, ImmutableSets.of()))) }
        return VanillaPackResources(metadata, ImmutableSets.copyOf(namespaces), copyAndReverse(rootPaths), pathsByType)
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private val ROOT_DIRECTORY_BY_TYPE = getRootDirectoriesForPackTypes()

        @JvmStatic
        private fun getRootDirectoriesForPackTypes(): Map<PackType, Path> {
            synchronized(VanillaPackResources::class.java) {
                val result = ImmutableMap.builder<PackType, Path>()
                PackType.values().forEach { type ->
                    val assetsMarker = "/${type.directory}/.mcassetsroot"
                    val resourceUrl = VanillaPackResources::class.java.getResource(assetsMarker)
                    if (resourceUrl == null) {
                        LOGGER.error("Could not find assets marker $assetsMarker in classpath!")
                        return@forEach
                    }
                    try {
                        val uri = resourceUrl.toURI()
                        val scheme = uri.scheme
                        if (scheme != "jar" && scheme != "file") LOGGER.warn("Assets marker URI $uri uses unexpected schema.")
                        val path = safeGetPath(uri)
                        result.put(type, path.parent)
                    } catch (exception: Exception) {
                        LOGGER.error("Failed to resolve path to vanilla assets!", exception)
                    }
                }
                return result.build()
            }
        }

        @JvmStatic
        private fun safeGetPath(uri: URI): Path {
            try {
                return Path.of(uri)
            } catch (_: FileSystemNotFoundException) {
                // Should not be possible to throw this
            } catch (exception: Throwable) {
                LOGGER.warn("Unable to get path for $uri!", exception)
            }
            try {
                FileSystems.newFileSystem(uri, emptyMap<String, Any>())
            } catch (_: FileSystemAlreadyExistsException) {
                // Not possible because if it already existed, we would've returned from the first try.
            }
            return Path.of(uri)
        }

        @JvmStatic
        private fun copyAndReverse(paths: Collection<Path>): List<Path> = ImmutableLists.copyOf(ArrayList(paths).apply { reverse() })
    }
}

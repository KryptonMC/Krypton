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

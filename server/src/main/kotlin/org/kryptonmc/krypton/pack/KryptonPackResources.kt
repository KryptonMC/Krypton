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

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.resource.Resource
import org.kryptonmc.krypton.resource.ResourceProvider
import org.kryptonmc.krypton.util.logger
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.util.stream.Collectors
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.isRegularFile

class KryptonPackResources(
    override val metadata: PackMetadata,
    override val namespaces: Set<String>
) : PackResources, ResourceProvider {

    override val name = "Default"

    override fun root(path: String): InputStream? {
        require("/" !in path && "\\" !in path) { "Root resources can only be file names!" }
        if (generatedDirectory != null) {
            val relative = generatedDirectory!!.resolve(path)
            if (relative.exists()) return relative.inputStream()
        }
        return javaClass.getResourceAsStream("/$path")
    }

    override fun resource(key: Key) = getResource(key) ?: throw FileNotFoundException(key.value())

    override fun resources(namespace: String, value: String, maxDepth: Int, predicate: (String) -> Boolean): Collection<Key> {
        val resources = mutableSetOf<Key>()
        if (generatedDirectory != null) try {
            resources += generatedDirectory!!.resolve("data").getResources(maxDepth, namespace, value, predicate)
        } catch (ignored: IOException) {}
        try {
            resources += EMBEDDED_PATH.getResources(maxDepth, namespace, value, predicate)
        } catch (exception: NoSuchFileException) {
        } catch (exception: FileNotFoundException) {
        } catch (exception: IOException) {
            LOGGER.error("Failed to retrieve the list of built-in resources!", exception)
        }
        return resources
    }

    override fun contains(key: Key): Boolean {
        val path = "/data/${key.namespace()}/${key.value()}"
        if (generatedDirectory != null && generatedDirectory!!.resolve("data/${key.namespace()}/${key.value()}").exists()) return true
        return try {
            javaClass.getResource(path).isValid(path)
        } catch (exception: IOException) {
            false
        }
    }

    override fun invoke(p1: Key) = object : Resource {

        override val sourceName = p1.asString()
        override val location = p1
        override val hasMetadata = false
        override val metadata: PackMetadata? = null
        override val inputStream = getResource(p1)!!

        override fun close() = inputStream.close()
    }

    override fun close() = Unit

    private fun getResource(key: Key): InputStream? {
        val path = "/data/${key.namespace()}/${key.value()}"
        if (generatedDirectory != null) {
            val relative = generatedDirectory!!.resolve("data/${key.namespace()}/${key.value()}")
            if (relative.exists()) try {
                return relative.inputStream()
            } catch (ignored: IOException) {}
        }
        return try {
            val url = javaClass.getResource(path)
            if (url.isValid(path)) url!!.openStream() else null
        } catch (exception: IOException) {
            javaClass.getResourceAsStream(path)
        }
    }

    companion object {

        var generatedDirectory: Path? = null
        private val LOGGER = logger<KryptonPackResources>()
        private val EMBEDDED_PATH: Path = kotlin.run {
            val markerPath = "/data/.krypton"
            val url = KryptonPackResources::class.java.getResource(markerPath) ?: error("Krypton data missing from JAR file!")
            try {
                val uri = url.toURI()
                val scheme = uri.scheme
                if (scheme != "jar" && scheme != "file") LOGGER.warn("Data URL $uri uses unexpected scheme $scheme, should be either jar or file")
                Path.of(uri).parent
            } catch (exception: Exception) {
                LOGGER.error("Failed to resolve path to built-in data!")
                throw exception
            }
        }
    }
}

private fun URL?.isValid(ending: String) = this != null && (protocol == "jar" || Path.of(toURI()).validate(ending))

private fun Path.getResources(maxDepth: Int, namespace: String, key: String, predicate: (String) -> Boolean): Collection<Key> {
    val path = resolve(namespace)
    return Files.walk(path.resolve(key), maxDepth).use { stream ->
        stream.filter { !it.toString().endsWith(PackResources.METADATA_EXTENSION) && it.isRegularFile() && predicate(it.fileName.toString()) }
            .map { Key.key(namespace, path.relativize(it).toString().replace("\\\\", "/")) }
            .collect(Collectors.toList())
    }
}

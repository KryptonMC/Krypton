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
package org.kryptonmc.krypton.resource

import net.kyori.adventure.key.Key
import org.apache.logging.log4j.Logger
import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.util.logger
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.FilterInputStream
import java.io.IOException
import java.io.InputStream
import java.io.PrintStream
import java.util.Collections

class FallbackResourceManager(private val namespace: String) : ResourceManager {

    private val fallbacks = mutableListOf<PackResources>()
    override val namespaces = setOf(namespace)
    override val packs: Sequence<PackResources>
        get() = fallbacks.asSequence()

    fun add(pack: PackResources) = fallbacks.add(pack)

    override fun contains(key: Key): Boolean {
        if (!key.isValid()) return false
        return fallbacks.any { key in it }
    }

    override fun resources(key: Key): List<Resource> {
        key.validate()
        val resources = mutableListOf<Resource>()
        val metadataKey = key.metadataKey()
        fallbacks.forEach {
            if (key !in it) return@forEach
            val metadataStream = if (metadataKey in it) getWrappedResource(metadataKey, it) else null
            resources.add(SimpleResource(it.name, key, getWrappedResource(key, it), metadataStream))
        }
        return resources.ifEmpty { throw FileNotFoundException(key.asString()) }
    }

    override fun list(name: String, predicate: (String) -> Boolean): Collection<Key> {
        val resources = mutableListOf<Key>()
        fallbacks.forEach { resources.addAll(it.resources(namespace, name, Int.MAX_VALUE, predicate)) }
        return resources.apply { sort() }
    }

    override fun invoke(p1: Key): Resource {
        p1.validate()
        var pack: PackResources? = null
        val metadataKey = p1.metadataKey()
        for (i in fallbacks.size - 1 downTo 0) {
            val packResources = fallbacks[i]
            if (pack == null && metadataKey in packResources) pack = packResources
            if (p1 in packResources) {
                val metadataStream = pack?.let { getWrappedResource(metadataKey, it) }
                return SimpleResource(packResources.name, p1, getWrappedResource(p1, packResources), metadataStream)
            }
        }
        throw FileNotFoundException(p1.asString())
    }

    private fun getWrappedResource(key: Key, pack: PackResources): InputStream {
        val stream = pack.resource(key)!!
        return if (LOGGER.isDebugEnabled) LeakedResourceWarningInputStream(stream, key, pack.name, LOGGER) else stream
    }

    companion object {

        private val LOGGER = logger<FallbackResourceManager>()
    }
}

private fun Key.isValid() = ".." !in value()

private fun Key.validate() {
    if (!isValid()) throw IOException("Invalid relative path to resource $this!")
}

private fun Key.metadataKey() = Key.key(namespace(), value() + PackResources.METADATA_EXTENSION)

private class LeakedResourceWarningInputStream(
    stream: InputStream,
    key: Key,
    name: String,
    private val logger: Logger
) : FilterInputStream(stream) {

    private val message: String
    private var closed = false

    init {
        val output = ByteArrayOutputStream()
        Exception().printStackTrace(PrintStream(output))
        message = "Leaked resource $key loaded from pack $name\n$output"
    }

    override fun close() {
        super.close()
        closed = true
    }

    protected fun finalize() {
        if (!closed) logger.warn(message)
    }
}

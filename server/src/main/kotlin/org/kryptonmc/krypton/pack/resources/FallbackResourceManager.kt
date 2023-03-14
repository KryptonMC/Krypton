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
package org.kryptonmc.krypton.pack.resources

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.pack.PackType
import java.io.FilterInputStream
import java.io.InputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.util.TreeMap
import java.util.function.Predicate
import java.util.function.Supplier

class FallbackResourceManager(private val type: PackType, private val namespace: String) : ResourceManager {

    private val fallbacks = ArrayList<PackEntry>()

    fun push(resources: PackResources) {
        pushInternal(resources.packId(), resources, null)
    }

    fun push(resources: PackResources, predicate: Predicate<Key>) {
        pushInternal(resources.packId(), resources, predicate)
    }

    fun pushFilterOnly(name: String, predicate: Predicate<Key>) {
        pushInternal(name, null, predicate)
    }

    private fun pushInternal(name: String, resources: PackResources?, filter: Predicate<Key>?) {
        fallbacks.add(PackEntry(name, resources, filter))
    }

    override fun getResource(location: Key): Resource? {
        for (i in fallbacks.size - 1 downTo 0) {
            val entry = fallbacks.get(i)
            val resources = entry.resources
            if (resources != null) {
                val stream = resources.getResource(type, location)
                if (stream != null) return createResource(resources, location, stream, createStackMetadataFinder(location, i))
            }
            if (entry.isFiltered(location)) {
                LOGGER.warn("Resource $location not found, but was filtered by pack ${entry.name}.")
                return null
            }
        }
        return null
    }

    override fun listResources(path: String, predicate: Predicate<Key>): Map<Key, Resource> {
        val resourceData = HashMap<Key, ResourceWithSourceAndIndex>()
        val metadata = HashMap<Key, ResourceWithSourceAndIndex>()
        val fallbackCount = fallbacks.size

        for (i in 0 until fallbackCount) {
            val entry = fallbacks.get(i)
            entry.filterAll(resourceData.keys)
            entry.filterAll(metadata.keys)
            val resources = entry.resources ?: continue
            resources.listResources(type, namespace, path) { key, stream ->
                if (isMetadata(key)) {
                    if (predicate.test(getResourceLocationFromMetadata(key))) metadata.put(key, ResourceWithSourceAndIndex(resources, stream, i))
                } else if (predicate.test(key)) {
                    resourceData.put(key, ResourceWithSourceAndIndex(resources, stream, i))
                }
            }
        }

        val result = TreeMap<Key, Resource>()
        resourceData.forEach { (key, data) ->
            val location = getMetadataLocation(key)
            val meta = metadata.get(location)
            val metadataSupplier = if (meta != null && meta.packIndex >= data.packIndex) {
                convertToMetadata(meta.resource)
            } else {
                ResourceMetadata.EMPTY_SUPPLIER
            }
            result.put(key, createResource(data.source, key, data.resource, metadataSupplier))
        }
        return result
    }

    private fun createStackMetadataFinder(location: Key, index: Int): Supplier<ResourceMetadata> = Supplier {
        val metadataLocation = getMetadataLocation(location)
        for (i in fallbacks.size - 1 downTo index) {
            val entry = fallbacks.get(i)
            val resources = entry.resources
            if (resources != null) {
                val stream = resources.getResource(type, metadataLocation)
                if (stream != null) return@Supplier parseMetadata(stream)
            }
            if (entry.isFiltered(metadataLocation)) break
        }
        ResourceMetadata.EMPTY
    }

    private fun listPackResources(entry: PackEntry, path: String, predicate: Predicate<Key>, output: MutableMap<Key, EntryStack>) {
        val resources = entry.resources ?: return
        resources.listResources(type, namespace, path) { key, stream ->
            if (isMetadata(key)) {
                val metadataLocation = getResourceLocationFromMetadata(key)
                if (!predicate.test(metadataLocation)) return@listResources
                output.computeIfAbsent(metadataLocation) { EntryStack(metadataLocation) }.metaSources.put(resources, stream)
            } else {
                if (!predicate.test(key)) return@listResources
                output.computeIfAbsent(key) { EntryStack(key) }.fileSources.add(ResourceWithSource(resources, stream))
            }
        }
    }

    override fun listResourceStacks(path: String, predicate: Predicate<Key>): Map<Key, List<Resource>> {
        val entries = HashMap<Key, EntryStack>()
        fallbacks.forEach {
            applyPackFiltersToExistingResources(it, entries)
            listPackResources(it, path, predicate, entries)
        }

        val result = TreeMap<Key, List<Resource>>()
        entries.values.forEach { stack ->
            if (stack.fileSources.isEmpty()) return@forEach
            val resources = ArrayList<Resource>()
            stack.fileSources.forEach {
                val source = it.source
                val metaStream = stack.metaSources.get(source)
                val meta = if (metaStream != null) convertToMetadata(metaStream) else ResourceMetadata.EMPTY_SUPPLIER
                resources.add(createResource(source, stack.fileLocation, it.resource, meta))
            }
            result.put(stack.fileLocation, resources)
        }
        return result
    }

    @JvmRecord
    private data class EntryStack(val fileLocation: Key, val metadataLocation: Key, val fileSources: MutableList<ResourceWithSource>,
                                  val metaSources: MutableMap<PackResources, Supplier<InputStream>>) {

        constructor(fileLocation: Key) : this(fileLocation, getMetadataLocation(fileLocation), ArrayList(), Object2ObjectArrayMap())
    }

    private class LeakedResourceWarningInputStream(input: InputStream, location: Key, packId: String) : FilterInputStream(input) {

        private val message: Supplier<String>
        private var closed = false

        init {
            val exception = Exception("Stacktrace")
            message = Supplier {
                val writer = StringWriter()
                exception.printStackTrace(PrintWriter(writer))
                "Leaked resource $location loaded from pack $packId\n$writer"
            }
        }

        override fun close() {
            super.close()
            closed = true
        }

        @Suppress("ProtectedMemberInFinalClass")
        protected fun finalize() {
            if (!closed) LOGGER.warn(message.get())
        }
    }

    @JvmRecord
    private data class PackEntry(val name: String, val resources: PackResources?, val filter: Predicate<Key>?) {

        fun filterAll(locations: MutableCollection<Key>) {
            if (filter != null) locations.removeIf(filter)
        }

        fun isFiltered(location: Key): Boolean = filter != null && filter.test(location)
    }

    @JvmRecord
    private data class ResourceWithSource(val source: PackResources, val resource: Supplier<InputStream>)

    @JvmRecord
    private data class ResourceWithSourceAndIndex(val source: PackResources, val resource: Supplier<InputStream>, val packIndex: Int)

    companion object {

        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        private fun createResource(resources: PackResources, location: Key, stream: Supplier<InputStream>,
                                   metadata: Supplier<ResourceMetadata>): Resource {
            return Resource(resources, wrapForDebug(location, resources, stream), metadata)
        }

        @JvmStatic
        private fun wrapForDebug(location: Key, resources: PackResources, stream: Supplier<InputStream>): Supplier<InputStream> {
            if (LOGGER.isDebugEnabled) return Supplier { LeakedResourceWarningInputStream(stream.get(), location, resources.packId()) }
            return stream
        }

        @JvmStatic
        private fun isMetadata(location: Key): Boolean = location.value().endsWith(PackResources.METADATA_EXTENSION)

        @JvmStatic
        private fun getResourceLocationFromMetadata(location: Key): Key =
            Key.key(location.namespace(), location.value().substring(0, location.value().length - PackResources.METADATA_EXTENSION.length))

        @JvmStatic
        private fun getMetadataLocation(location: Key): Key = Key.key(location.namespace(), location.value() + PackResources.METADATA_EXTENSION)

        @JvmStatic
        private fun convertToMetadata(stream: Supplier<InputStream>): Supplier<ResourceMetadata> = Supplier { parseMetadata(stream) }

        @JvmStatic
        private fun parseMetadata(supplier: Supplier<InputStream>): ResourceMetadata = supplier.get().use { ResourceMetadata.fromJsonStream(it) }

        @JvmStatic
        private fun applyPackFiltersToExistingResources(entry: PackEntry, output: MutableMap<Key, EntryStack>) {
            output.values.forEach { stack ->
                if (entry.isFiltered(stack.fileLocation)) {
                    stack.fileSources.clear()
                } else if (entry.isFiltered(stack.metadataLocation)) {
                    stack.metaSources.clear()
                }
            }
        }
    }
}

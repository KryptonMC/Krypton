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
package org.kryptonmc.krypton.pack.resources

import it.unimi.dsi.fastutil.objects.Object2IntMaps
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.util.logger
import java.io.ByteArrayOutputStream
import java.io.FilterInputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.util.TreeMap
import java.util.function.Predicate
import java.util.function.Supplier

class FallbackResourceManager(private val namespace: String) : ResourceManager {

    private val fallbacks = ArrayList<PackEntry>()

    fun push(resources: PackResources) {
        pushInternal(resources.name, resources, null)
    }

    fun push(resources: PackResources, predicate: Predicate<Key>) {
        pushInternal(resources.name, resources, predicate)
    }

    fun pushFilterOnly(name: String, predicate: Predicate<Key>) {
        pushInternal(name, null, predicate)
    }

    private fun pushInternal(name: String, resources: PackResources?, filter: Predicate<Key>?) {
        fallbacks.add(PackEntry(name, resources, filter))
    }

    override fun getResource(location: Key): Resource? {
        if (!isValidLocation(location)) return null
        for (i in fallbacks.size - 1 downTo 0) {
            val entry = fallbacks.get(i)
            val resources = entry.resources
            if (resources != null && resources.hasResource(location)) {
                return Resource(resources.name, createResourceGetter(location, resources), createStackMetadataFinder(location, i))
            }
            if (entry.isFiltered(location)) {
                LOGGER.warn("Resource $location not found, but was filtered by pack ${entry.name}")
                return null
            }
        }
        return null
    }

    override fun listResources(path: String, predicate: Predicate<Key>): Map<Key, Resource> {
        val resourceIds = Object2IntOpenHashMap<Key>()
        val fallbackSize = fallbacks.size
        for (i in 0 until fallbackSize) {
            val entry = fallbacks.get(i)
            entry.filterAll(resourceIds.keys)
            if (entry.resources != null) {
                entry.resources.getResources(namespace, path, predicate).forEach { resourceIds.put(it, i) }
            }
        }
        val resources = TreeMap<Key, Resource>()
        Object2IntMaps.fastForEach(resourceIds) {
            val id = it.intValue
            val key = it.key
            val packResources = fallbacks.get(id).resources!!
            resources.put(key, Resource(packResources.name, createResourceGetter(key, packResources), createStackMetadataFinder(key, id)))
        }
        return resources
    }

    override fun listResourceStacks(path: String, predicate: Predicate<Key>): Map<Key, List<Resource>> {
        val entries = HashMap<Key, EntryStack>()
        fallbacks.forEach {
            applyPackFiltersToExistingResources(it, entries)
            listPackResources(it, path, predicate, entries)
        }
        val result = TreeMap<Key, List<Resource>>()
        entries.forEach { result.put(it.key, it.value.createThunks()) }
        return result
    }

    private fun listPackResources(entry: PackEntry, path: String, predicate: Predicate<Key>, output: MutableMap<Key, EntryStack>) {
        val resources = entry.resources ?: return
        resources.getResources(namespace, path, predicate).forEach {
            val metadataLocation = getMetadataLocation(it)
            val thunkSupplier = SinglePackResourceThunkSupplier(it, metadataLocation, resources)
            output.computeIfAbsent(it) { EntryStack(metadataLocation, ArrayList()) }.entries.add(thunkSupplier)
        }
    }

    private fun createResourceGetter(location: Key, resources: PackResources): Supplier<InputStream> {
        if (!LOGGER.isDebugEnabled) return Supplier { resources.getResource(location) }
        return Supplier { LeakedResourceWarningInputStream(resources.getResource(location), location, resources.name) }
    }

    private fun createStackMetadataFinder(location: Key, index: Int): Supplier<ResourceMetadata> = Supplier {
        val metadataLocation = getMetadataLocation(location)
        for (i in fallbacks.size - 1 downTo index) {
            val entry = fallbacks.get(i)
            val resources = entry.resources
            if (resources != null && resources.hasResource(metadataLocation)) {
                return@Supplier resources.getResource(location).use(ResourceMetadata::fromJsonStream)
            }
            if (entry.isFiltered(metadataLocation)) break
        }
        ResourceMetadata.EMPTY
    }

    private fun isValidLocation(location: Key): Boolean = !location.value().contains("..")

    @JvmRecord
    private data class EntryStack(val metadataLocation: Key, val entries: MutableList<SinglePackResourceThunkSupplier>) {

        fun createThunks(): List<Resource> = entries.map(SinglePackResourceThunkSupplier::create)
    }

    private class LeakedResourceWarningInputStream(input: InputStream, location: Key, packId: String) : FilterInputStream(input) {

        private val message = "Leaked resource $location loaded from pack $packId\n${generateStackTrace()}"
        private var closed = false

        override fun close() {
            super.close()
            closed = true
        }

        @Suppress("ProtectedMemberInFinalClass")
        protected fun finalize() {
            if (!closed) LOGGER.warn(message)
        }

        companion object {

            @JvmStatic
            private fun generateStackTrace(): OutputStream {
                val output = ByteArrayOutputStream()
                @Suppress("ThrowingExceptionsWithoutMessageOrCause")
                Exception().printStackTrace(PrintStream(output))
                return output
            }
        }
    }

    @JvmRecord
    private data class PackEntry(val name: String, val resources: PackResources?, val filter: Predicate<Key>?) {

        fun filterAll(locations: MutableCollection<Key>) {
            if (filter != null) locations.removeIf(filter)
        }

        fun isFiltered(location: Key): Boolean = filter != null && filter.test(location)
    }

    private inner class SinglePackResourceThunkSupplier(
        private val location: Key,
        private val metadataLocation: Key,
        private val source: PackResources
    ) {

        private var shouldGetMetadata = true

        fun ignoreMetadata() {
            shouldGetMetadata = false
        }

        fun create(): Resource {
            if (!shouldGetMetadata) return Resource(source.name, createResourceGetter(location, source))
            return Resource(source.name, createResourceGetter(location, source)) {
                if (!source.hasResource(metadataLocation)) return@Resource ResourceMetadata.EMPTY
                source.getResource(metadataLocation).use(ResourceMetadata::fromJsonStream)
            }
        }
    }

    companion object {

        private val LOGGER = logger<FallbackResourceManager>()

        @JvmStatic
        private fun getMetadataLocation(location: Key): Key = Key.key(location.namespace(), location.value() + PackResources.METADATA_EXTENSION)

        @JvmStatic
        private fun applyPackFiltersToExistingResources(entry: PackEntry, output: MutableMap<Key, EntryStack>) {
            val iterator = output.entries.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (entry.isFiltered(next.key)) {
                    iterator.remove()
                } else if (entry.isFiltered(next.value.metadataLocation)) {
                    next.value.entries.forEach(SinglePackResourceThunkSupplier::ignoreMetadata)
                }
            }
        }
    }
}

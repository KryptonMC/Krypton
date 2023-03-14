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

import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.pack.PackType
import org.kryptonmc.krypton.pack.metadata.ResourceFilterSection
import org.kryptonmc.krypton.util.ImmutableLists
import java.io.IOException
import java.util.TreeMap
import java.util.function.Predicate

class MultiPackResourceManager(type: PackType, packs: List<PackResources>) : CloseableResourceManager {

    private val namespacedManagers: Map<String, FallbackResourceManager>
    private val packs = ImmutableLists.copyOf(packs)

    init {
        val managers = HashMap<String, FallbackResourceManager>()
        val namespaces = packs.asSequence().flatMap { it.namespaces(type) }.distinct()
        packs.forEach { resources ->
            val filterData = getPackFilterSection(resources)
            val packNamespaces = resources.namespaces(type)
            val predicate: Predicate<Key>? = if (filterData != null) Predicate { filterData.isPathFiltered(it.value()) } else null

            namespaces.forEach inner@{ namespace ->
                val inPackNamespaces = packNamespaces.contains(namespace)
                val filtered = filterData != null && filterData.isNamespaceFiltered(namespace)
                if (!inPackNamespaces && !filtered) return@inner
                val manager = managers.computeIfAbsent(namespace) { FallbackResourceManager(type, it) }
                if (inPackNamespaces && filtered) {
                    // predicate must be non-null as filterData != null if filtered is true, and if filterData != null, predicate != null
                    manager.push(resources, predicate!!)
                } else if (inPackNamespaces) {
                    manager.push(resources)
                } else {
                    // predicate must be non-null as filterData != null if filtered is true, and if filterData != null, predicate != null
                    manager.pushFilterOnly(resources.packId(), predicate!!)
                }
            }
        }
        namespacedManagers = managers
    }

    private fun getPackFilterSection(resources: PackResources): ResourceFilterSection? {
        return try {
            resources.getMetadataSection(ResourceFilterSection.SERIALIZER)
        } catch (exception: IOException) {
            LOGGER.error("Failed to get filter section from pack ${resources.packId()}!", exception)
            null
        }
    }

    override fun getResource(location: Key): Resource? = namespacedManagers.get(location.namespace())?.getResource(location)

    override fun listResources(path: String, predicate: Predicate<Key>): Map<Key, Resource> {
        checkTrailingDirectoryPath(path)
        val result = TreeMap<Key, Resource>()
        namespacedManagers.values.forEach { result.putAll(it.listResources(path, predicate)) }
        return result
    }

    override fun listResourceStacks(path: String, predicate: Predicate<Key>): Map<Key, List<Resource>> {
        checkTrailingDirectoryPath(path)
        val result = TreeMap<Key, List<Resource>>()
        namespacedManagers.values.forEach { result.putAll(it.listResourceStacks(path, predicate)) }
        return result
    }

    override fun close() {
        packs.forEach { it.close() }
    }

    companion object {

        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        private fun checkTrailingDirectoryPath(path: String) {
            require(!path.endsWith("/")) { "Trailing slash path $path!" }
        }
    }
}

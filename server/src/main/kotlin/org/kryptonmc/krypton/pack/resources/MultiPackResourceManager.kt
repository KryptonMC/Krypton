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

import kotlinx.collections.immutable.toImmutableList
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.pack.metadata.ResourceFilterData
import org.kryptonmc.krypton.util.logger
import java.io.IOException
import java.util.TreeMap
import java.util.function.Predicate

class MultiPackResourceManager(packs: List<PackResources>) : CloseableResourceManager {

    private val packs = packs.toImmutableList()
    private val namespacedManagers: Map<String, FallbackResourceManager>

    init {
        val managers = HashMap<String, FallbackResourceManager>()
        val namespaces = packs.asSequence().flatMap { it.namespaces }.distinct().toList()
        packs.forEach { resources ->
            val filterData = getPackFilterData(resources)
            val packNamespaces = resources.namespaces
            val predicate: Predicate<Key>? = if (filterData != null) Predicate { filterData.isPathFiltered(it.value()) } else null
            namespaces.forEach inner@{
                val inPackNamespaces = packNamespaces.contains(it)
                val filtered = filterData != null && filterData.isNamespaceFiltered(it)
                if (!inPackNamespaces && !filtered) return@inner
                val manager = managers.getOrPut(it) { FallbackResourceManager(it) }
                if (inPackNamespaces && filtered) {
                    // predicate must be non-null as filterData != null if filtered is true, and if filterData != null, predicate != null
                    manager.push(resources, predicate!!)
                } else if (inPackNamespaces) {
                    manager.push(resources)
                } else {
                    // predicate must be non-null as filterData != null if filtered is true, and if filterData != null, predicate != null
                    manager.pushFilterOnly(resources.name, predicate!!)
                }
            }
        }
        namespacedManagers = managers
    }

    override fun getResource(location: Key): Resource? = namespacedManagers[location.namespace()]?.getResource(location)

    override fun listResources(path: String, predicate: Predicate<Key>): Map<Key, Resource> {
        val result = TreeMap<Key, Resource>()
        namespacedManagers.values.forEach { result.putAll(it.listResources(path, predicate)) }
        return result
    }

    override fun listResourceStacks(path: String, predicate: Predicate<Key>): Map<Key, List<Resource>> {
        val result = TreeMap<Key, List<Resource>>()
        namespacedManagers.values.forEach { result.putAll(it.listResourceStacks(path, predicate)) }
        return result
    }

    override fun close() {
        packs.forEach(PackResources::close)
    }

    private fun getPackFilterData(resources: PackResources): ResourceFilterData? {
        return try {
            resources.getMetadata(ResourceFilterData.SERIALIZER)
        } catch (exception: IOException) {
            LOGGER.error("Failed to get filter section from pack ${resources.name}!", exception)
            null
        }
    }

    companion object {

        private val LOGGER = logger<MultiPackResourceManager>()
    }
}

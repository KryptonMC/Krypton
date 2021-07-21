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
package org.kryptonmc.krypton.tags

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.google.gson.Gson
import com.google.gson.JsonObject
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.resource.ResourceManager
import org.kryptonmc.krypton.util.logger

class TagLoader<T : Any>(
    private val idToValue: (Key) -> T?,
    private val directory: String
) {

    fun load(manager: ResourceManager): Map<Key, Tag.Builder> {
        val map = mutableMapOf<Key, Tag.Builder>()
        manager.list(directory) { it.endsWith(PATH_SUFFIX) }.forEach { resourceKey ->
            val path = resourceKey.value()
            val key = Key.key(resourceKey.namespace(), path.substring(directory.length + 1, path.length - PATH_SUFFIX_LENGTH))
            try {
                manager.resources(key).forEach { resource ->
                    resource.inputStream.reader().use {
                        val json = GSON.fromJson(it, JsonObject::class.java)
                        if (json == null) {
                            LOGGER.error("Failed to load tag list $key from $resourceKey in data pack ${resource.sourceName} as it is empty or null!")
                            return@use
                        }
                        map.getOrPut(key) { Tag.Builder() }.add(json, resource.sourceName)
                    }
                }
            } catch (exception: Exception) {
                LOGGER.error("Failed to read tag list $key from $resourceKey")
            }
        }
        return map
    }

    fun build(builders: Map<Key, Tag.Builder>): TagCollection<T> {
        val tags = mutableMapOf<Key, Tag<T>>()
        val getter: (Key) -> Tag<T>? = tags::get
        val dependencies = HashMultimap.create<Key, Key>()
        builders.forEach { (key, value) -> value.visitRequiredDependencies { dependencies.addIfNotCyclic(key, it) } }
        builders.forEach { (key, value) -> value.visitOptionalDependencies { dependencies.addIfNotCyclic(key, it) } }
        val keys = mutableSetOf<Key>()
        builders.keys.forEach { builderKey ->
            builders.visit(dependencies, builderKey, keys) { key, builder ->
                builder.build(getter, idToValue)
                    .ifLeft { LOGGER.error("Failed to load tag $key as it is missing the following references: ${it.joinToString(transform = Any::toString)}") }
                    .ifRight { tags[key] = it }
            }
        }
        return TagCollection.of(tags)
    }

    fun loadAndBuild(manager: ResourceManager) = build(load(manager))

    companion object {

        private val LOGGER = logger<TagLoader<*>>()
        private val GSON = Gson()
        private const val PATH_SUFFIX = ".json"
        private const val PATH_SUFFIX_LENGTH = PATH_SUFFIX.length
    }
}

private fun Map<Key, Tag.Builder>.visit(dependencies: Multimap<Key, Key>, key: Key, output: MutableSet<Key>, consumer: (Key, Tag.Builder) -> Unit) {
    if (!output.add(key)) return
    dependencies[key].forEach { visit(dependencies, it, output, consumer) }
    get(key)?.let { consumer(key, it) }
}

private fun Multimap<Key, Key>.addIfNotCyclic(key1: Key, key2: Key) {
    if (!isCyclic(key1, key2)) put(key1, key2)
}

private fun Multimap<Key, Key>.isCyclic(key1: Key, key2: Key): Boolean {
    val elements = get(key2)
    return if (elements.contains(key1)) true else elements.any { isCyclic(key1, it) }
}

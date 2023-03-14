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
package org.kryptonmc.krypton.tags

import com.google.common.collect.HashMultimap
import com.google.common.collect.ImmutableSet
import com.google.common.collect.Multimap
import com.google.gson.JsonParser
import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.pack.resources.ResourceManager
import org.kryptonmc.krypton.resource.FileToIdConverter
import org.kryptonmc.serialization.Dynamic
import org.kryptonmc.serialization.gson.GsonOps
import org.kryptonmc.util.Either
import java.util.function.BiConsumer
import java.util.function.Function

class TagLoader<T>(private val idToValue: Function<Key, T?>, private val directory: String) {

    fun load(resourceManager: ResourceManager): MutableMap<Key, MutableList<EntryWithSource>> {
        val result = HashMap<Key, MutableList<EntryWithSource>>()
        val converter = FileToIdConverter.json(directory)

        converter.listMatchingResourceStacks(resourceManager).entries.forEach { (fileLocation, resourceStack) ->
            val id = converter.fileToId(fileLocation)
            resourceStack.forEach { resource ->
                try {
                    resource.openAsReader().use { reader ->
                        val json = JsonParser.parseReader(reader)
                        val entries = result.computeIfAbsent(id) { ArrayList() }
                        val file = TagFile.CODEC.read(Dynamic(GsonOps.INSTANCE, json)).getOrThrow(false) { LOGGER.error(it) }
                        if (file.replace) entries.clear()
                        file.entries.forEach { entries.add(EntryWithSource(it, resource.sourcePackId())) }
                    }
                } catch (exception: Exception) {
                    LOGGER.error("Failed to read tag list $id from $fileLocation in data pack ${resource.sourcePackId()}", exception)
                }
            }
        }
        return result
    }

    private fun build(lookup: TagEntry.Lookup<T>, entries: List<EntryWithSource>): Either<Collection<EntryWithSource>, Collection<T>> {
        val values = ImmutableSet.builder<T>()
        val missing = ArrayList<EntryWithSource>()
        entries.forEach { entry ->
            if (!entry.entry.build(lookup) { values.add(it!!) }) missing.add(entry)
        }
        return if (missing.isEmpty()) Either.right(values.build()) else Either.left(missing)
    }

    fun build(entries: MutableMap<Key, MutableList<EntryWithSource>>): Map<Key, Collection<T>> {
        val result = HashMap<Key, Collection<T>>()
        val lookup = object : TagEntry.Lookup<T> {
            override fun element(key: Key): T? = idToValue.apply(key)

            override fun tag(key: Key): Collection<T>? = result.get(key)
        }
        val dependencyNames = HashMultimap.create<Key, Key>()
        entries.forEach { (key, value) ->
            value.forEach { entry -> entry.entry.visitRequiredDependencies { addDependencyIfNotCyclic(dependencyNames, key, it) } }
        }
        entries.forEach { (key, value) ->
            value.forEach { entry -> entry.entry.visitOptionalDependencies { addDependencyIfNotCyclic(dependencyNames, key, it) } }
        }
        val names = HashSet<Key>()
        entries.keys.forEach { name ->
            visitDependenciesAndElement(entries, dependencyNames, names, name) { key, sourcedEntries ->
                build(lookup, sourcedEntries)
                    .ifLeft { LOGGER.error("Failed to load tag $key due to missing references! Missing: ${it.joinToString(", ")}") }
                    .ifRight { result.put(key, it) }
            }
        }
        return result
    }

    fun loadAndBuild(resourceManager: ResourceManager): Map<Key, Collection<T>> = build(load(resourceManager))

    @JvmRecord
    data class EntryWithSource(val entry: TagEntry, val source: String) {

        override fun toString(): String = "$entry (from $source)"
    }

    companion object {

        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        private fun visitDependenciesAndElement(entries: MutableMap<Key, MutableList<EntryWithSource>>, dependencyNames: Multimap<Key, Key>,
                                                names: MutableSet<Key>, name: Key, visitor: BiConsumer<Key, List<EntryWithSource>>) {
            if (!names.add(name)) return
            dependencyNames.get(name).forEach { visitDependenciesAndElement(entries, dependencyNames, names, it, visitor) }
            entries.get(name)?.let { visitor.accept(name, it) }
        }

        @JvmStatic
        private fun isCyclic(dependencyNames: Multimap<Key, Key>, name: Key, dependencyName: Key): Boolean {
            val names = dependencyNames.get(dependencyName)
            return if (names.contains(name)) true else names.any { isCyclic(dependencyNames, name, it) }
        }

        @JvmStatic
        private fun addDependencyIfNotCyclic(dependencyNames: Multimap<Key, Key>, name: Key, dependencyName: Key) {
            if (!isCyclic(dependencyNames, name, dependencyName)) dependencyNames.put(name, dependencyName)
        }
    }
}

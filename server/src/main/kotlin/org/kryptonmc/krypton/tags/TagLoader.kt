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

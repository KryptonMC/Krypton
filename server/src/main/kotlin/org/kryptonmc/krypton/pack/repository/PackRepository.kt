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
package org.kryptonmc.krypton.pack.repository

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import com.google.common.collect.Lists
import org.kryptonmc.krypton.pack.PackResources
import java.util.TreeMap
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream

class PackRepository(vararg sources: RepositorySource) {

    private val sources = ImmutableSet.copyOf(sources)
    private var available: Map<String, Pack> = ImmutableMap.of()
    private var selected: List<Pack> = ImmutableList.of()

    fun reload() {
        val currentSelected = selected.stream().map { it.id() }.collect(ImmutableList.toImmutableList())
        available = discoverAvailable()
        selected = rebuildSelected(currentSelected)
    }

    private fun discoverAvailable(): Map<String, Pack> {
        val available = TreeMap<String, Pack>()
        sources.forEach { source -> source.loadPacks { available.put(it.id(), it) } }
        return ImmutableMap.copyOf(available)
    }

    fun setSelected(ids: Collection<String>) {
        selected = rebuildSelected(ids)
    }

    private fun rebuildSelected(ids: Collection<String>): List<Pack> {
        val packs = getAvailablePacks(ids).collect(Collectors.toList())
        available.values.forEach {
            if (it.isRequired() && !packs.contains(it)) it.defaultPosition().insert(packs, it, false, Function.identity())
        }
        return ImmutableList.copyOf(packs)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getAvailablePacks(ids: Collection<String>): Stream<Pack> = ids.stream().map(available::get).filter { it != null } as Stream<Pack>

    fun availableIds(): Collection<String> = available.keys

    fun availablePacks(): Collection<Pack> = available.values

    fun selectedIds(): Collection<String> = Lists.transform(selected) { it.id() }

    fun selectedPacks(): Collection<Pack> = selected

    fun getPackById(id: String): Pack? = available.get(id)

    fun isAvailable(id: String): Boolean = available.containsKey(id)

    fun openAllSelected(): List<PackResources> = Lists.transform(selected) { it.open() }
}

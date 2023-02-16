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

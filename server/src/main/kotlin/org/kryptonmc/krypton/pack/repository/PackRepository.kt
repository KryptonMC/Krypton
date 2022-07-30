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

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import org.kryptonmc.krypton.pack.PackResources
import java.util.TreeMap

class PackRepository(private val sources: Set<RepositorySource>, private val constructor: Pack.Constructor) {

    private var available: Map<String, Pack> = persistentMapOf()
    private var selected: List<Pack> = persistentListOf()

    val availableIds: Collection<String>
        get() = available.keys
    val availablePacks: Collection<Pack>
        get() = available.values
    val selectedIds: Collection<String>
        get() = selected.map { it.id }
    val selectedPacks: Collection<Pack>
        get() = selected

    fun isAvailable(id: String): Boolean = available.containsKey(id)

    fun pack(id: String): Pack? = available[id]

    fun openAllSelected(): List<PackResources> = selected.map(Pack::open)

    fun reload() {
        available = discoverAvailable()
        selected = rebuildSelected(selected.mapTo(mutableSetOf()) { it.id })
    }

    fun setSelected(ids: Collection<String>) {
        selected = rebuildSelected(ids)
    }

    private fun discoverAvailable(): Map<String, Pack> {
        val available = TreeMap<String, Pack>()
        sources.forEach { source -> source.loadPacks(constructor) { available[it.id] = it } }
        return available.toImmutableMap()
    }

    private fun rebuildSelected(ids: Collection<String>): List<Pack> {
        val packs = getAvailablePacks(ids).toMutableList()
        available.values.forEach {
            if (it.required && !packs.contains(it)) it.defaultPosition.insert(packs, it, false) { pack -> pack }
        }
        return packs.toImmutableList()
    }

    private fun getAvailablePacks(ids: Collection<String>): Sequence<Pack> = ids.asSequence().map(available::get).filterNotNull()
}

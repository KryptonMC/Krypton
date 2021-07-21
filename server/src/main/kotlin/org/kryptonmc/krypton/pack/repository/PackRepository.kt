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
package org.kryptonmc.krypton.pack.repository

import org.kryptonmc.krypton.pack.PackResources
import java.util.TreeMap

class PackRepository private constructor(
    private val sources: Set<RepositorySource>,
    private val constructor: (String, () -> PackResources, Pack.Position, Boolean, PackSource) -> Pack
) {

    private var available = emptyMap<String, Pack>()
    var selectedPacks = emptyList<Pack>()
        private set

    constructor(vararg sources: RepositorySource) : this(sources.toSet(), ::Pack)

    operator fun get(id: String) = available[id]

    operator fun contains(id: String) = available.containsKey(id)

    fun reload() {
        val ids = selectedIds
        available = findAvailable()
        selectedPacks = rebuildSelected(ids)
    }

    fun openAllSelected(): List<PackResources> = selectedPacks.map { it.open() }

    fun select(selection: Collection<String>) {
        selectedPacks = rebuildSelected(selection)
    }

    fun isAvailable(id: String) = available.containsKey(id)

    private fun findAvailable(): Map<String, Pack> {
        val available = TreeMap<String, Pack>()
        sources.forEach { source -> source.loadPacks(constructor) { available[it.id] = it } }
        return available.toMap()
    }

    private fun rebuildSelected(selection: Collection<String>): List<Pack> {
        val selectedAvailable = availablePacks(selection).toMutableList()
        available.values.forEach { pack -> if (pack.isRequired && pack !in selectedAvailable) pack.position.insert(selectedAvailable, pack, { it }, false) }
        return selectedAvailable.toList()
    }

    private fun availablePacks(selection: Collection<String>): Sequence<Pack> = selection.asSequence().map { available[it] }.filterNotNull()

    val availableIds: Collection<String>
        get() = available.keys

    val availablePacks: Collection<Pack>
        get() = available.values

    val selectedIds: Collection<String>
        get() = selectedPacks.map { it.id }
}

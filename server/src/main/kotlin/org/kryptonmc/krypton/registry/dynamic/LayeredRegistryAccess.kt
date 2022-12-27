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
package org.kryptonmc.krypton.registry.dynamic

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.util.ImmutableLists

class LayeredRegistryAccess<T> private constructor(keys: List<T>, values: List<RegistryAccess.Frozen>) {

    private val keys = ImmutableLists.copyOf(keys)
    private val values = ImmutableLists.copyOf(values)
    private val composite = RegistryAccess.ImmutableImpl(collectRegistries(values)).freeze()

    constructor(keys: List<T>) : this(keys, Array<RegistryAccess.Frozen>(keys.size) { RegistryAccess.EMPTY }.asList())

    private fun getLayerIndexOrThrow(key: T): Int {
        val index = keys.indexOf(key)
        check(index != -1) { "Cannot find layer $key in $keys!" }
        return index
    }

    fun getLayer(key: T): RegistryAccess.Frozen = values.get(getLayerIndexOrThrow(key))

    /**
     * The resulting access represents registries between index 0 and the index of the given key.
     */
    fun getAccessForLoading(key: T): RegistryAccess.Frozen = getCompositeAccessForLayers(0, getLayerIndexOrThrow(key))

    /**
     * The resulting access represents registries between the index of the given key and the maximum index.
     */
    fun getAccessFrom(key: T): RegistryAccess.Frozen = getCompositeAccessForLayers(getLayerIndexOrThrow(key), values.size)

    /**
     * Gets a registry access that represents registries between the given startIndex and endIndex.
     */
    private fun getCompositeAccessForLayers(startIndex: Int, endIndex: Int): RegistryAccess.Frozen =
        RegistryAccess.ImmutableImpl(collectRegistries(values.subList(startIndex, endIndex))).freeze()

    fun replaceFrom(key: T, vararg values: RegistryAccess.Frozen): LayeredRegistryAccess<T> = replaceFrom(key, values.asList())

    fun replaceFrom(key: T, replacements: List<RegistryAccess.Frozen>): LayeredRegistryAccess<T> {
        val index = getLayerIndexOrThrow(key)
        if (replacements.size > values.size - index) {
            error("Too many values to replace! Expected maximum of ${values.size - index}, got ${replacements.size} values!")
        }
        val newValues = ArrayList<RegistryAccess.Frozen>()
        for (i in 0 until index) {
            newValues.add(values.get(i))
        }
        newValues.addAll(replacements)
        while (newValues.size < values.size) {
            newValues.add(RegistryAccess.EMPTY)
        }
        return LayeredRegistryAccess(keys, newValues)
    }

    fun compositeAccess(): RegistryAccess.Frozen = composite

    companion object {

        @JvmStatic
        private fun collectRegistries(holders: Collection<RegistryAccess>): Map<ResourceKey<out Registry<*>>, KryptonRegistry<*>> {
            val result = HashMap<ResourceKey<out Registry<*>>, KryptonRegistry<*>>()
            holders.forEach { holder ->
                holder.registries().forEach { entry ->
                    if (result.put(entry.key, entry.value) != null) error("Duplicated registry ${entry.key}!")
                }
            }
            return result
        }
    }
}

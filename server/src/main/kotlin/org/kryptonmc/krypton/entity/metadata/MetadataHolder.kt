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
package org.kryptonmc.krypton.entity.metadata

import org.kryptonmc.krypton.entity.KryptonEntity
import space.vectrix.flare.fastutil.Int2ObjectSyncMap

class MetadataHolder(private val entity: KryptonEntity) {

    private val itemsById = Int2ObjectSyncMap.hashmap<Entry<*>>()

    var isDirty: Boolean = false
        private set
    var isEmpty: Boolean = true
        private set
    val all: Sequence<Entry<*>>
        get() = itemsById.int2ObjectEntrySet()
            .asSequence()
            .map { it.value.copy() }
            .apply { invalidate() }
    val dirty: Sequence<Entry<*>>
        get() {
            if (!isDirty) return emptySequence()
            val entries = itemsById.int2ObjectEntrySet()
                .asSequence()
                .filter { it.value.isDirty }
                .map {
                    it.value.isDirty = false
                    it.value.copy()
                }
            isDirty = false
            return entries
        }

    fun <T> add(key: MetadataKey<T>, value: T = key.default) {
        val id = key.id
        require(id <= MAX_ID_VALUE) { "Data value id is too big! Maximum size is $MAX_ID_VALUE, value was $id!" }
        require(!itemsById.containsKey(id)) { "Duplicate id value for $id!" }
        require(key.serializer.id >= 0) { "Unregistered serializer ${key.serializer} for $id!" }
        return createItem(key, value)
    }

    operator fun <T> get(key: MetadataKey<T>): T = entry<T>(key).value

    operator fun <T> set(key: MetadataKey<T>, value: T) {
        val existing = entry(key)
        if (value === existing.value) return
        existing.value = value
        entity.onDataUpdate(key)
        existing.isDirty = true
        isDirty = true
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> entry(key: MetadataKey<T>): Entry<T> = checkNotNull(itemsById[key.id] as? Entry<T>) {
        "Could not find key $key for entity of type ${entity.type}!"
    }

    private fun invalidate() {
        isDirty = false
        itemsById.int2ObjectEntrySet().forEach { it.value.isDirty = false }
    }

    private fun <T> createItem(key: MetadataKey<T>, value: T) {
        val item = Entry(key, value)
        itemsById[key.id] = item
        isEmpty = false
    }

    data class Entry<T>(val key: MetadataKey<T>, var value: T) {

        var isDirty: Boolean = true

        fun copy(): Entry<T> = Entry(key, value)
    }

    companion object {

        private const val MAX_ID_VALUE = 254
    }
}

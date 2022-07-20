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
package org.kryptonmc.krypton.entity.metadata

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.krypton.entity.KryptonEntity
import space.vectrix.flare.fastutil.Int2ObjectSyncMap

// TODO: Possibly change this up to be less vanilla-like
class MetadataHolder(private val entity: KryptonEntity) {

    private val itemsById = Int2ObjectSyncMap.hashmap<Entry<*>>()

    var isDirty: Boolean = false
        private set
    var isEmpty: Boolean = true
        private set

    fun <T> add(key: MetadataKey<T>, value: T) {
        val id = key.id
        require(id <= MAX_ID_VALUE) { "Data value id is too big! Maximum size is $MAX_ID_VALUE, value was $id!" }
        require(!itemsById.containsKey(id)) { "Duplicate id value for $id!" }
        require(MetadataSerializers.idOf(key.serializer) >= 0) { "Unregistered serializer ${key.serializer} for $id!" }
        return createItem(key, value)
    }

    fun <T> get(key: MetadataKey<T>): T = entry(key).value

    fun <T> set(key: MetadataKey<T>, value: T) {
        val existing = entry(key)
        if (value == existing.value) return
        existing.value = value
        entity.onDataUpdate(key)
        existing.isDirty = true
        isDirty = true
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> entry(key: MetadataKey<T>): Entry<T> = checkNotNull(itemsById[key.id] as? Entry<T>) {
        "Could not find key $key for entity of type ${entity.type}!"
    }

    private fun <T> createItem(key: MetadataKey<T>, value: T) {
        val item = Entry(key, value)
        itemsById[key.id] = item
        isEmpty = false
    }

    fun collectAll(): List<Entry<*>>? {
        var entries: PersistentList.Builder<Entry<*>>? = null
        itemsById.values.forEach {
            if (entries == null) entries = persistentListOf<Entry<*>>().builder()
            entries!!.add(it.copy())
        }
        return entries?.build()
    }

    fun collectDirty(): List<Entry<*>>? {
        var entries: PersistentList.Builder<Entry<*>>? = null
        if (isDirty) {
            itemsById.values.forEach {
                if (!it.isDirty) return@forEach
                it.isDirty = false
                if (entries == null) entries = persistentListOf<Entry<*>>().builder()
                entries!!.add(it.copy())
            }
        }
        isDirty = false
        return entries?.build()
    }

    data class Entry<T>(val key: MetadataKey<T>, var value: T) {

        var isDirty: Boolean = true

        fun copy(): Entry<T> = Entry(key, value)
    }

    companion object {

        private const val MAX_ID_VALUE = 254
    }
}

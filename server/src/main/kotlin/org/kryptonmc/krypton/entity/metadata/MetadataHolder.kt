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

import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.util.writeVarInt
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class MetadataHolder(private val entity: KryptonEntity) {

    private val itemsById = Int2ObjectOpenHashMap<Entry<*>>()
    private val lock = ReentrantReadWriteLock()
    private var isDirty = false

    var isEmpty = true
        private set
    val all: List<Entry<*>>
        get() = lock.read { itemsById.values.map { it.copy() }.apply { invalidate() } }
    val dirty: List<Entry<*>>
        get() {
            if (!isDirty) return emptyList()
            return lock.read {
                val entries = itemsById.values.asSequence()
                    .filter { it.isDirty }
                    .map {
                        it.isDirty = false
                        it.copy()
                    }
                    .toList()
                isDirty = false
                entries
            }
        }

    fun <T> add(key: MetadataKey<T>, value: T = key.default) {
        val id = key.id
        require(id <= MAX_ID_VALUE) { "Data value id is too big! Maximum size is $MAX_ID_VALUE, value was $id!" }
        require(!itemsById.containsKey(id)) { "Duplicate id value for $id!" }
        require(key.serializer.id >= 0) { "Unregistered serializer ${key.serializer} for $id!" }
        return createItem(key, value)
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: MetadataKey<T>) = lock.read { itemsById[key.id] as Entry<T> }.value

    @Suppress("UNCHECKED_CAST")
    operator fun <T> set(key: MetadataKey<T>, value: T) {
        val existing = lock.read { itemsById[key.id] as Entry<T> }
        if (value === existing.value) return
        existing.value = value
        entity.onDataUpdate(key)
        existing.isDirty = true
        isDirty = true
    }

    private fun invalidate() {
        isDirty = false
        lock.read { itemsById.values.forEach { it.isDirty = false } }
    }

    private fun <T> createItem(key: MetadataKey<T>, value: T) {
        val item = Entry(key, value)
        lock.write {
            itemsById[key.id] = item
            isEmpty = false
        }
    }

    data class Entry<T>(val key: MetadataKey<T>, var value: T) {

        var isDirty = true

        fun copy() = Entry(key, key.serializer.copy(value))
    }

    companion object {

        private const val MAX_ID_VALUE = 254
        private const val EOF_MARKER = 255

        fun List<Entry<*>>.write(buf: ByteBuf) {
            forEach { buf.writeEntry(it) }
            buf.writeByte(EOF_MARKER)
        }

        private fun <T> ByteBuf.writeEntry(entry: Entry<T>) {
            val key = entry.key
            writeByte(key.id)
            writeVarInt(key.serializer.id)
            key.serializer.write(this, entry.value)
        }
    }
}

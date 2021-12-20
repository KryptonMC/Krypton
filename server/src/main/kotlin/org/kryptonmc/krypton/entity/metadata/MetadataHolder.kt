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

    var isDirty = false
        private set
    var isEmpty = true
        private set
    val all: Sequence<Entry<*>>
        get() = lock.read {
            itemsById.int2ObjectEntrySet()
                .fastIterator()
                .asSequence()
                .map { it.value.copy() }
                .apply { invalidate() }
        }
    val dirty: Sequence<Entry<*>>
        get() {
            if (!isDirty) return emptySequence()
            return lock.read {
                val entries = itemsById.int2ObjectEntrySet()
                    .fastIterator()
                    .asSequence()
                    .filter { it.value.isDirty }
                    .map {
                        it.value.isDirty = false
                        it.value.copy()
                    }
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

    operator fun <T> get(key: MetadataKey<T>): T = checkNotNull(entry<T>(key.id)) {
        "Could not find key $key for entity of type ${entity.type}!"
    }.value

    operator fun <T> set(key: MetadataKey<T>, value: T) {
        val existing = checkNotNull(entry<T>(key.id)) { "Could not find key $key for entity of type ${entity.type}!" }
        if (value === existing.value) return
        existing.value = value
        entity.onDataUpdate(key)
        existing.isDirty = true
        isDirty = true
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> entry(id: Int): Entry<T>? = lock.read { itemsById[id] as? Entry<T> }

    private fun invalidate() {
        isDirty = false
        lock.read { itemsById.int2ObjectEntrySet().fastForEach { it.value.isDirty = false } }
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

        fun copy(): Entry<T> = Entry(key, value)
    }

    companion object {

        private const val MAX_ID_VALUE = 254
        private const val EOF_MARKER = 255

        fun Sequence<Entry<*>>.write(buf: ByteBuf) {
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

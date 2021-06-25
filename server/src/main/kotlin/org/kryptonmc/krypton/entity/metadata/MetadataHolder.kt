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
import io.netty.handler.codec.EncoderException
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.util.reports.CrashReport
import org.kryptonmc.krypton.util.reports.ReportedException
import org.kryptonmc.krypton.util.writeVarInt
import java.util.concurrent.locks.ReentrantReadWriteLock

class MetadataHolder(private val entity: KryptonEntity) {

    private val itemsById = Int2ObjectOpenHashMap<Entry<*>>()
    private val lock = ReentrantReadWriteLock()

    var isEmpty = true
        private set

    var isDirty = false
        private set

    fun <T> add(key: MetadataKey<T>, value: T = key.default) {
        val id = key.id
        require(id <= MAX_ID_VALUE) { "Data value id is too big! Maximum size is $MAX_ID_VALUE, value was $id!" }
        require(!itemsById.containsKey(id)) { "Duplicate id value for $id!" }
        require(MetadataSerializers.idOf(key.serializer) >= 0) { "Unregistered serializer ${key.serializer} for $id!" }
        return createItem(key, value)
    }

    operator fun <T> plusAssign(key: MetadataKey<T>) = add(key)

    operator fun <T> get(key: MetadataKey<T>) = getItem(key).value

    operator fun <T> set(key: MetadataKey<T>, value: T) {
        val item = getItem(key)
        if (value == item.value) return
        item.value = value
        entity.onDataUpdate(key)
        item.isDirty = true
        isDirty = true
    }

    @Suppress("ReplacePutWithAssignment") // Specialised fastutil put
    private fun <T> createItem(key: MetadataKey<T>, value: T) {
        val item = Entry(key, value)
        lock.writeLock().lock()
        itemsById.put(key.id, item)
        isEmpty = false
        lock.writeLock().unlock()
    }

    @Suppress("UNCHECKED_CAST") // This should never fail
    private fun <T> getItem(key: MetadataKey<T>): Entry<T> {
        lock.readLock().lock()
        return try {
            itemsById[key.id] as Entry<T>
        } catch (exception: Throwable) {
            val report = CrashReport.of(exception, "Getting entity metadata")
            report.addCategory("Entity metadata").apply { set("Data key", key) }
            throw ReportedException(report)
        } finally {
            lock.readLock().unlock()
        }
    }

    val all: List<Entry<*>>
        get() {
            lock.readLock().lock()
            val entries = itemsById.values.map { it.copy() }
            lock.readLock().unlock()
            return entries
        }

    val dirty: List<Entry<*>>
        get() {
            if (!isDirty) return emptyList()
            lock.readLock().lock()
            val entries = itemsById.values.asSequence()
                .filter { it.isDirty }
                .map {
                    it.isDirty = false
                    it.copy()
                }
                .toList()
            lock.readLock().unlock()
            isDirty = false
            return entries
        }

    data class Entry<T>(val key: MetadataKey<T>, var value: T) {

        var isDirty = true

        fun copy() = Entry(key, key.serializer.copy(value))
    }

    companion object {

        private const val MAX_ID_VALUE = 254
    }
}

private const val EOF_MARKER = 255

fun List<MetadataHolder.Entry<*>>.write(buf: ByteBuf) {
    forEach { buf.writeEntry(it) }
    if (isNotEmpty()) buf.writeByte(EOF_MARKER)
}

private fun <T> ByteBuf.writeEntry(entry: MetadataHolder.Entry<T>) {
    val key = entry.key
    val id = MetadataSerializers.idOf(key.serializer)
    if (id < 0) throw EncoderException("Unknown serializer type ${key.serializer}!")
    writeByte(key.id)
    writeVarInt(id)
    key.serializer.write(this, entry.value)
}

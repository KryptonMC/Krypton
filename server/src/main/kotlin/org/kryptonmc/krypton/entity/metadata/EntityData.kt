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
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.reports.CrashReport
import org.kryptonmc.krypton.util.reports.ReportedException
import org.kryptonmc.krypton.util.writeVarInt
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.reflect.KClass

class EntityData(private val entity: KryptonEntity) {

    private val itemsById = Int2ObjectOpenHashMap<Item<*>>()
    private val lock = ReentrantReadWriteLock()

    var isEmpty = true
        private set

    var isDirty = false
        private set

    fun <T> define(accessor: EntityDataAccessor<T>, value: T) {
        val id = accessor.id
        require(id <= MAX_ID_VALUE) { "Data value id is too big! Maximum size is $MAX_ID_VALUE, value was $id!" }
        require(!itemsById.containsKey(id)) { "Duplicate id value for $id!" }
        require(EntityDataSerializers.idOf(accessor.serializer) >= 0) { "Unregistered serializer ${accessor.serializer} for $id!" }
        return createItem(accessor, value)
    }

    operator fun <T> get(accessor: EntityDataAccessor<T>) = getItem(accessor).value

    operator fun <T> set(accessor: EntityDataAccessor<T>, value: T) {
        val item = getItem(accessor)
        if (value == item.value) return
        item.value = value
        item.isDirty = true
        isDirty = true
    }

    private fun <T> createItem(accessor: EntityDataAccessor<T>, value: T) {
        val item = Item(accessor, value)
        lock.writeLock().lock()
        itemsById.put(accessor.id, item)
        isEmpty = false
        lock.writeLock().unlock()
    }

    @Suppress("UNCHECKED_CAST") // This should never fail
    private fun <T> getItem(accessor: EntityDataAccessor<T>): Item<T> {
        lock.readLock().lock()
        return try {
            itemsById[accessor.id] as Item<T>
        } catch (exception: Throwable) {
            val report = CrashReport.of(exception, "Getting synched entity data")
            report.addCategory("Synched entity data").apply { set("Data ID", accessor) }
            throw ReportedException(report)
        } finally {
            lock.readLock().unlock()
        }
    }

    val all: List<Item<*>>?
        get() {
            var items: MutableList<Item<*>>? = null
            lock.readLock().lock()
            itemsById.values.forEach {
                if (items == null) items = mutableListOf()
                items!!.add(it.copy())
            }
            lock.readLock().unlock()
            return items
        }

    val dirty: List<Item<*>>?
        get() {
            if (!isDirty) return null
            var items: MutableList<Item<*>>? = null
            lock.readLock().lock()
            itemsById.values.forEach {
                if (!it.isDirty) return@forEach
                it.isDirty = false
                if (items == null) items = mutableListOf()
                items!!.add(it.copy())
            }
            lock.readLock().unlock()
            isDirty = false
            return items
        }

    data class Item<T>(val accessor: EntityDataAccessor<T>, var value: T) {

        var isDirty = true

        fun copy() = Item(accessor, accessor.serializer.copy(value))
    }

    companion object {

        private const val MAX_ID_VALUE = 254

        private val ENTITY_ID_POOL = Object2IntOpenHashMap<Class<out KryptonEntity>>()
        private val LOGGER = logger<EntityData>()

        @Suppress("ReplacePutWithAssignment") // We want to use the specialised put from fastutil
        fun <T> define(entityClass: Class<out KryptonEntity>, serializer: EntityDataSerializer<T>): EntityDataAccessor<T> {
            if (LOGGER.isDebugEnabled) {
                try {
                    val currentClass = Class.forName(Thread.currentThread().stackTrace[2].className)
                    if (currentClass != entityClass) LOGGER.debug("Attempted to define entity data for $entityClass from $currentClass")
                } catch (ignored: ClassNotFoundException) {}
            }

            val id = if (ENTITY_ID_POOL.containsKey(entityClass)) {
                ENTITY_ID_POOL.getInt(entityClass) + 1
            } else {
                var id = 0
                var absoluteClass: Class<*> = entityClass
                while (absoluteClass != KryptonEntity::class.java) {
                    absoluteClass = absoluteClass.superclass
                    if (ENTITY_ID_POOL.containsKey(absoluteClass)) {
                        id = ENTITY_ID_POOL.getInt(absoluteClass) + 1
                        break
                    }
                }
                id
            }

            require(id <= MAX_ID_VALUE) { "Data value id is too big! Maximum size is $MAX_ID_VALUE, value was $id!" }
            ENTITY_ID_POOL.put(entityClass, id)
            return serializer.createAccessor(id)
        }

        fun <T> define(entityClass: KClass<out KryptonEntity>, serializer: EntityDataSerializer<T>) = define(entityClass.java, serializer)
    }
}

private const val EOF_MARKER = 255

fun List<EntityData.Item<*>>?.pack(buf: ByteBuf) {
    this?.forEach { buf.writeDataItem(it) }
    buf.writeByte(EOF_MARKER)
}

private fun <T> ByteBuf.writeDataItem(item: EntityData.Item<T>) {
    val accessor = item.accessor
    val id = EntityDataSerializers.idOf(accessor.serializer)
    if (id < 0) throw EncoderException("Unknown serializer type ${accessor.serializer}!")
    writeByte(accessor.id)
    writeVarInt(id)
    accessor.serializer.write(this, item.value)
}

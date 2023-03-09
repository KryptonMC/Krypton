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
package org.kryptonmc.krypton.entity.ai.memory

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound
import org.kryptonmc.serialization.nbt.NbtOps
import java.util.concurrent.ConcurrentHashMap

/**
 * Holds memories for living entities.
 *
 * Memories can be any arbitrary data that is used for the entity's AI, and helps to decouple that
 * data from the entity itself.
 *
 * It also provides auto-expiry for memories, so they get automatically removed after a certain
 * amount of time.
 */
class Brain {

    private val memories = ConcurrentHashMap<MemoryKey<*>, Memory<*>>()

    fun isMemoryRegistered(key: MemoryKey<*>): Boolean = memories.containsKey(key)

    fun hasMemory(key: MemoryKey<*>): Boolean {
        if (!isMemoryRegistered(key)) return false
        val memory = memories.get(key)!!
        return memory !is EmptyMemory
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getMemory(key: MemoryKey<T>): T? {
        if (!hasMemory(key)) return null
        return memories.get(key)!!.value as? T
    }

    fun <T : Any> setStaticMemory(key: MemoryKey<T>, value: T?) {
        if (!memories.containsKey(key)) return
        if (value == null || (value is Collection<*> && value.isEmpty())) {
            memories.put(key, EmptyMemory)
            return
        }
        memories.put(key, StaticMemory(value))
    }

    fun <T : Any> setExpirableMemory(key: MemoryKey<T>, value: T?, ttl: Long) {
        if (!memories.containsKey(key)) return
        if (value == null || (value is Collection<*> && value.isEmpty())) {
            memories.put(key, EmptyMemory)
            return
        }
        memories.put(key, ExpirableMemory(value, ttl))
    }

    fun load(data: CompoundTag) {
        data.getCompound("Memories").forEachCompound { memoryKey, memoryData ->
            val key = KryptonRegistries.MEMORY_KEY.get(Key.key(memoryKey)) ?: return@forEachCompound
            val value = memoryData.get("value") ?: return@forEachCompound
            val decoded = try {
                key.codec.decode(value, NbtOps.INSTANCE)
            } catch (_: Exception) {
                return@forEachCompound
            }

            val ttl = memoryData.getLong("ttl")
            val memory = if (ttl == Long.MAX_VALUE) StaticMemory(decoded) else ExpirableMemory(decoded, ttl)
            memories.put(key, memory)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun save(): CompoundTag = compound {
        compound("memories") {
            memories.forEach { (key, memory) ->
                if (memory is EmptyMemory) return@forEach
                memory.save(key as MemoryKey<in Any>, this)
            }
        }
    }
}

/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

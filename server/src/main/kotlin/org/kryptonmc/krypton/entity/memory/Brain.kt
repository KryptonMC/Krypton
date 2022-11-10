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
package org.kryptonmc.krypton.entity.memory

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound
import org.kryptonmc.serialization.nbt.NbtOps
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

class Brain<E : KryptonLivingEntity> {

    private val memories = ConcurrentHashMap<MemoryKey<*>, Optional<Memory<*>>>()

    operator fun contains(key: MemoryKey<*>): Boolean = memories.containsKey(key)

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: MemoryKey<T>): Optional<T> {
        val memory = requireNotNull(memories.get(key)) { "Cannot get unregistered memory for key $key!" }
        return memory.map { it.value as? T }
    }

    operator fun <T : Any> set(key: MemoryKey<T>, value: T?) {
        set(key, value, Long.MAX_VALUE)
    }

    fun <T : Any> set(key: MemoryKey<T>, value: T?, ttl: Long) {
        if (!memories.containsKey(key)) return
        if (value == null) {
            memories.put(key, Optional.empty())
            return
        }
        if (value is Collection<*> && value.isEmpty()) {
            memories.put(key, Optional.empty())
            return
        }
        memories.put(key, Optional.of(Memory(value, ttl)))
    }

    fun load(data: CompoundTag) {
        data.getCompound("Memories").forEachCompound { memoryKey, memory ->
            val key = KryptonRegistries.MEMORIES.get(Key.key(memoryKey)) ?: return@forEachCompound
            val value = memory.get("value") ?: return@forEachCompound
            val decoded = try {
                key.codec.decode(value, NbtOps.INSTANCE)
            } catch (_: Exception) {
                return@forEachCompound
            }
            memories.put(key, Optional.of(Memory(decoded, memory.getLong("ttl"))))
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun save(): CompoundTag = compound {
        compound("memories") { memories.forEach { (key, memory) -> memory.ifPresent { it.save(key as MemoryKey<in Any>, this) } } }
    }
}

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
package org.kryptonmc.krypton.entity.memory

import org.kryptonmc.nbt.CompoundTag

class Memory<T : Any>(val value: T?, timeToLive: Long = Long.MAX_VALUE) {

    var timeToLive = timeToLive
        private set
    private val canExpire: Boolean
        get() = timeToLive != Long.MAX_VALUE
    val hasExpired: Boolean
        get() = timeToLive <= 0L

    fun tick() {
        if (canExpire) --timeToLive
    }

    fun save(key: MemoryKey<in T>, tag: CompoundTag.Builder): CompoundTag.Builder = tag.apply {
        compound(key.key.asString()) {
            if (value != null) put("value", key.codec.encode(value))
            if (canExpire) long("ttl", timeToLive)
        }
    }

    override fun toString(): String = "Memory(value=$value, ttl=$timeToLive)"
}

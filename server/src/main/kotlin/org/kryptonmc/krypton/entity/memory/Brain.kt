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

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

class Brain(private val memories: MutableList<Memory<out Any>>) {

    fun load(tag: CompoundTag) {
        tag.getCompound("Memories").forEach {
            val key = InternalRegistries.MEMORIES[Key.key(it.key)] ?: return@forEach
            val value = it.value as? CompoundTag ?: return@forEach
            val decodedResult = key.codec.parse(NBTOps, value["value"] ?: return@forEach)
            if (decodedResult.result().isEmpty) return@forEach
            val decoded = decodedResult.result().get()
            memories += Memory(key, decoded, value.getLong("ttl"))
        }
    }

    fun save(): CompoundTag = compound {
        compound("memories") memories@{ memories.forEach { it.save(this@memories) } }
    }
}

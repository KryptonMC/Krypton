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
package org.kryptonmc.krypton.map

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import org.kryptonmc.krypton.world.data.PersistentData
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

class MapIndex : PersistentData() {

    private val usedIds = Object2IntOpenHashMap<String>().apply { defaultReturnValue(-1) }

    fun nextId(): Int {
        val next = usedIds.getInt("map") + 1
        usedIds["map"] = next
        markDirty()
        return next
    }

    override fun save(): CompoundTag = compound { usedIds.object2IntEntrySet().forEach { int(it.key, it.intValue) } }

    companion object {

        @JvmStatic
        fun from(data: CompoundTag): MapIndex {
            val index = MapIndex()
            data.keys.forEach { if (data.contains(it, 99)) index.usedIds[it] = data.getInt(it) }
            return index
        }
    }
}

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
package org.kryptonmc.krypton.entity.components

import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.MutableListTag

interface SerializableEntity : BaseEntity {

    val serializer: EntitySerializer<out KryptonEntity>

    fun load(data: CompoundTag) {
        serializer().load(this as KryptonEntity, data)
    }

    fun save(): CompoundTag.Builder = serializer().save(this as KryptonEntity)

    fun saveWithPassengers(): CompoundTag.Builder = save().apply {
        if (isVehicle()) {
            val passengerList = MutableListTag.of(ArrayList(), EndTag.ID)
            passengers.forEach {
                if (it !is KryptonEntity) return@forEach
                passengerList.add(it.saveWithPassengers().build())
            }
            if (!passengerList.isEmpty) put("Passengers", passengerList)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun serializer(): EntitySerializer<KryptonEntity> = serializer as EntitySerializer<KryptonEntity>
}

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

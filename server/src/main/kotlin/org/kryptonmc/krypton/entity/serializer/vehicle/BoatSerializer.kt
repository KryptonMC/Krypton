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
package org.kryptonmc.krypton.entity.serializer.vehicle

import org.kryptonmc.api.entity.vehicle.BoatVariant
import org.kryptonmc.krypton.entity.serializer.BaseEntitySerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.vehicle.KryptonBoat
import org.kryptonmc.krypton.util.nbt.putStringEnum
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag

object BoatSerializer : EntitySerializer<KryptonBoat> {

    private const val TYPE_TAG = "Type"
    private val TYPE_NAMES = BoatVariant.values().associateBy { it.name.lowercase() }

    override fun load(entity: KryptonBoat, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        if (data.contains(TYPE_TAG, StringTag.ID)) entity.variant = TYPE_NAMES.getOrDefault(data.getString(TYPE_TAG), BoatVariant.OAK)
    }

    override fun save(entity: KryptonBoat): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        putStringEnum(TYPE_TAG, entity.variant)
    }
}

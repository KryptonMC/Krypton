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
package org.kryptonmc.krypton.entity.serializer.animal

import org.kryptonmc.krypton.entity.animal.KryptonRabbit
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.nbt.CompoundTag

object RabbitSerializer : EntitySerializer<KryptonRabbit> {

    private const val TYPE_TAG = "RabbitType"
    private const val MORE_CARROT_TICKS_TAG = "MoreCarrotTicks"

    override fun load(entity: KryptonRabbit, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        entity.data.set(MetadataKeys.Rabbit.TYPE, data.getInt(TYPE_TAG))
        entity.moreCarrotTicks = data.getInt(MORE_CARROT_TICKS_TAG)
    }

    override fun save(entity: KryptonRabbit): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        putInt(TYPE_TAG, entity.data.get(MetadataKeys.Rabbit.TYPE))
        putInt(MORE_CARROT_TICKS_TAG, entity.moreCarrotTicks)
    }
}
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
package org.kryptonmc.krypton.entity.serializer.aquatic

import org.kryptonmc.krypton.entity.aquatic.KryptonFish
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.MobSerializer
import org.kryptonmc.nbt.CompoundTag

object FishSerializer : EntitySerializer<KryptonFish> {

    private const val FROM_BUCKET_TAG = "FromBucket"

    override fun load(entity: KryptonFish, data: CompoundTag) {
        MobSerializer.load(entity, data)
        entity.setSpawnedFromBucket(data.getBoolean(FROM_BUCKET_TAG))
    }

    override fun save(entity: KryptonFish): CompoundTag.Builder = MobSerializer.save(entity).apply {
        putBoolean(FROM_BUCKET_TAG, entity.wasSpawnedFromBucket())
    }
}

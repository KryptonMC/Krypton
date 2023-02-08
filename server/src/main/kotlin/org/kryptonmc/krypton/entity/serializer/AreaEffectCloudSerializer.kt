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
package org.kryptonmc.krypton.entity.serializer

import org.kryptonmc.api.util.Color
import org.kryptonmc.krypton.entity.KryptonAreaEffectCloud
import org.kryptonmc.nbt.CompoundTag

object AreaEffectCloudSerializer : EntitySerializer<KryptonAreaEffectCloud> {

    private const val AGE_TAG = "Age"
    private const val DURATION_TAG = "Duration"
    private const val RADIUS_TAG = "Radius"
    private const val COLOR_TAG = "Color"

    override fun load(entity: KryptonAreaEffectCloud, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        entity.age = data.getInt(AGE_TAG)
        entity.duration = data.getInt(DURATION_TAG)
        entity.radius = data.getFloat(RADIUS_TAG)
        entity.color = Color(data.getInt(COLOR_TAG))
    }

    override fun save(entity: KryptonAreaEffectCloud): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        putInt(AGE_TAG, entity.age)
        putInt(DURATION_TAG, entity.duration)
        putFloat(RADIUS_TAG, entity.radius)
        putInt(COLOR_TAG, entity.color.encode())
    }
}

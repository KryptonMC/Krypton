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

    override fun load(entity: KryptonAreaEffectCloud, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        entity.age = data.getInt("Age")
        entity.duration = data.getInt("Duration")
        entity.radius = data.getFloat("Radius")
        entity.color = Color.of(data.getInt("Color"))
    }

    override fun save(entity: KryptonAreaEffectCloud): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        putInt("Age", entity.age)
        putInt("Duration", entity.duration)
        putFloat("Radius", entity.radius)
        putInt("Color", entity.color.value)
    }
}

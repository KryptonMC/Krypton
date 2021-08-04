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
package org.kryptonmc.krypton.entity

import org.kryptonmc.api.entity.AreaEffectCloud
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import java.awt.Color

class KryptonAreaEffectCloud(world: KryptonWorld) : KryptonEntity(world, EntityTypes.AREA_EFFECT_CLOUD), AreaEffectCloud {

    init {
        data += MetadataKeys.AREA_EFFECT_CLOUD.RADIUS
        data += MetadataKeys.AREA_EFFECT_CLOUD.COLOR
        data += MetadataKeys.AREA_EFFECT_CLOUD.IGNORE_RADIUS
        data += MetadataKeys.AREA_EFFECT_CLOUD.PARTICLE
    }

    override var age = 0
    override var duration = 0

    override fun load(tag: CompoundTag) {
        super.load(tag)
        age = tag.getInt("Age")
        duration = tag.getInt("Duration")
        radius = tag.getFloat("Radius")
        color = tag.getColor("Color")
    }

    override fun save() = super.save().apply {
        int("Age", age)
        int("Duration", duration)
        float("Radius", radius)
        int("Color", color.rgb)
    }

    override var radius: Float
        get() = data[MetadataKeys.AREA_EFFECT_CLOUD.RADIUS]
        set(value) = data.set(MetadataKeys.AREA_EFFECT_CLOUD.RADIUS, value)

    override var color: Color
        get() = Color(data[MetadataKeys.AREA_EFFECT_CLOUD.COLOR])
        set(value) = data.set(MetadataKeys.AREA_EFFECT_CLOUD.COLOR, value.rgb)
}

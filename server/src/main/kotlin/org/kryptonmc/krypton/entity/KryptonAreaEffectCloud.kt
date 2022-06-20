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
package org.kryptonmc.krypton.entity

import org.kryptonmc.api.entity.AreaEffectCloud
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonAreaEffectCloud(world: KryptonWorld) : KryptonEntity(world, EntityTypes.AREA_EFFECT_CLOUD), AreaEffectCloud {

    override var age: Int = 0
    override var duration: Int = 0

    override var radius: Float
        get() = data[MetadataKeys.AREA_EFFECT_CLOUD.RADIUS]
        set(value) = data.set(MetadataKeys.AREA_EFFECT_CLOUD.RADIUS, value)
    override var color: Int
        get() = data[MetadataKeys.AREA_EFFECT_CLOUD.COLOR]
        set(value) = data.set(MetadataKeys.AREA_EFFECT_CLOUD.COLOR, value)

    init {
        data.add(MetadataKeys.AREA_EFFECT_CLOUD.RADIUS)
        data.add(MetadataKeys.AREA_EFFECT_CLOUD.COLOR)
        data.add(MetadataKeys.AREA_EFFECT_CLOUD.IGNORE_RADIUS)
        data.add(MetadataKeys.AREA_EFFECT_CLOUD.PARTICLE)
    }
}

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

import org.kryptonmc.api.effect.particle.ParticleTypes
import org.kryptonmc.api.entity.AreaEffectCloud
import org.kryptonmc.api.util.Color
import org.kryptonmc.krypton.effect.particle.ParticleOptions
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.AreaEffectCloudSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonAreaEffectCloud(world: KryptonWorld) : KryptonEntity(world), AreaEffectCloud {

    override val type: KryptonEntityType<KryptonAreaEffectCloud>
        get() = KryptonEntityTypes.AREA_EFFECT_CLOUD
    override val serializer: EntitySerializer<KryptonAreaEffectCloud>
        get() = AreaEffectCloudSerializer

    var age: Int = 0
    override var duration: Int = 0

    override var radius: Float
        get() = data.get(MetadataKeys.AreaEffectCloud.RADIUS)
        set(value) = data.set(MetadataKeys.AreaEffectCloud.RADIUS, value)
    override var color: Color
        get() = Color.of(data.get(MetadataKeys.AreaEffectCloud.COLOR))
        set(value) = data.set(MetadataKeys.AreaEffectCloud.COLOR, value.value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.AreaEffectCloud.RADIUS, DEFAULT_RADIUS)
        data.define(MetadataKeys.AreaEffectCloud.COLOR, 0)
        data.define(MetadataKeys.AreaEffectCloud.IGNORE_RADIUS, false)
        data.define(MetadataKeys.AreaEffectCloud.PARTICLE, ParticleOptions(ParticleTypes.EFFECT.get(), null))
    }

    companion object {

        private const val DEFAULT_RADIUS = 0.5F
    }
}

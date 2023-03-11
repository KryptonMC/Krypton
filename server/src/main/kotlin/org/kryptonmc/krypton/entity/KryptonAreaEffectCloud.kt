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
        get() = Color(data.get(MetadataKeys.AreaEffectCloud.COLOR))
        set(value) = data.set(MetadataKeys.AreaEffectCloud.COLOR, value.encode())

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

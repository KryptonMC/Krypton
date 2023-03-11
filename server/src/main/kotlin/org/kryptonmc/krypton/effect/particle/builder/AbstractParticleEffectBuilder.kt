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
package org.kryptonmc.krypton.effect.particle.builder

import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.builder.BaseParticleEffectBuilder
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.effect.particle.KryptonParticleEffect

@Suppress("UNCHECKED_CAST")
abstract class AbstractParticleEffectBuilder<B : BaseParticleEffectBuilder<B>>(protected val type: ParticleType) : BaseParticleEffectBuilder<B> {

    private var quantity = 1
    private var offset: Vec3d = Vec3d.ZERO
    private var longDistance = false

    abstract fun buildData(): ParticleData?

    override fun quantity(quantity: Int): B = apply {
        require(quantity >= 1) { "Quantity must be >= 1!" }
        this.quantity = quantity
    } as B

    override fun offset(offset: Vec3d): B = apply { this.offset = offset } as B

    override fun longDistance(longDistance: Boolean): B = apply { this.longDistance = longDistance } as B

    override fun build(): ParticleEffect = KryptonParticleEffect(type, quantity, offset, longDistance, buildData())
}

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
package org.kryptonmc.krypton.effect.particle.builder

import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.builder.BaseParticleEffectBuilder
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.krypton.effect.particle.KryptonParticleEffect
import org.spongepowered.math.vector.Vector3d

@Suppress("UNCHECKED_CAST")
abstract class AbstractParticleEffectBuilder<B : BaseParticleEffectBuilder<B>>(
    protected val type: ParticleType,
    protected var quantity: Int,
    protected var offset: Vector3d,
    protected var longDistance: Boolean
) : BaseParticleEffectBuilder<B> {

    abstract fun buildData(): ParticleData?

    override fun quantity(quantity: Int): B = apply { this.quantity = quantity } as B

    override fun offset(offset: Vector3d): B = apply { this.offset = offset } as B

    override fun longDistance(longDistance: Boolean): B = apply { this.longDistance = longDistance } as B

    override fun build(): ParticleEffect = KryptonParticleEffect(type, quantity, offset, longDistance, buildData())
}

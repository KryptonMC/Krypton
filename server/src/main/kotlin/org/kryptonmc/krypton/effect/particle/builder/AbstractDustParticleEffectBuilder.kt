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
package org.kryptonmc.krypton.effect.particle.builder

import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.builder.BaseDustParticleEffectBuilder
import org.spongepowered.math.vector.Vector3d

@Suppress("UNCHECKED_CAST")
abstract class AbstractDustParticleEffectBuilder<B : BaseDustParticleEffectBuilder<B>>(
    type: ParticleType,
    quantity: Int,
    offset: Vector3d,
    longDistance: Boolean,
    red: Short,
    green: Short,
    blue: Short,
    protected var scale: Float = 0F
) : AbstractColorParticleEffectBuilder<B>(type, quantity, offset, longDistance, red, green, blue), BaseDustParticleEffectBuilder<B> {

    override fun scale(scale: Float): B = apply { this.scale = scale } as B
}

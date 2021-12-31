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
import org.kryptonmc.api.effect.particle.builder.BaseColorParticleEffectBuilder
import org.spongepowered.math.vector.Vector3d

@Suppress("UNCHECKED_CAST")
abstract class AbstractColorParticleEffectBuilder<B : BaseColorParticleEffectBuilder<B>>(
    type: ParticleType,
    quantity: Int,
    offset: Vector3d,
    longDistance: Boolean,
    protected var red: Short = 0,
    protected var green: Short = 0,
    protected var blue: Short = 0
) : AbstractParticleEffectBuilder<B>(type, quantity, offset, longDistance), BaseColorParticleEffectBuilder<B> {

    override fun rgb(red: Int, green: Int, blue: Int): B = apply {
        this.red = (red and 0xFF).toShort()
        this.green = (green and 0xFF).toShort()
        this.blue = (blue and 0xFF).toShort()
    } as B
}

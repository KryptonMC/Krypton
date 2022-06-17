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

import org.kryptonmc.api.effect.particle.DustTransitionParticleType
import org.kryptonmc.api.effect.particle.builder.DustTransitionParticleEffectBuilder
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.krypton.effect.particle.data.KryptonDustTransitionParticleData
import org.spongepowered.math.vector.Vector3d

class KryptonDustTransitionParticleEffectBuilder(type: DustTransitionParticleType) :
    AbstractDustParticleEffectBuilder<DustTransitionParticleEffectBuilder>(type),
    DustTransitionParticleEffectBuilder {

    private var toRed: Short = 0
    private var toGreen: Short = 0
    private var toBlue: Short = 0

    override fun toRGB(red: Int, green: Int, blue: Int): DustTransitionParticleEffectBuilder = apply {
        toRed = (red and 0xFF).toShort()
        toGreen = (green and 0xFF).toShort()
        toBlue = (blue and 0xFF).toShort()
    }

    override fun buildData(): ParticleData = KryptonDustTransitionParticleData(red, green, blue, scale, toRed, toGreen, toBlue)
}

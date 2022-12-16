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
import org.kryptonmc.api.util.Color
import org.kryptonmc.krypton.effect.particle.data.KryptonDustTransitionParticleData

class KryptonDustTransitionParticleEffectBuilder(type: DustTransitionParticleType) : AbstractDustParticleEffectBuilder<ApiDT>(type), ApiDT {

    private var to: Color = Color.BLACK

    override fun toColor(color: Color): ApiDT = apply { to = color }

    override fun buildData(): ParticleData = KryptonDustTransitionParticleData(color, scale, to)
}

private typealias ApiDT = DustTransitionParticleEffectBuilder

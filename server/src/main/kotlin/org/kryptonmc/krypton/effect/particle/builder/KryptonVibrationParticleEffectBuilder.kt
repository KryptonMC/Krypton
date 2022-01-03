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

import org.kryptonmc.api.effect.particle.VibrationParticleType
import org.kryptonmc.api.effect.particle.builder.VibrationParticleEffectBuilder
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.krypton.effect.particle.data.KryptonVibrationParticleData
import org.spongepowered.math.vector.Vector3d

class KryptonVibrationParticleEffectBuilder(
    type: VibrationParticleType,
    quantity: Int = 1,
    offset: Vector3d = Vector3d.ZERO,
    longDistance: Boolean = false,
    private var origin: Vector3d = Vector3d.ZERO,
    private var destination: Vector3d = Vector3d.ZERO,
    private var ticks: Int = 0
) : AbstractParticleEffectBuilder<VibrationParticleEffectBuilder>(type, quantity, offset, longDistance), VibrationParticleEffectBuilder {

    override fun origin(position: Vector3d): VibrationParticleEffectBuilder = apply { origin = position }

    override fun destination(position: Vector3d): VibrationParticleEffectBuilder = apply { destination = position }

    override fun ticks(ticks: Int): VibrationParticleEffectBuilder = apply { this.ticks = ticks }

    override fun buildData(): ParticleData = KryptonVibrationParticleData(origin, destination, ticks)
}

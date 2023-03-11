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

import org.kryptonmc.api.effect.particle.VibrationParticleType
import org.kryptonmc.api.effect.particle.builder.VibrationParticleEffectBuilder
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.effect.particle.data.KryptonVibrationParticleData

class KryptonVibrationParticleEffectBuilder(type: VibrationParticleType) : AbstractParticleEffectBuilder<ApiVibration>(type), ApiVibration {

    private var destination: Vec3d = Vec3d.ZERO
    private var ticks = 0

    override fun destination(position: Vec3d): ApiVibration = apply { destination = position }

    override fun ticks(ticks: Int): ApiVibration = apply { this.ticks = ticks }

    override fun buildData(): ParticleData = KryptonVibrationParticleData(destination, ticks)
}

private typealias ApiVibration = VibrationParticleEffectBuilder

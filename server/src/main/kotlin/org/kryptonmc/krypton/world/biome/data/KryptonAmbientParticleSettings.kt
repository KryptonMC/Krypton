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
package org.kryptonmc.krypton.world.biome.data

import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.world.biome.AmbientParticleSettings
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

@JvmRecord
data class KryptonAmbientParticleSettings(
    override val type: ParticleType,
    override val data: ParticleData?,
    override val probability: Float
) : AmbientParticleSettings {

    constructor(type: ParticleType, probability: Float) : this(type, null, probability)

    class Builder(private var type: ParticleType) : AmbientParticleSettings.Builder {

        private var data: ParticleData? = null
        private var probability = 0F

        override fun type(type: ParticleType): AmbientParticleSettings.Builder = apply { this.type = type }

        override fun data(data: ParticleData?): AmbientParticleSettings.Builder = apply { this.data = data }

        override fun probability(probability: Float): AmbientParticleSettings.Builder = apply { this.probability = probability }

        override fun build(): AmbientParticleSettings = KryptonAmbientParticleSettings(type, data, probability)
    }

    object Factory : AmbientParticleSettings.Factory {

        override fun of(type: ParticleType, data: ParticleData?, probability: Float): AmbientParticleSettings =
            KryptonAmbientParticleSettings(type, data, probability)

        override fun builder(type: ParticleType): AmbientParticleSettings.Builder = Builder(type)
    }

    companion object {

        @JvmField
        val CODEC: Codec<AmbientParticleSettings> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codecs.PARTICLE.fieldOf("particle").getting { it.type },
                Codec.FLOAT.fieldOf("probability").getting { it.probability }
            ).apply(instance, ::KryptonAmbientParticleSettings)
        }
    }
}

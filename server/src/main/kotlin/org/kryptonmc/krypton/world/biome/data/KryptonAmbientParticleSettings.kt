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

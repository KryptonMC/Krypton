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

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.world.biome.AmbientAdditionsSettings
import org.kryptonmc.krypton.effect.sound.KryptonSoundEvent
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

@JvmRecord
data class KryptonAmbientAdditionsSettings(override val sound: SoundEvent, override val probability: Double) : AmbientAdditionsSettings {

    class Builder(private var sound: SoundEvent) : AmbientAdditionsSettings.Builder {

        private var probability = 0.0

        override fun sound(sound: SoundEvent): AmbientAdditionsSettings.Builder = apply { this.sound = sound }

        override fun probability(probability: Double): AmbientAdditionsSettings.Builder = apply { this.probability = probability }

        override fun build(): AmbientAdditionsSettings = KryptonAmbientAdditionsSettings(sound, probability)
    }

    object Factory : AmbientAdditionsSettings.Factory {

        override fun of(sound: SoundEvent, probability: Double): AmbientAdditionsSettings = KryptonAmbientAdditionsSettings(sound, probability)

        override fun builder(sound: SoundEvent): AmbientAdditionsSettings.Builder = Builder(sound)
    }

    companion object {

        @JvmField
        val CODEC: Codec<AmbientAdditionsSettings> = RecordCodecBuilder.create { instance ->
            instance.group(
                KryptonSoundEvent.DIRECT_CODEC.fieldOf("sound").getting { it.sound },
                Codec.DOUBLE.fieldOf("probability").getting { it.probability }
            ).apply(instance, ::KryptonAmbientAdditionsSettings)
        }
    }
}

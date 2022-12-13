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
package org.kryptonmc.krypton.world.biome

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.world.biome.AmbientAdditionsSettings
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

@JvmRecord
data class KryptonAmbientAdditionsSettings(override val sound: SoundEvent, override val probability: Double) : AmbientAdditionsSettings {

    override fun toBuilder(): AmbientAdditionsSettings.Builder = Builder(this)

    class Builder(private var sound: SoundEvent) : AmbientAdditionsSettings.Builder {

        private var probability = 0.0

        constructor(settings: AmbientAdditionsSettings) : this(settings.sound) {
            probability = settings.probability
        }

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
                Codecs.SOUND_EVENT.fieldOf("sound").getting { it.sound },
                Codec.DOUBLE.fieldOf("probability").getting { it.probability }
            ).apply(instance) { sound, probability -> KryptonAmbientAdditionsSettings(sound, probability) }
        }
    }
}

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

import org.kryptonmc.api.effect.Music
import org.kryptonmc.krypton.effect.KryptonMusic
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.Color
import org.kryptonmc.api.world.biome.AmbientAdditionsSettings
import org.kryptonmc.api.world.biome.AmbientMoodSettings
import org.kryptonmc.api.world.biome.AmbientParticleSettings
import org.kryptonmc.api.world.biome.BiomeEffects
import org.kryptonmc.api.world.biome.GrassColorModifier
import org.kryptonmc.krypton.effect.sound.KryptonSoundEvent
import org.kryptonmc.krypton.util.ColorUtil
import org.kryptonmc.krypton.util.serialization.EnumCodecs
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder
import java.util.Optional

@JvmRecord
data class KryptonBiomeEffects(
    override val fogColor: Color,
    override val waterColor: Color,
    override val waterFogColor: Color,
    override val skyColor: Color,
    override val grassColorModifier: GrassColorModifier = GrassColorModifier.NONE,
    override val foliageColor: Color? = null,
    override val grassColor: Color? = null,
    override val ambientParticleSettings: AmbientParticleSettings? = null,
    override val ambientLoopSound: SoundEvent? = null,
    override val ambientMoodSettings: AmbientMoodSettings? = null,
    override val ambientAdditionsSettings: AmbientAdditionsSettings? = null,
    override val backgroundMusic: Music? = null
) : BiomeEffects {

    class Builder : BiomeEffects.Builder {

        private var fog = Color.BLACK
        private var water = Color.BLACK
        private var waterFog = Color.BLACK
        private var sky = Color.BLACK
        private var grassModifier = GrassColorModifier.NONE
        private var foliage: Color? = null
        private var grass: Color? = null
        private var particles: AmbientParticleSettings? = null
        private var loopSound: SoundEvent? = null
        private var mood: AmbientMoodSettings? = null
        private var additions: AmbientAdditionsSettings? = null
        private var backgroundMusic: Music? = null

        override fun fogColor(color: Color): BiomeEffects.Builder = apply { fog = color }

        override fun waterColor(color: Color): BiomeEffects.Builder = apply { water = color }

        override fun waterFogColor(color: Color): BiomeEffects.Builder = apply { waterFog = color }

        override fun skyColor(color: Color): BiomeEffects.Builder = apply { sky = color }

        override fun grassColorModifier(modifier: GrassColorModifier): BiomeEffects.Builder = apply { grassModifier = modifier }

        override fun foliageColor(color: Color?): BiomeEffects.Builder = apply { foliage = color }

        override fun grassColor(color: Color?): BiomeEffects.Builder = apply { grass = color }

        override fun particles(settings: AmbientParticleSettings?): BiomeEffects.Builder = apply { particles = settings }

        override fun loopSound(sound: SoundEvent?): BiomeEffects.Builder = apply { loopSound = sound }

        override fun mood(settings: AmbientMoodSettings?): BiomeEffects.Builder = apply { mood = settings }

        override fun additions(settings: AmbientAdditionsSettings?): BiomeEffects.Builder = apply { additions = settings }

        override fun backgroundMusic(music: Music?): BiomeEffects.Builder = apply { backgroundMusic = music }

        override fun build(): BiomeEffects =
            KryptonBiomeEffects(fog, water, waterFog, sky, grassModifier, foliage, grass, particles, loopSound, mood, additions, backgroundMusic)
    }

    object Factory : BiomeEffects.Factory {

        override fun builder(): BiomeEffects.Builder = Builder()
    }

    companion object {

        @JvmField
        val DEFAULT: BiomeEffects = Builder().build()
        @JvmField
        val CODEC: Codec<BiomeEffects> = RecordCodecBuilder.create { instance ->
            instance.group(
                ColorUtil.CODEC.fieldOf("fog_color").getting { it.fogColor },
                ColorUtil.CODEC.fieldOf("water_color").getting { it.waterColor },
                ColorUtil.CODEC.fieldOf("water_fog_color").getting { it.waterFogColor },
                ColorUtil.CODEC.fieldOf("sky_color").getting { it.skyColor },
                EnumCodecs.GRASS_COLOR_MODIFIER.fieldOf("grass_color_modifier").getting { it.grassColorModifier },
                ColorUtil.CODEC.optionalFieldOf("foliage_color").getting { Optional.ofNullable(it.foliageColor) },
                ColorUtil.CODEC.optionalFieldOf("grass_color").getting { Optional.ofNullable(it.grassColor) },
                KryptonAmbientParticleSettings.CODEC.optionalFieldOf("particle").getting { Optional.ofNullable(it.ambientParticleSettings) },
                KryptonSoundEvent.DIRECT_CODEC.optionalFieldOf("ambient_sound").getting { Optional.ofNullable(it.ambientLoopSound) },
                KryptonAmbientMoodSettings.CODEC.optionalFieldOf("mood_sound").getting { Optional.ofNullable(it.ambientMoodSettings) },
                KryptonAmbientAdditionsSettings.CODEC.optionalFieldOf("additions_sound").getting { Optional.ofNullable(it.ambientAdditionsSettings) },
                KryptonMusic.CODEC.optionalFieldOf("music").getting { Optional.ofNullable(it.backgroundMusic) }
            ).apply(instance) { fog, water, waterFog, sky, modifier, foliage, grass, particles, loopSound, mood, additions, music ->
                KryptonBiomeEffects(fog, water, waterFog, sky, modifier, foliage.orElse(null), grass.orElse(null), particles.orElse(null),
                    loopSound.orElse(null), mood.orElse(null), additions.orElse(null), music.orElse(null))
            }
        }
    }
}

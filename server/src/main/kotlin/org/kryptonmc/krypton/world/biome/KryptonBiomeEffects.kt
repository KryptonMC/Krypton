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

import org.kryptonmc.api.effect.Music
import org.kryptonmc.krypton.effect.KryptonMusic
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.Color
import org.kryptonmc.api.world.biome.AmbientAdditionsSettings
import org.kryptonmc.api.world.biome.AmbientMoodSettings
import org.kryptonmc.api.world.biome.AmbientParticleSettings
import org.kryptonmc.api.world.biome.BiomeEffects
import org.kryptonmc.api.world.biome.GrassColorModifier
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.serialization.EnumCodecs
import org.kryptonmc.krypton.util.serialization.nullableFieldOf
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

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

    override fun toBuilder(): BiomeEffects.Builder = Builder(this)

    class Builder() : BiomeEffects.Builder {

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

        constructor(effects: BiomeEffects) : this() {
            fog = effects.fogColor
            water = effects.waterColor
            waterFog = effects.waterFogColor
            sky = effects.skyColor
            grassModifier = effects.grassColorModifier
            foliage = effects.foliageColor
            grass = effects.grassColor
            particles = effects.ambientParticleSettings
            loopSound = effects.ambientLoopSound
            mood = effects.ambientMoodSettings
            additions = effects.ambientAdditionsSettings
            backgroundMusic = effects.backgroundMusic
        }

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
        val CODEC: Codec<BiomeEffects> = RecordCodecBuilder.create {
            it.group(
                Codecs.COLOR.fieldOf("fog_color").getting(BiomeEffects::fogColor),
                Codecs.COLOR.fieldOf("water_color").getting(BiomeEffects::waterColor),
                Codecs.COLOR.fieldOf("water_fog_color").getting(BiomeEffects::waterFogColor),
                Codecs.COLOR.fieldOf("sky_color").getting(BiomeEffects::skyColor),
                EnumCodecs.GRASS_COLOR_MODIFIER.fieldOf("grass_color_modifier").getting(BiomeEffects::grassColorModifier),
                Codecs.COLOR.nullableFieldOf("foliage_color").getting(BiomeEffects::foliageColor),
                Codecs.COLOR.nullableFieldOf("grass_color").getting(BiomeEffects::grassColor),
                KryptonAmbientParticleSettings.CODEC.nullableFieldOf("particle").getting(BiomeEffects::ambientParticleSettings),
                Codecs.SOUND_EVENT.nullableFieldOf("ambient_sound").getting(BiomeEffects::ambientLoopSound),
                KryptonAmbientMoodSettings.CODEC.nullableFieldOf("mood_sound").getting(BiomeEffects::ambientMoodSettings),
                KryptonAmbientAdditionsSettings.CODEC.nullableFieldOf("additions_sound").getting(BiomeEffects::ambientAdditionsSettings),
                KryptonMusic.CODEC.nullableFieldOf("music").getting(BiomeEffects::backgroundMusic)
            ).apply(it, ::KryptonBiomeEffects)
        }
    }
}

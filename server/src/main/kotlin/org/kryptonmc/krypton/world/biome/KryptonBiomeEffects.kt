/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.effect.Music
import org.kryptonmc.krypton.effect.KryptonMusic
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.world.biome.AmbientAdditionsSettings
import org.kryptonmc.api.world.biome.AmbientMoodSettings
import org.kryptonmc.api.world.biome.AmbientParticleSettings
import org.kryptonmc.api.world.biome.BiomeEffects
import org.kryptonmc.api.world.biome.GrassColorModifier
import org.kryptonmc.api.world.biome.GrassColorModifiers
import org.kryptonmc.krypton.util.Codecs
import org.kryptonmc.krypton.util.nullableFieldOf
import java.awt.Color

@JvmRecord
data class KryptonBiomeEffects(
    override val fogColor: Color,
    override val waterColor: Color,
    override val waterFogColor: Color,
    override val skyColor: Color,
    override val grassColorModifier: GrassColorModifier = GrassColorModifiers.NONE,
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

        private var fogColor = Color.BLACK
        private var waterColor = Color.BLACK
        private var waterFogColor = Color.BLACK
        private var skyColor = Color.BLACK
        private var grassColorModifier = GrassColorModifiers.NONE
        private var foliageColor: Color? = null
        private var grassColor: Color? = null
        private var particles: AmbientParticleSettings? = null
        private var loopSound: SoundEvent? = null
        private var mood: AmbientMoodSettings? = null
        private var additions: AmbientAdditionsSettings? = null
        private var backgroundMusic: Music? = null

        constructor(effects: BiomeEffects) : this() {
            fogColor = effects.fogColor
            waterColor = effects.waterColor
            waterFogColor = effects.waterFogColor
            skyColor = effects.skyColor
            grassColorModifier = effects.grassColorModifier
            foliageColor = effects.foliageColor
            grassColor = effects.grassColor
            particles = effects.ambientParticleSettings
            loopSound = effects.ambientLoopSound
            mood = effects.ambientMoodSettings
            additions = effects.ambientAdditionsSettings
            backgroundMusic = effects.backgroundMusic
        }

        override fun fogColor(color: Color): BiomeEffects.Builder = apply { fogColor = color }

        override fun waterColor(color: Color): BiomeEffects.Builder = apply { waterColor = color }

        override fun waterFogColor(color: Color): BiomeEffects.Builder = apply { waterFogColor = color }

        override fun skyColor(color: Color): BiomeEffects.Builder = apply { skyColor = color }

        override fun grassColorModifier(modifier: GrassColorModifier): BiomeEffects.Builder = apply { grassColorModifier = modifier }

        override fun foliageColor(color: Color?): BiomeEffects.Builder = apply { foliageColor = color }

        override fun grassColor(color: Color?): BiomeEffects.Builder = apply { grassColor = color }

        override fun particles(settings: AmbientParticleSettings?): BiomeEffects.Builder = apply { particles = settings }

        override fun loopSound(sound: SoundEvent?): BiomeEffects.Builder = apply { loopSound = sound }

        override fun mood(settings: AmbientMoodSettings?): BiomeEffects.Builder = apply { mood = settings }

        override fun additions(settings: AmbientAdditionsSettings?): BiomeEffects.Builder = apply { additions = settings }

        override fun backgroundMusic(music: Music?): BiomeEffects.Builder = apply { backgroundMusic = music }

        override fun build(): BiomeEffects = KryptonBiomeEffects(
            fogColor,
            waterColor,
            waterFogColor,
            skyColor,
            grassColorModifier,
            foliageColor,
            grassColor,
            particles,
            loopSound,
            mood,
            additions,
            backgroundMusic
        )
    }

    object Factory : BiomeEffects.Factory {

        override fun of(
            fogColor: Color,
            waterColor: Color,
            waterFogColor: Color,
            skyColor: Color,
            grassColorModifier: GrassColorModifier,
            foliageColor: Color?,
            grassColor: Color?,
            ambientParticleSettings: AmbientParticleSettings?,
            ambientLoopSound: SoundEvent?,
            ambientMoodSettings: AmbientMoodSettings?,
            ambientAdditionsSettings: AmbientAdditionsSettings?,
            backgroundMusic: Music?
        ): BiomeEffects = KryptonBiomeEffects(
            fogColor,
            waterColor,
            waterFogColor,
            skyColor,
            grassColorModifier,
            foliageColor,
            grassColor,
            ambientParticleSettings,
            ambientLoopSound,
            ambientMoodSettings,
            ambientAdditionsSettings,
            backgroundMusic
        )

        override fun builder(): BiomeEffects.Builder = Builder()
    }

    companion object {

        @JvmField
        val DEFAULT: BiomeEffects = Builder().build()

        @JvmField
        val CODEC: Codec<BiomeEffects> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codecs.COLOR.fieldOf("fog_color").forGetter(BiomeEffects::fogColor),
                Codecs.COLOR.fieldOf("water_color").forGetter(BiomeEffects::waterColor),
                Codecs.COLOR.fieldOf("water_fog_color").forGetter(BiomeEffects::waterFogColor),
                Codecs.COLOR.fieldOf("sky_color").forGetter(BiomeEffects::skyColor),
                KryptonGrassColorModifier.CODEC
                    .optionalFieldOf("grass_color_modifier", GrassColorModifiers.NONE)
                    .forGetter(BiomeEffects::grassColorModifier),
                Codecs.COLOR.nullableFieldOf("foliage_color").forGetter(BiomeEffects::foliageColor),
                Codecs.COLOR.nullableFieldOf("grass_color").forGetter(BiomeEffects::grassColor),
                KryptonAmbientParticleSettings.CODEC.nullableFieldOf("particle").forGetter(BiomeEffects::ambientParticleSettings),
                Codecs.SOUND_EVENT.nullableFieldOf("ambient_sound").forGetter(BiomeEffects::ambientLoopSound),
                KryptonAmbientMoodSettings.CODEC.nullableFieldOf("mood_sound").forGetter(BiomeEffects::ambientMoodSettings),
                KryptonAmbientAdditionsSettings.CODEC.nullableFieldOf("additions_sound").forGetter(BiomeEffects::ambientAdditionsSettings),
                KryptonMusic.CODEC.nullableFieldOf("music").forGetter(BiomeEffects::backgroundMusic)
            ).apply(instance, ::KryptonBiomeEffects)
        }
    }
}

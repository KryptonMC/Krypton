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

import org.kryptonmc.api.effect.Music
import org.kryptonmc.krypton.effect.KryptonMusic
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.world.biome.AmbientAdditionsSettings
import org.kryptonmc.api.world.biome.AmbientMoodSettings
import org.kryptonmc.api.world.biome.AmbientParticleSettings
import org.kryptonmc.api.world.biome.BiomeEffects
import org.kryptonmc.api.world.biome.GrassColorModifier
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.serialization.CompoundEncoder
import org.kryptonmc.krypton.util.serialization.EnumCodecs
import org.kryptonmc.krypton.util.serialization.encode
import org.kryptonmc.nbt.compound
import java.awt.Color

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

        private var fogColor = Color.BLACK
        private var waterColor = Color.BLACK
        private var waterFogColor = Color.BLACK
        private var skyColor = Color.BLACK
        private var grassColorModifier = GrassColorModifier.NONE
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

        override fun builder(): BiomeEffects.Builder = Builder()
    }

    companion object {

        @JvmField
        val DEFAULT: BiomeEffects = Builder().build()

        @JvmField
        val ENCODER: CompoundEncoder<BiomeEffects> = CompoundEncoder {
            compound {
                encode(Codecs.COLOR, "fog_color", it.fogColor)
                encode(Codecs.COLOR, "water_color", it.waterColor)
                encode(Codecs.COLOR, "water_fog_color", it.waterFogColor)
                encode(Codecs.COLOR, "sky_color", it.skyColor)
                encode(EnumCodecs.GRASS_COLOR_MODIFIER, "grass_color_modifier", it.grassColorModifier)
                encode(Codecs.COLOR, "foliage_color", it.foliageColor)
                encode(Codecs.COLOR, "grass_color", it.grassColor)
                encode(KryptonAmbientParticleSettings.ENCODER, "particle", it.ambientParticleSettings)
                encode(Codecs.SOUND_EVENT, "ambient_sound", it.ambientLoopSound)
                encode(KryptonAmbientMoodSettings.ENCODER, "mood_sound", it.ambientMoodSettings)
                encode(KryptonAmbientAdditionsSettings.ENCODER, "additions_sound", it.ambientAdditionsSettings)
                encode(KryptonMusic.ENCODER, "music", it.backgroundMusic)
            }
        }
    }
}

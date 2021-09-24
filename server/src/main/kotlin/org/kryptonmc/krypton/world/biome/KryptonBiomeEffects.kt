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
    }

    companion object {

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

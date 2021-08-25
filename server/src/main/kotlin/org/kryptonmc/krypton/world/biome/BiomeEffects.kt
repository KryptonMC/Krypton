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
import org.kryptonmc.krypton.effect.Music
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.StringSerializable
import org.kryptonmc.krypton.util.Codecs
import java.awt.Color
import java.util.Optional

data class BiomeEffects(
    val fogColor: Color,
    val waterColor: Color,
    val waterFogColor: Color,
    val skyColor: Color,
    val grassColorModifier: GrassColorModifier = GrassColorModifier.NONE,
    val foliageColor: Optional<Color> = Optional.empty(),
    val grassColor: Optional<Color> = Optional.empty(),
    val ambientParticleSettings: Optional<AmbientParticleSettings> = Optional.empty(),
    val ambientLoopSound: Optional<SoundEvent> = Optional.empty(),
    val ambientMoodSettings: Optional<AmbientMoodSettings> = Optional.empty(),
    val ambientAdditionsSettings: Optional<AmbientAdditionsSettings> = Optional.empty(),
    val backgroundMusic: Optional<Music> = Optional.empty()
) {

    companion object {

        val CODEC: Codec<BiomeEffects> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codecs.COLOR.fieldOf("fog_color").forGetter(BiomeEffects::fogColor),
                Codecs.COLOR.fieldOf("water_color").forGetter(BiomeEffects::waterColor),
                Codecs.COLOR.fieldOf("water_fog_color").forGetter(BiomeEffects::waterFogColor),
                Codecs.COLOR.fieldOf("sky_color").forGetter(BiomeEffects::skyColor),
                GrassColorModifier.CODEC.optionalFieldOf("grass_color_modifier", GrassColorModifier.NONE).forGetter(BiomeEffects::grassColorModifier),
                Codecs.COLOR.optionalFieldOf("foliage_color").forGetter(BiomeEffects::foliageColor),
                Codecs.COLOR.optionalFieldOf("grass_color").forGetter(BiomeEffects::grassColor),
                AmbientParticleSettings.CODEC.optionalFieldOf("particle").forGetter(BiomeEffects::ambientParticleSettings),
                Codecs.SOUND_EVENT.optionalFieldOf("ambient_sound").forGetter(BiomeEffects::ambientLoopSound),
                AmbientMoodSettings.CODEC.optionalFieldOf("mood_sound").forGetter(BiomeEffects::ambientMoodSettings),
                AmbientAdditionsSettings.CODEC.optionalFieldOf("additions_sound").forGetter(BiomeEffects::ambientAdditionsSettings),
                Music.CODEC.optionalFieldOf("music").forGetter(BiomeEffects::backgroundMusic)
            ).apply(instance, ::BiomeEffects)
        }
    }
}

enum class GrassColorModifier(override val serialized: String) : StringSerializable {

    NONE("none"),
    DARK_FOREST("dark_forest"),
    SWAMP("swamp");

    companion object {

        private val BY_NAME = values().associateBy { it.serialized }
        val CODEC = Codecs.forEnum(values()) { BY_NAME[it] }
    }
}

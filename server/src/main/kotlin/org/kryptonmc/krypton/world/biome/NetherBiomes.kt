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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.Music
import org.kryptonmc.api.effect.particle.ParticleTypes
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.world.biome.AmbientParticleSettings
import org.kryptonmc.api.world.biome.BiomeCategories
import org.kryptonmc.api.world.biome.Precipitations
import org.kryptonmc.api.world.biome.biome
import org.kryptonmc.krypton.effect.KryptonMusics
import java.awt.Color

object NetherBiomes {

    private const val TEMPERATURE = 2F
    private val SKY_COLOR = OverworldBiomes.calculateSkyColor(2F)

    @JvmStatic
    fun netherWastes(key: Key): KryptonBiome = baseNether(
        key,
        3344392,
        SoundEvents.AMBIENT_NETHER_WASTES_LOOP,
        SoundEvents.AMBIENT_NETHER_WASTES_MOOD,
        SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS,
        KryptonMusics.NETHER_WASTES
    )

    @JvmStatic
    fun soulSandValley(key: Key): KryptonBiome = baseNether(
        key,
        1787717,
        SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP,
        SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD,
        SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS,
        KryptonMusics.SOUL_SAND_VALLEY,
        AmbientParticleSettings.of(ParticleTypes.ASH, null, 0.00625F)
    )

    @JvmStatic
    fun basaltDeltas(key: Key): KryptonBiome = baseNether(
        key,
        6840176,
        SoundEvents.AMBIENT_BASALT_DELTAS_LOOP,
        SoundEvents.AMBIENT_BASALT_DELTAS_MOOD,
        SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS,
        KryptonMusics.BASALT_DELTAS,
        AmbientParticleSettings.of(ParticleTypes.WHITE_ASH, null, 0.118093334F)
    )

    @JvmStatic
    fun crimsonForest(key: Key): KryptonBiome = baseNether(
        key,
        3343107,
        SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP,
        SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD,
        SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS,
        KryptonMusics.CRIMSON_FOREST,
        AmbientParticleSettings.of(ParticleTypes.CRIMSON_SPORE, null, 0.025F)
    )

    @JvmStatic
    fun warpedForest(key: Key): KryptonBiome = baseNether(
        key,
        1705242,
        SoundEvents.AMBIENT_WARPED_FOREST_LOOP,
        SoundEvents.AMBIENT_WARPED_FOREST_MOOD,
        SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS,
        KryptonMusics.WARPED_FOREST,
        AmbientParticleSettings.of(ParticleTypes.WARPED_SPORE, null, 0.01428F)
    )

    @JvmStatic
    private fun baseNether(
        key: Key,
        fogColor: Int,
        loopSound: SoundEvent,
        moodSound: SoundEvent,
        additionsSound: SoundEvent,
        music: Music,
        particles: AmbientParticleSettings? = null
    ): KryptonBiome = biome(key) {
        climate {
            precipitation(Precipitations.NONE)
            temperature(TEMPERATURE)
            downfall(0F)
        }
        category(BiomeCategories.NETHER)
        effects {
            waterColor(OverworldBiomes.OVERWORLD_WATER)
            waterFogColor(OverworldBiomes.OVERWORLD_WATER_FOG)
            fogColor(Color(fogColor))
            skyColor(SKY_COLOR)
            particles(particles)
            loopSound(loopSound)
            mood(moodSound) {
                delay(6000)
                searchExtent(8)
                offset(2.0)
            }
            additions(additionsSound) {
                probability(0.0111)
            }
            backgroundMusic(music)
        }
    } as KryptonBiome
}

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

import org.kryptonmc.api.effect.particle.ParticleTypes
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.util.Color
import org.kryptonmc.api.world.biome.AmbientAdditionsSettings
import org.kryptonmc.api.world.biome.AmbientMoodSettings
import org.kryptonmc.api.world.biome.AmbientParticleSettings
import org.kryptonmc.api.world.biome.Climate
import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.api.world.biome.TemperatureModifier
import org.kryptonmc.krypton.effect.KryptonMusic

object NetherBiomes {

    private val SKY_COLOR = OverworldBiomes.calculateSkyColor(2F)

    private const val WASTES_FOG = 3344392 // Red: 51, Green: 8, Blue: 8
    private const val VALLEY_FOG = 1787717 // Red: 27, Green: 71, Blue: 69
    private const val DELTAS_FOG = 6840176 // Red: 104, Green: 95, Blue: 112
    private const val CRIMSON_FOG = 3343107 // Red: 51, Green: 3, Blue: 3
    private const val WARPED_FOG = 1705242 // Red: 26, Green: 5, Blue: 26

    private val WASTES_LOOP = SoundEvents.AMBIENT_NETHER_WASTES_LOOP.get()
    private val VALLEY_LOOP = SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP.get()
    private val DELTAS_LOOP = SoundEvents.AMBIENT_BASALT_DELTAS_LOOP.get()
    private val CRIMSON_LOOP = SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP.get()
    private val WARPED_LOOP = SoundEvents.AMBIENT_WARPED_FOREST_LOOP.get()

    private val WASTES_MOOD = SoundEvents.AMBIENT_NETHER_WASTES_MOOD.get()
    private val VALLEY_MOOD = SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD.get()
    private val DELTAS_MOOD = SoundEvents.AMBIENT_BASALT_DELTAS_MOOD.get()
    private val CRIMSON_MOOD = SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD.get()
    private val WARPED_MOOD = SoundEvents.AMBIENT_WARPED_FOREST_MOOD.get()

    private val WASTES_ADDITIONS = SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS.get()
    private val VALLEY_ADDITIONS = SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS.get()
    private val DELTAS_ADDITIONS = SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS.get()
    private val CRIMSON_ADDITIONS = SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS.get()
    private val WARPED_ADDITIONS = SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS.get()

    private val WASTES_MUSIC = SoundEvents.MUSIC_BIOME_NETHER_WASTES.get()
    private val VALLEY_MUSIC = SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY.get()
    private val DELTAS_MUSIC = SoundEvents.MUSIC_BIOME_BASALT_DELTAS.get()
    private val CRIMSON_MUSIC = SoundEvents.MUSIC_BIOME_CRIMSON_FOREST.get()
    private val WARPED_MUSIC = SoundEvents.MUSIC_BIOME_WARPED_FOREST.get()

    private val VALLEY_PARTICLES = AmbientParticleSettings.of(ParticleTypes.ASH.get(), null, 0.00625F)
    private val DELTAS_PARTICLES = AmbientParticleSettings.of(ParticleTypes.WHITE_ASH.get(), null, 0.118093334F)
    private val CRIMSON_PARTICLES = AmbientParticleSettings.of(ParticleTypes.CRIMSON_SPORE.get(), null, 0.025F)
    private val WARPED_PARTICLES = AmbientParticleSettings.of(ParticleTypes.WARPED_SPORE.get(), null, 0.01428F)

    @JvmStatic
    fun netherWastes(): KryptonBiome = baseNether(WASTES_FOG, WASTES_LOOP, WASTES_MOOD, WASTES_ADDITIONS, WASTES_MUSIC)

    @JvmStatic
    fun soulSandValley(): KryptonBiome = baseNether(VALLEY_FOG, VALLEY_LOOP, VALLEY_MOOD, VALLEY_ADDITIONS, VALLEY_MUSIC, VALLEY_PARTICLES)

    @JvmStatic
    fun basaltDeltas(): KryptonBiome = baseNether(DELTAS_FOG, DELTAS_LOOP, DELTAS_MOOD, DELTAS_ADDITIONS, DELTAS_MUSIC, DELTAS_PARTICLES)

    @JvmStatic
    fun crimsonForest(): KryptonBiome = baseNether(CRIMSON_FOG, CRIMSON_LOOP, CRIMSON_MOOD, CRIMSON_ADDITIONS, CRIMSON_MUSIC, CRIMSON_PARTICLES)

    @JvmStatic
    fun warpedForest(): KryptonBiome = baseNether(WARPED_FOG, WARPED_LOOP, WARPED_MOOD, WARPED_ADDITIONS, WARPED_MUSIC, WARPED_PARTICLES)

    @JvmStatic
    private fun baseNether(
        fog: Int,
        loop: SoundEvent,
        mood: SoundEvent,
        additions: SoundEvent,
        music: SoundEvent,
        particles: AmbientParticleSettings? = null
    ): KryptonBiome = KryptonBiome.Builder().apply {
        climate(Climate.of(Precipitation.NONE, 2F, 0F, TemperatureModifier.NONE))
        effects {
            waterColor(OverworldBiomes.OVERWORLD_WATER)
            waterFogColor(OverworldBiomes.OVERWORLD_WATER_FOG)
            fogColor(Color.of(fog))
            skyColor(SKY_COLOR)
            particles(particles)
            loopSound(loop)
            mood(AmbientMoodSettings.of(mood, 6000, 8, 2.0))
            additions(AmbientAdditionsSettings.of(additions, 0.0111))
            backgroundMusic(KryptonMusic.game(music))
        }
    }.build()
}

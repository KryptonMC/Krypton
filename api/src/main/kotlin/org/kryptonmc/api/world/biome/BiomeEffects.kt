/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import org.kryptonmc.api.effect.sound.Music
import org.kryptonmc.api.effect.sound.SoundEvent

/**
 * Holder for effects data for a [Biome].
 *
 * @param fogColor the colour of fog
 * @param waterColor the colour of water
 * @param waterFogColor the colour of water fog
 * @param skyColor the colour of the sky
 * @param grassColorModifier the modifier to apply to the grass colour
 * @param foliageColor the overridden colour of the foliage
 * @param grassColor the overridden colour of the grass
 * @param ambientParticleSettings the settings for the particles that may be
 * spawned in this biome
 * @param ambientLoopSound the sound that may be looped in the background when in
 * this biome
 * @param ambientMoodSettings the settings for the mood sound that may be played
 * in the background while in this biome
 * @param ambientAdditionsSettings the settings for the additions sound that may
 * be played in the background while in this biome
 * @param backgroundMusic the music that may be played in the background
 */
data class BiomeEffects(
    val fogColor: Int,
    val waterColor: Int,
    val waterFogColor: Int,
    val skyColor: Int,
    val grassColorModifier: GrassColorModifier,
    val foliageColor: Int? = null,
    val grassColor: Int? = null,
    val ambientParticleSettings: AmbientParticleSettings? = null,
    val ambientLoopSound: SoundEvent? = null,
    val ambientMoodSettings: AmbientMoodSettings? = null,
    val ambientAdditionsSettings: AmbientAdditionsSettings? = null,
    val backgroundMusic: Music? = null
)

/**
 * Grass colour modifiers for a biome.
 */
enum class GrassColorModifier {

    NONE,
    DARK_FOREST,
    SWAMP;

    override fun toString() = name.lowercase()
}

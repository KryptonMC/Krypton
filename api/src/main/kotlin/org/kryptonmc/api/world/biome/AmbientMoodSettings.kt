/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import org.kryptonmc.api.effect.sound.SoundEvent

/**
 * Settings for the mood sound that may play in the background when in a biome.
 *
 * @param sound the sound that may be played
 * @param tickDelay the delay before the sound is played
 * @param blockSearchExtent the radius of which the client will search for a block
 * @param soundPositionOffset the offset from the client's position where the
 * sound will be played from
 */
data class AmbientMoodSettings(
    val sound: SoundEvent,
    val tickDelay: Int,
    val blockSearchExtent: Int,
    val soundPositionOffset: Double
)

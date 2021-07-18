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
 * Settings for the additions sound that may play in the background when in a biome.
 *
 * @param sound the sound
 * @param probability the probability that the sound may play in a tick
 */
data class AmbientAdditionsSettings(
    val sound: SoundEvent,
    val probability: Int
)

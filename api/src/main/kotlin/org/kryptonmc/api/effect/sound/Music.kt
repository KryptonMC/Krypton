/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.sound

/**
 * Represents music that may be played to a client.
 *
 * @param sound the sound event to play
 * @param minimumDelay the minimum delay before the music is played
 * @param maximumDelay the maximum delay before the music is played
 * @param replace if the music should replace any currently playing music
 */
data class Music(
    val sound: SoundEvent,
    val minimumDelay: Int,
    val maximumDelay: Int,
    val replace: Boolean
)

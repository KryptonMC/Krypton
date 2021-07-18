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
 * All built-in music types.
 */
object Musics {

    // @formatter:off
    @JvmField val MENU = Music(SoundEvents.MUSIC_MENU, ONE_SECOND, THIRTY_SECONDS, true)
    @JvmField val CREATIVE = Music(SoundEvents.MUSIC_CREATIVE, TEN_MINUTES, TWENTY_MINUTES, false)
    @JvmField val CREDITS = Music(SoundEvents.MUSIC_CREDITS, 0, 0, true)
    @JvmField val END_BOSS = Music(SoundEvents.MUSIC_DRAGON, 0, 0, true)
    @JvmField val END = Music(SoundEvents.MUSIC_END, FIVE_MINUTES, TWENTY_MINUTES, true)
    @JvmField val UNDER_WATER = Music(SoundEvents.MUSIC_UNDER_WATER, TEN_MINUTES, TWENTY_MINUTES, false)
    @JvmField val GAME = Music(SoundEvents.MUSIC_GAME, TEN_MINUTES, TWENTY_MINUTES, false)
    // @formatter:on
}

// Time in ticks constants
private const val ONE_SECOND = 20
private const val THIRTY_SECONDS = 600
private const val FIVE_MINUTES = 6000
private const val TEN_MINUTES = 12000
private const val TWENTY_MINUTES = 24000

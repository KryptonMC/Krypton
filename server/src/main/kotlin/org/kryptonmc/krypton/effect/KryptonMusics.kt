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
package org.kryptonmc.krypton.effect

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

object KryptonMusics {

    // Time in ticks constants
    private const val ONE_SECOND = 20
    private const val THIRTY_SECONDS = 600
    private const val FIVE_MINUTES = 6000
    private const val TEN_MINUTES = 12000
    private const val TWENTY_MINUTES = 24000

    val MENU = register(SoundEvents.MUSIC_MENU, ONE_SECOND, THIRTY_SECONDS, true)
    val CREATIVE = register(SoundEvents.MUSIC_CREATIVE, TEN_MINUTES, TWENTY_MINUTES, false)
    val CREDITS = register(SoundEvents.MUSIC_CREDITS, 0, 0, true)
    val END_BOSS = register(SoundEvents.MUSIC_DRAGON, 0, 0, true)
    val END = register(SoundEvents.MUSIC_END, FIVE_MINUTES, TWENTY_MINUTES, true)
    val UNDER_WATER = register(SoundEvents.MUSIC_UNDER_WATER, TEN_MINUTES, TWENTY_MINUTES, false)
    val GAME = register(SoundEvents.MUSIC_GAME, TEN_MINUTES, TWENTY_MINUTES, false)
    val NETHER_WASTES = register(SoundEvents.MUSIC_BIOME_NETHER_WASTES, TEN_MINUTES, TWENTY_MINUTES, false)
    val SOUL_SAND_VALLEY = register(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY, TEN_MINUTES, TWENTY_MINUTES, false)
    val BASALT_DELTAS = register(SoundEvents.MUSIC_BIOME_BASALT_DELTAS, TEN_MINUTES, TWENTY_MINUTES, false)
    val CRIMSON_FOREST = register(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST, TEN_MINUTES, TWENTY_MINUTES, false)
    val WARPED_FOREST = register(SoundEvents.MUSIC_BIOME_WARPED_FOREST, TEN_MINUTES, TWENTY_MINUTES, false)

    private fun register(sound: SoundEvent, minDelay: Int, maxDelay: Int, replace: Boolean): KryptonMusic = Registries.register(
        Registries.MUSIC,
        sound.key(),
        KryptonMusic(sound, minDelay, maxDelay, replace)
    ) as KryptonMusic
}


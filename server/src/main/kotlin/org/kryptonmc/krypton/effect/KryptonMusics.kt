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

object KryptonMusics {

    // Time in ticks constants
    private const val ONE_SECOND = 20
    private const val THIRTY_SECONDS = 600
    private const val FIVE_MINUTES = 6000
    private const val TEN_MINUTES = 12000
    private const val TWENTY_MINUTES = 24000

    @JvmField val MENU = register(SoundEvents.MUSIC_MENU, ONE_SECOND, THIRTY_SECONDS, true)
    @JvmField val CREATIVE = register(SoundEvents.MUSIC_CREATIVE, TEN_MINUTES, TWENTY_MINUTES, false)
    @JvmField val CREDITS = register(SoundEvents.MUSIC_CREDITS, 0, 0, true)
    @JvmField val END_BOSS = register(SoundEvents.MUSIC_DRAGON, 0, 0, true)
    @JvmField val END = register(SoundEvents.MUSIC_END, FIVE_MINUTES, TWENTY_MINUTES, true)
    @JvmField val UNDER_WATER = registerGame(SoundEvents.MUSIC_UNDER_WATER)
    @JvmField val GAME = registerGame(SoundEvents.MUSIC_GAME)
    @JvmField val NETHER_WASTES = game(SoundEvents.MUSIC_BIOME_NETHER_WASTES)
    @JvmField val SOUL_SAND_VALLEY = game(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY)
    @JvmField val BASALT_DELTAS = game(SoundEvents.MUSIC_BIOME_BASALT_DELTAS)
    @JvmField val CRIMSON_FOREST = game(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST)
    @JvmField val WARPED_FOREST = game(SoundEvents.MUSIC_BIOME_WARPED_FOREST)
    @JvmField val MEADOW = game(SoundEvents.MUSIC_BIOME_MEADOW)
    @JvmField val FROZEN_PEAKS = game(SoundEvents.MUSIC_BIOME_FROZEN_PEAKS)
    @JvmField val JAGGED_PEAKS = game(SoundEvents.MUSIC_BIOME_JAGGED_PEAKS)
    @JvmField val STONY_PEAKS = game(SoundEvents.MUSIC_BIOME_STONY_PEAKS)
    @JvmField val SNOWY_SLOPES = game(SoundEvents.MUSIC_BIOME_SNOWY_SLOPES)
    @JvmField val GROVE = game(SoundEvents.MUSIC_BIOME_GROVE)
    @JvmField val LUSH_CAVES = game(SoundEvents.MUSIC_BIOME_LUSH_CAVES)
    @JvmField val DRIPSTONE_CAVES = game(SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES)

    @JvmStatic
    private fun game(sound: SoundEvent): KryptonMusic = KryptonMusic(sound, TEN_MINUTES, TWENTY_MINUTES, false)

    @JvmStatic
    private fun registerGame(sound: SoundEvent): KryptonMusic = Registries.MUSIC.register(sound.key(), game(sound))

    @JvmStatic
    private fun register(sound: SoundEvent, minDelay: Int, maxDelay: Int, replace: Boolean): KryptonMusic = Registries.MUSIC.register(
        sound.key(),
        KryptonMusic(sound, minDelay, maxDelay, replace)
    )
}


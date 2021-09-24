/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla music.
 */
@Suppress("UndocumentedPublicProperty")
@Catalogue(Music::class)
public object Musics {

    // @formatter:on
    @JvmField public val MENU: Music = get(SoundEvents.MUSIC_MENU)
    @JvmField public val CREATIVE: Music = get(SoundEvents.MUSIC_CREATIVE)
    @JvmField public val CREDITS: Music = get(SoundEvents.MUSIC_CREDITS)
    @JvmField public val END_BOSS: Music = get(SoundEvents.MUSIC_DRAGON)
    @JvmField public val END: Music = get(SoundEvents.MUSIC_END)
    @JvmField public val UNDER_WATER: Music = get(SoundEvents.MUSIC_UNDER_WATER)
    @JvmField public val GAME: Music = get(SoundEvents.MUSIC_GAME)
    @JvmField public val NETHER_WASTES: Music = get(SoundEvents.MUSIC_BIOME_NETHER_WASTES)
    @JvmField public val SOUL_SAND_VALLEY: Music = get(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY)
    @JvmField public val BASALT_DELTAS: Music = get(SoundEvents.MUSIC_BIOME_BASALT_DELTAS)
    @JvmField public val CRIMSON_FOREST: Music = get(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST)
    @JvmField public val WARPED_FOREST: Music = get(SoundEvents.MUSIC_BIOME_WARPED_FOREST)

    // @formatter:off
    private fun get(sound: SoundEvent): Music = Registries.MUSIC[sound.key()]!!
}

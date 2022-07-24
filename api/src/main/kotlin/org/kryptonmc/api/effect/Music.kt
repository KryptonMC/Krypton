/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.provide

/**
 * Music that may be played. This has a minimum and maximum delay before the
 * music will start playing, and the music can start playing anywhere in
 * between this time frame.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Music {

    /**
     * The sound that will be played.
     */
    @get:JvmName("sound")
    public val sound: SoundEvent

    /**
     * The minimum delay before the music will start playing.
     */
    @get:JvmName("minimumDelay")
    public val minimumDelay: Int

    /**
     * The maximum delay before the music will start playing.
     */
    @get:JvmName("maximumDelay")
    public val maximumDelay: Int

    /**
     * If this music should replace any currently playing music.
     *
     * For example, the menu, credits, and ender dragon fight music will do
     * this.
     */
    @get:JvmName("replaceCurrentMusic")
    public val replaceCurrentMusic: Boolean

    @ApiStatus.Internal
    public interface Factory {

        public fun of(sound: SoundEvent, minimumDelay: Int, maximumDelay: Int, replaceCurrentMusic: Boolean): Music
    }

    public companion object {

        /**
         * Creates new music with the given values.
         *
         * @param sound the sound that will be played
         * @param minimumDelay the minimum delay before starting
         * @param maximumDelay the maximum delay before starting
         * @param replaceCurrentMusic if the current music should be replaced
         */
        @JvmStatic
        @Contract("_, _, _, _ -> new", pure = true)
        public fun of(sound: SoundEvent, minimumDelay: Int, maximumDelay: Int, replaceCurrentMusic: Boolean): Music =
            Krypton.factoryProvider.provide<Factory>().of(sound, minimumDelay, maximumDelay, replaceCurrentMusic)
    }
}

/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.effect

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * Music that may be played. This has a minimum and maximum delay before the
 * music will start playing, and the music can start playing anywhere in
 * between this time frame.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
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
    @TypeFactory
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
            Krypton.factory<Factory>().of(sound, minimumDelay, maximumDelay, replaceCurrentMusic)
    }
}

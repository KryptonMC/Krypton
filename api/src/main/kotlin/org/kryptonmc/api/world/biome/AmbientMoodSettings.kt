/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.provide

/**
 * Settings for ambient mood sounds that will play whilst in certain biomes.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface AmbientMoodSettings {

    /**
     * The sound that will be played.
     */
    @get:JvmName("sound")
    public val sound: SoundEvent

    /**
     * The delay, in ticks, before the sound starts playing.
     */
    @get:JvmName("tickDelay")
    public val tickDelay: Int

    /**
     * The cubic range of possible positions to play the sound.
     *
     * The player is at the centre of the cubic range, and the edge length is
     * `2 * blockSearchExtent + 1` (source: [The Official Minecraft Wiki](https://minecraft.fandom.com/wiki/Biome/JSON_format))
     */
    @get:JvmName("blockSearchExtent")
    public val blockSearchExtent: Int

    /**
     * The offset that the client uses for the position to play the sound from.
     * This will be offset from the client's current position.
     */
    @get:JvmName("offset")
    public val offset: Double

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(sound: SoundEvent, tickDelay: Int, blockSearchExtent: Int, offset: Double): AmbientMoodSettings
    }

    public companion object {

        private val FACTORY = FactoryProvider.INSTANCE.provide<Factory>()

        /**
         * Creates new ambient mood sound settings with the given values.
         *
         * @param sound the sound
         * @param tickDelay the delay, in ticks, before playing the sound
         * @param blockSearchExtent the cubic range of possible positions to
         * play the sound
         * @param offset the offset from the client's position
         * @return new ambient mood sound settings
         */
        @JvmStatic
        public fun of(
            sound: SoundEvent,
            tickDelay: Int,
            blockSearchExtent: Int,
            offset: Double
        ): AmbientMoodSettings = FACTORY.of(sound, tickDelay, blockSearchExtent, offset)
    }
}

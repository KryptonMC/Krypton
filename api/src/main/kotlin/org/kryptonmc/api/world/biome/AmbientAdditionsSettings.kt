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
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.provide

/**
 * Settings for ambient sounds that may play randomly when in the biome, with
 * a fixed chance.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface AmbientAdditionsSettings {

    /**
     * The sound that might be played.
     */
    @get:JvmName("sound")
    public val sound: SoundEvent

    /**
     * The probability of the sound playing on any tick.
     */
    @get:JvmName("probability")
    public val probability: Double

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(sound: SoundEvent, probability: Double): AmbientAdditionsSettings
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates new ambient addition sound settings with the given values.
         *
         * @param sound the sound
         * @param probability the probability
         * @return new ambient addition sound settings
         */
        @JvmStatic
        public fun of(sound: SoundEvent, probability: Double): AmbientAdditionsSettings = FACTORY.of(sound, probability)
    }
}

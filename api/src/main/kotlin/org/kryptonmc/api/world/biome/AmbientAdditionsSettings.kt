/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.provide

/**
 * Settings for ambient sounds that may play randomly when in the biome, with
 * a fixed chance.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface AmbientAdditionsSettings : Buildable<AmbientAdditionsSettings, AmbientAdditionsSettings.Builder> {

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

    /**
     * A builder for ambient additions settings.
     */
    @BiomeDsl
    public interface Builder : Buildable.Builder<AmbientAdditionsSettings> {

        /**
         * Sets the sound for the ambient additions settings to the given
         * [sound].
         *
         * @param sound the sound
         * @return this builder
         * @see AmbientAdditionsSettings.sound
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun sound(sound: SoundEvent): Builder

        /**
         * Sets the probability for the ambient additions settings to the
         * given [probability].
         *
         * @param probability the probability
         * @return this builder
         * @see AmbientAdditionsSettings.probability
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun probability(probability: Double): Builder
    }

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(sound: SoundEvent, probability: Double): AmbientAdditionsSettings

        public fun builder(sound: SoundEvent): Builder
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
        @Contract("_ -> new", pure = true)
        public fun of(sound: SoundEvent, probability: Double): AmbientAdditionsSettings = FACTORY.of(sound, probability)

        /**
         * Creates a new builder for ambient additions settings.
         *
         * @param sound the sound
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(sound: SoundEvent): Builder = FACTORY.builder(sound)
    }
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.Buildable
import javax.annotation.concurrent.Immutable

/**
 * Settings for ambient mood sounds that will play whilst in certain biomes.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@Immutable
public interface AmbientMoodSettings : Buildable<AmbientMoodSettings.Builder, AmbientMoodSettings> {

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

    /**
     * A builder for ambient mood settings.
     */
    @BiomeDsl
    public interface Builder : AbstractBuilder<AmbientMoodSettings> {

        /**
         * Sets the sound for the ambient mood settings.
         *
         * @param sound the sound
         * @return this builder
         * @see AmbientMoodSettings.sound
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun sound(sound: SoundEvent): Builder

        /**
         * Sets the delay, in ticks, for the ambient mood settings.
         *
         * @param delay the delay
         * @return this builder
         * @see AmbientMoodSettings.tickDelay
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun delay(delay: Int): Builder

        /**
         * Sets the block search extent for the ambient mood settings.
         *
         * @param extent the extent
         * @return this builder
         * @see AmbientMoodSettings.blockSearchExtent
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun searchExtent(extent: Int): Builder

        /**
         * Sets the offset for the ambient mood settings.
         *
         * @param offset the offset
         * @return this builder
         * @see AmbientMoodSettings.offset
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun offset(offset: Double): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun of(sound: SoundEvent, tickDelay: Int, blockSearchExtent: Int, offset: Double): AmbientMoodSettings

        public fun builder(sound: SoundEvent): Builder
    }

    public companion object {

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
        @Contract("_, _, _, _ -> new", pure = true)
        public fun of(sound: SoundEvent, tickDelay: Int, blockSearchExtent: Int, offset: Double): AmbientMoodSettings =
            Krypton.factory<Factory>().of(sound, tickDelay, blockSearchExtent, offset)

        /**
         * Creates a new builder for ambient mood settings.
         *
         * @param sound the sound
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(sound: SoundEvent): Builder = Krypton.factory<Factory>().builder(sound)
    }
}

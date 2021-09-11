/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.data

import org.kryptonmc.api.space.Position

/**
 * Holds data for vibration particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface VibrationParticleData : ParticleData {

    /**
     * The starting position of the vibration.
     */
    @get:JvmName("origin")
    public val origin: Position

    /**
     * The ending position of the vibration.
     */
    @get:JvmName("destination")
    public val destination: Position

    /**
     * The time, in ticks, it will take for the vibration to vibrate from
     * the [origin] to the [destination].
     */
    @get:JvmName("ticks")
    public val ticks: Int

    public companion object {

        /**
         * Creates new vibration particle effect data with the given [origin],
         * [destination], and [ticks].
         *
         * @param origin the starting position
         * @param destination the ending position
         * @param ticks the time, in ticks, it takes for the vibration to finish
         * @return new vibration particle effect data
         */
        @JvmStatic
        public fun of(origin: Position, destination: Position, ticks: Int): VibrationParticleData =
            ParticleData.FACTORY.vibration(origin, destination, ticks)
    }
}

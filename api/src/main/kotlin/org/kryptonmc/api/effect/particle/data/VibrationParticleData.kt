/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.data

import org.jetbrains.annotations.Contract
import org.spongepowered.math.vector.Vector3d
import javax.annotation.concurrent.Immutable

/**
 * Holds data for vibration particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@Immutable
public interface VibrationParticleData : ParticleData {

    /**
     * The ending position of the vibration.
     */
    @get:JvmName("destination")
    public val destination: Vector3d

    /**
     * The time, in ticks, it will take for the vibration to vibrate from its
     * starting position to the [destination].
     */
    @get:JvmName("ticks")
    public val ticks: Int

    public companion object {

        /**
         * Creates new vibration particle effect data with the given
         * [destination] and [ticks].
         *
         * @param destination the ending position
         * @param ticks the time, in ticks, it takes for the vibration to finish
         * @return new vibration particle effect data
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(destination: Vector3d, ticks: Int): VibrationParticleData = ParticleData.factory().vibration(destination, ticks)
    }
}

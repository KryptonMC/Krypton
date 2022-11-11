/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import javax.annotation.concurrent.Immutable

/**
 * A three dimensional rotation representing a pitch, yaw, and roll, in
 * degrees.
 *
 * As the values are represented in degrees
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@Immutable
public interface Rotations {

    /**
     * The yaw value, in degrees between 0 and 360.
     */
    @get:JvmName("yaw")
    public val yaw: Float

    /**
     * The pitch value, in degrees between 0 and 360.
     */
    @get:JvmName("pitch")
    public val pitch: Float

    /**
     * The roll value, in degrees between 0 and 360.
     */
    @get:JvmName("roll")
    public val roll: Float

    @ApiStatus.Internal
    public interface Factory {

        public fun of(yaw: Float, pitch: Float, roll: Float): Rotations
    }

    public companion object {

        /**
         * The rotations object with its yaw, pitch, and roll set to 0.
         */
        @JvmField
        public val ZERO: Rotations = of(0F, 0F, 0F)

        /**
         * Creates a new rotations object with the given [yaw], [pitch], and
         * [roll].
         *
         * @param yaw the yaw value
         * @param pitch the pitch value
         * @param roll the roll value
         * @return a new rotations object
         */
        @JvmStatic
        public fun of(yaw: Float, pitch: Float, roll: Float): Rotations = Krypton.factory<Factory>().of(yaw, pitch, roll)
    }
}

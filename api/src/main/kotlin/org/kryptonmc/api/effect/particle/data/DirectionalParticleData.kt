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
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Holds data for directional particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface DirectionalParticleData : ParticleData {

    /**
     * The direction the particle will travel.
     *
     * If this value is null, it will be randomized.
     */
    @get:JvmName("direction")
    public val direction: Vec3d?

    /**
     * The current velocity of the particle in the direction it is moving.
     *
     * If this value is zero, the particle is not moving.
     */
    @get:JvmName("velocity")
    public val velocity: Float

    public companion object {

        /**
         * Creates new directional particle data with the given [direction] and
         * [velocity].
         *
         * @param direction the direction the particle will travel
         * @param velocity the velocity of the particle
         * @return new directional particle data
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun of(direction: Vec3d?, velocity: Float): DirectionalParticleData = ParticleData.factory().directional(direction, velocity)
    }
}

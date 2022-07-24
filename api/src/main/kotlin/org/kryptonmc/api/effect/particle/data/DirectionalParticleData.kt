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
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide
import org.spongepowered.math.vector.Vector3d

/**
 * Holds data for directional particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface DirectionalParticleData : ParticleData {

    /**
     * The direction the particle will travel.
     *
     * If this value is null, it will be randomized.
     */
    @get:JvmName("direction")
    public val direction: Vector3d?

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
        public fun of(direction: Vector3d?, velocity: Float): DirectionalParticleData =
            Krypton.factoryProvider.provide<ParticleData.Factory>().directional(direction, velocity)
    }
}

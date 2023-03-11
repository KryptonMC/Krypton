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

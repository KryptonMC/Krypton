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
 * Holds data for vibration particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface VibrationParticleData : ParticleData {

    /**
     * The ending position of the vibration.
     */
    @get:JvmName("destination")
    public val destination: Vec3d

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
        public fun of(destination: Vec3d, ticks: Int): VibrationParticleData = ParticleData.factory().vibration(destination, ticks)
    }
}

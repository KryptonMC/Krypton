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
import org.kryptonmc.api.util.Color
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Holds data for dust colour transition particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface DustTransitionParticleData : DustParticleData {

    /**
     * The destination colour of the particle.
     */
    @get:JvmName("toColor")
    public val toColor: Color

    public companion object {

        /**
         * Creates new dust colour transition particle data with the given
         * [from] source colour, [scale], and [to] destination colour.
         *
         * @param from the from colour
         * @param scale the scale
         * @param to the to colour
         * @return new dust transition particle data
         */
        @JvmStatic
        @Contract("_, _, _ -> new", pure = true)
        public fun of(from: Color, scale: Float, to: Color): DustTransitionParticleData = ParticleData.factory().transition(from, scale, to)
    }
}

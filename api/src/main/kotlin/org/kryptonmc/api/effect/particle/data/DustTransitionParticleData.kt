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

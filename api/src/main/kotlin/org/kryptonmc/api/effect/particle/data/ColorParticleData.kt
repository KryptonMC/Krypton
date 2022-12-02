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
 * Holds data for coloured particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface ColorParticleData : ParticleData {

    /**
     * The colour of the particle.
     */
    @get:JvmName("color")
    public val color: Color

    public companion object {

        /**
         * Creates new colour particle data with the given [color].
         *
         * @param color the colour
         * @return new colour particle data
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(color: Color): ColorParticleData = ParticleData.factory().color(color)
    }
}

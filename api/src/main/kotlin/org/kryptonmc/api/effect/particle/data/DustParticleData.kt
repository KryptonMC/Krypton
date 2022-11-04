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
import javax.annotation.concurrent.Immutable

/**
 * Holds data for dust particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@Immutable
public interface DustParticleData : ColorParticleData {

    /**
     * The scale of the dust.
     */
    @get:JvmName("scale")
    public val scale: Float

    public companion object {

        /**
         * Creates new dust particle data with the given [color] and [scale].
         *
         * @param color the colour
         * @param scale the scale
         * @return new dust particle data
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun of(color: Color, scale: Float): DustParticleData = ParticleData.factory().dust(color, scale)
    }
}

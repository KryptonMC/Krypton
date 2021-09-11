/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.data

/**
 * Holds data for dust particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface DustParticleData : ColorParticleData {

    /**
     * The scale of the dust.
     */
    @get:JvmName("scale")
    public val scale: Float

    public companion object {

        /**
         * Creates new dust particle data with the given [red], [green], and
         * [blue] RGB components, and the given [scale].
         *
         * @param red the red component
         * @param green the green component
         * @param blue the blue component
         * @param scale the scale of the dust
         * @return new dust particle data
         */
        @JvmStatic
        public fun of(red: Short, green: Short, blue: Short, scale: Float): DustParticleData =
            ParticleData.FACTORY.dust(red, green, blue, scale)
    }
}

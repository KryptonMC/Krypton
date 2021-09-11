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
 * Holds data for coloured particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ColorParticleData : ParticleData {

    /**
     * The red component of the RGB colour.
     */
    @get:JvmName("red")
    public val red: Short

    /**
     * The green component of the RGB colour.
     */
    @get:JvmName("green")
    public val green: Short

    /**
     * The blue component of the RGB colour.
     */
    @get:JvmName("blue")
    public val blue: Short

    public companion object {

        /**
         * Creates new color particle data with the given [red], [green], and
         * [blue] RGB components.
         *
         * @param red the red component
         * @param green the green component
         * @param blue the blue component
         * @return new color particle data
         */
        @JvmStatic
        public fun of(red: Short, green: Short, blue: Short): ColorParticleData =
            ParticleData.FACTORY.color(red, green, blue)
    }
}

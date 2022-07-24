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
import org.kryptonmc.api.util.Color
import org.kryptonmc.api.util.provide

/**
 * Holds data for coloured particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ColorParticleData : ParticleData {

    /**
     * The colour of the particle.
     */
    @get:JvmName("color")
    public val color: Color

    /**
     * The red component of the RGB colour.
     */
    @get:JvmName("red")
    public val red: Int
        get() = color.red

    /**
     * The green component of the RGB colour.
     */
    @get:JvmName("green")
    public val green: Int
        get() = color.green

    /**
     * The blue component of the RGB colour.
     */
    @get:JvmName("blue")
    public val blue: Int
        get() = color.blue

    public companion object {

        /**
         * Creates new colour particle data with the given [color].
         *
         * @param color the colour
         * @return new colour particle data
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(color: Color): ColorParticleData = Krypton.factoryProvider.provide<ParticleData.Factory>().color(color)

        /**
         * Creates new colour particle data with the given [red], [green], and
         * [blue] RGB components.
         *
         * @param red the red component
         * @param green the green component
         * @param blue the blue component
         * @return new color particle data
         */
        @JvmStatic
        @Contract("_, _, _ -> new", pure = true)
        public fun of(red: Int, green: Int, blue: Int): ColorParticleData = of(Color.of(red, green, blue))
    }
}

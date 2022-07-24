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
         * Creates new dust particle data with the given [color] and [scale].
         *
         * @param color the colour
         * @param scale the scale
         * @return new dust particle data
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun of(color: Color, scale: Float): DustParticleData = Krypton.factoryProvider.provide<ParticleData.Factory>().dust(color, scale)

        /**
         * Creates new dust particle data with the given [red], [green], and
         * [blue] RGB components, and the given [scale].
         *
         * @param red the red component
         * @param green the green component
         * @param blue the blue component
         * @param scale the scale
         * @return new dust particle data
         */
        @JvmStatic
        @Contract("_, _, _, _ -> new", pure = true)
        public fun of(red: Int, green: Int, blue: Int, scale: Float): DustParticleData = of(Color.of(red, green, blue), scale)
    }
}

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
 * Holds data for dust colour transition particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface DustTransitionParticleData : DustParticleData {

    /**
     * The destination colour of the particle.
     */
    @get:JvmName("to")
    public val to: Color

    /**
     * The red component of the destination RGB colour.
     */
    @get:JvmName("toRed")
    public val toRed: Int
        get() = to.red

    /**
     * The green component of the destination RGB colour.
     */
    @get:JvmName("toGreen")
    public val toGreen: Int
        get() = to.green

    /**
     * The blue component of the destination RGB colour.
     */
    @get:JvmName("toBlue")
    public val toBlue: Int
        get() = to.blue

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
        public fun of(from: Color, scale: Float, to: Color): DustTransitionParticleData =
            Krypton.factoryProvider.provide<ParticleData.Factory>().transition(from, scale, to)

        /**
         * Creates new dust color transition particle data with the given
         * [fromRed], [fromGreen], and [fromBlue] source colour RGB components,
         * the given dust [scale], and the given [toRed], [toGreen], and
         * [toBlue] destination colour RGB components.
         *
         * @param fromRed the source red component
         * @param fromGreen the source green component
         * @param fromBlue the source blue component
         * @param scale the scale of the dust
         * @param toRed the destination red component
         * @param toGreen the destination green component
         * @param toBlue the destination blue component
         * @return new dust color transition particle data
         */
        @JvmStatic
        @Contract("_, _, _, _, _, _, _ -> new", pure = true)
        public fun of(fromRed: Int, fromGreen: Int, fromBlue: Int, scale: Float, toRed: Int, toGreen: Int, toBlue: Int): DustTransitionParticleData =
            of(Color.of(fromRed, fromGreen, fromBlue), scale, Color.of(toRed, toGreen, toBlue))
    }
}

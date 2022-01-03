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

/**
 * Holds data for dust colour transition particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface DustTransitionParticleData : DustParticleData {

    /**
     * The red component of the destination RGB colour.
     */
    @get:JvmName("toRed")
    public val toRed: Short

    /**
     * The green component of the destination RGB colour.
     */
    @get:JvmName("toGreen")
    public val toGreen: Short

    /**
     * The blue component of the destination RGB colour.
     */
    @get:JvmName("toBlue")
    public val toBlue: Short

    public companion object {

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
        @Contract("_ -> new", pure = true)
        public fun of(
            fromRed: Short,
            fromGreen: Short,
            fromBlue: Short,
            scale: Float,
            toRed: Short,
            toGreen: Short,
            toBlue: Short
        ): DustTransitionParticleData = ParticleData.FACTORY.transition(fromRed, fromGreen, fromBlue, scale, toRed, toGreen, toBlue)
    }
}

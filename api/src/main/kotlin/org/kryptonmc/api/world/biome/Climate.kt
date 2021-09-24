/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.provide

/**
 * The climate for a biome.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Climate {

    /**
     * The precipitation settings for this climate.
     */
    @get:JvmName("precipitation")
    public val precipitation: Precipitation

    /**
     * The temperature of this climate.
     */
    @get:JvmName("temperature")
    public val temperature: Float

    /**
     * The downfall of this climate.
     */
    @get:JvmName("downfall")
    public val downfall: Float

    /**
     * The temperature modifier for this climate.
     */
    @get:JvmName("temperatureModifier")
    public val temperatureModifier: TemperatureModifier

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(precipitation: Precipitation, temperature: Float, downfall: Float, temperatureModifier: TemperatureModifier): Climate
    }

    public companion object {

        private val FACTORY = FactoryProvider.INSTANCE.provide<Factory>()

        /**
         * Creates a new climate with the given values.
         *
         * @param precipitation the precipitation
         * @param temperature the temperature
         * @param downfall the downfall
         * @param temperatureModifier the temperature modifier
         * @return a new climate
         */
        @JvmStatic
        public fun of(
            precipitation: Precipitation,
            temperature: Float,
            downfall: Float,
            temperatureModifier: TemperatureModifier
        ): Climate = FACTORY.of(precipitation, temperature, downfall, temperatureModifier)
    }
}

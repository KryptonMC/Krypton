/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.Buildable
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory
import org.kryptonmc.internal.annotations.dsl.BiomeDsl

/**
 * The climate for a biome.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface Climate : Buildable<Climate.Builder, Climate> {

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

    /**
     * A builder for climates.
     */
    @BiomeDsl
    public interface Builder : AbstractBuilder<Climate> {

        /**
         * Sets the precipitation for the climate to the given [precipitation]
         * and returns this builder.
         *
         * @param precipitation the precipitation
         * @return this builder
         * @see Climate.precipitation
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun precipitation(precipitation: Precipitation): Builder

        /**
         * Sets the temperature for the climate to the given [temperature] and
         * returns this builder.
         *
         * @param temperature the temperature
         * @return this builder
         * @see Climate.temperature
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun temperature(temperature: Float): Builder

        /**
         * Sets the downfall for the climate to the given [downfall] and
         * returns this builder.
         *
         * @param downfall the downfall
         * @return this builder
         * @see Climate.downfall
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun downfall(downfall: Float): Builder

        /**
         * Sets the temperature modifier for the climate to the given
         * [modifier] and returns this builder.
         *
         * @param modifier the temperature modifier
         * @return this builder
         * @see Climate.temperatureModifier
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun temperatureModifier(modifier: TemperatureModifier): Builder
    }

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun of(precipitation: Precipitation, temperature: Float, downfall: Float, temperatureModifier: TemperatureModifier): Climate

        public fun builder(): Builder
    }

    public companion object {

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
        @Contract("_, _, _, _ -> new", pure = true)
        public fun of(precipitation: Precipitation, temperature: Float, downfall: Float, temperatureModifier: TemperatureModifier): Climate =
            Krypton.factory<Factory>().of(precipitation, temperature, downfall, temperatureModifier)

        /**
         * Creates a new builder for climates.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(): Builder = Krypton.factory<Factory>().builder()
    }
}

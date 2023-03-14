/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.world.biome

import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory
import org.kryptonmc.internal.annotations.dsl.BiomeDsl

/**
 * The climate for a biome.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
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
        public fun of(precipitation: Precipitation, temperature: Float, downfall: Float, temperatureModifier: TemperatureModifier): Climate {
            return builder()
                .precipitation(precipitation)
                .temperature(temperature)
                .downfall(downfall)
                .temperatureModifier(temperatureModifier)
                .build()
        }

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

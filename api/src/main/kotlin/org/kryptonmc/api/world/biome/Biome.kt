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
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory
import org.kryptonmc.internal.annotations.dsl.BiomeDsl
import java.util.function.Consumer

/**
 * A biome is a region in a world with distinct geographical features.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Biomes::class)
@ImmutableType
public interface Biome : Keyed {

    /**
     * The climate of this biome.
     */
    @get:JvmName("climate")
    public val climate: Climate

    /**
     * The effects of this biome.
     */
    @get:JvmName("effects")
    public val effects: BiomeEffects

    /**
     * A builder for biomes.
     */
    @BiomeDsl
    public interface Builder : AbstractBuilder<Biome> {

        /**
         * Sets the climate of the biome to the given [climate] and returns
         * this builder.
         *
         * @param climate the climate
         * @return this builder
         * @see Biome.climate
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun climate(climate: Climate): Builder

        /**
         * Applies the given [builder] to a new climate builder and sets the
         * climate to the built instance.
         *
         * @param builder the builder
         * @return this builder
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun climate(builder: Consumer<Climate.Builder>): Builder = climate(Climate.builder().apply { builder.accept(this) }.build())

        /**
         * Sets the effects settings for the biome to the given [effects]
         * settings and returns this builder.
         *
         * @param effects the effects settings
         * @return this builder
         * @see Biome.effects
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun effects(effects: BiomeEffects): Builder

        /**
         * Applies the given [builder] to a new effects settings builder and
         * sets the effects settings to the built instance.
         *
         * @param builder the builder
         * @return this builder
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun effects(builder: Consumer<BiomeEffects.Builder>): Builder = effects(BiomeEffects.builder().apply { builder.accept(this) }.build())
    }

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun builder(): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building a new biome.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(): Builder = Krypton.factory<Factory>().builder()
    }
}

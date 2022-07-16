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
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.Buildable
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.KeyedBuilder
import org.kryptonmc.api.util.provide
import java.util.function.Consumer

/**
 * A biome is a region in a world with distinct geographical features.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Biomes::class)
public interface Biome : Buildable<Biome.Builder, Biome>, Keyed {

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
    public interface Builder : AbstractBuilder<Biome>, KeyedBuilder<Biome, Builder> {

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
        @JvmSynthetic
        @Contract("_ -> this", mutates = "this")
        public fun climate(builder: Climate.Builder.() -> Unit): Builder = climate(Climate.builder().apply(builder).build())

        /**
         * Applies the given [builder] to a new climate builder and sets the
         * climate to the built instance.
         *
         * @param builder the builder
         * @return this builder
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun climate(builder: Consumer<Climate.Builder>): Builder = climate { builder.accept(this) }

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
        @JvmSynthetic
        @Contract("_ -> this", mutates = "this")
        public fun effects(builder: BiomeEffects.Builder.() -> Unit): Builder = effects(BiomeEffects.builder().apply(builder).build())

        /**
         * Applies the given [builder] to a new effects settings builder and
         * sets the effects settings to the built instance.
         *
         * @param builder the builder
         * @return this builder
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun effects(builder: Consumer<BiomeEffects.Builder>): Builder = effects { builder.accept(this) }
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(key: Key): Builder
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new builder for biomes with the given [key].
         *
         * @param key the key
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(key: Key): Builder = FACTORY.builder(key)
    }
}

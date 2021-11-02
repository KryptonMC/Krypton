/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.KeyedBuilder
import org.kryptonmc.api.util.provide
import java.util.function.Consumer

/**
 * A biome is a region in a world with distinct geographical features.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Biomes::class)
public interface Biome : Buildable<Biome, Biome.Builder>, Keyed {

    /**
     * The climate of this biome.
     */
    @get:JvmName("climate")
    public val climate: Climate

    /**
     * The depth of this biome. Used for terrain noise generation.
     */
    @get:JvmName("depth")
    public val depth: Float

    /**
     * The scale of this biome. Used for terrain noise generation.
     */
    @get:JvmName("scale")
    public val scale: Float

    /**
     * The category of this biome.
     */
    @get:JvmName("category")
    public val category: BiomeCategory

    /**
     * The effects of this biome.
     */
    @get:JvmName("effects")
    public val effects: BiomeEffects

    /**
     * A builder for biomes.
     */
    @BiomeDsl
    public interface Builder : KeyedBuilder<Biome, Builder> {

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
         * Sets the depth of the biome to the given [depth] and returns this
         * builder.
         *
         * @param depth the depth
         * @return this builder
         * @see Biome.depth
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun depth(depth: Float): Builder

        /**
         * Sets the scale of the biome to the given [scale] and returns this
         * builder.
         *
         * @param scale the depth
         * @return this builder
         * @see Biome.scale
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun scale(scale: Float): Builder

        /**
         * Sets the category the biome is in to the given [category] and
         * returns this builder.
         *
         * @param category the category
         * @return this builder
         * @see Biome.category
         */
        @BiomeDsl
        @Contract("_ -> this", mutates = "this")
        public fun category(category: BiomeCategory): Builder

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

        public fun of(key: Key, climate: Climate, depth: Float, scale: Float, category: BiomeCategory, effects: BiomeEffects): Biome

        public fun builder(key: Key): Builder
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new biome with the given values.
         *
         * @param key the key
         * @param climate the climate
         * @param depth the depth
         * @param scale the scale
         * @param category the category
         * @param effects the effects
         * @return a new biome
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(
            key: Key,
            climate: Climate,
            depth: Float,
            scale: Float,
            category: BiomeCategory,
            effects: BiomeEffects
        ): Biome = FACTORY.of(key, climate, depth, scale, category, effects)

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

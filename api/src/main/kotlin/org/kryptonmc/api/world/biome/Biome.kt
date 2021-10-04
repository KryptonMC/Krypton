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
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide

/**
 * A biome is a region in a world with distinct geographical features.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Biomes::class)
public interface Biome : Keyed {

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

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key, climate: Climate, depth: Float, scale: Float, category: BiomeCategory, effects: BiomeEffects): Biome
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
        public fun of(
            key: Key,
            climate: Climate,
            depth: Float,
            scale: Float,
            category: BiomeCategory,
            effects: BiomeEffects
        ): Biome = FACTORY.of(key, climate, depth, scale, category, effects)
    }
}

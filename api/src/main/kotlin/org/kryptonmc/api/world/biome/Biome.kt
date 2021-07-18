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

/**
 * Holds data for a biome.
 *
 * @param key the key of this biome
 * @param climate the climate settings for this biome
 * @param depth the depth of this biome
 * @param scale the scale of this biome
 * @param category the category of this biome
 * @param effects the effects settings for this biome
 */
data class Biome(
    val key: Key,
    val climate: ClimateSettings,
    val depth: Float,
    val scale: Float,
    val category: BiomeCategory,
    val effects: BiomeEffects
)

/**
 * Categories of biomes that biomes may fall under.
 */
enum class BiomeCategory {

    NONE,
    TAIGA,
    EXTREME_HILLS,
    JUNGLE,
    MESA,
    PLAINS,
    SAVANNA,
    ICY,
    THE_END,
    BEACH,
    FOREST,
    OCEAN,
    DESERT,
    RIVER,
    SWAMP,
    MUSHROOM,
    NETHER,
    UNDERGROUND;

    override fun toString() = name.lowercase()
}

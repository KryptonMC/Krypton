/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

/**
 * The settings for the climate of a biome.
 *
 * @param precipitation the precipitation of the biome
 * @param temperature the temperature of the biome
 * @param temperatureModifier the temperature modifier of the biome
 * @param downfall the downfall of the biome
 */
data class ClimateSettings(
    val precipitation: Precipitation,
    val temperature: Float,
    val temperatureModifier: TemperatureModifier,
    val downfall: Float
)

/**
 * Precipitations for a biome.
 */
enum class Precipitation {

    NONE,
    RAIN,
    SNOW;

    override fun toString() = name.lowercase()
}

/**
 * Temperature modifiers for a biome.
 */
enum class TemperatureModifier {

    NONE,
    FROZEN;

    override fun toString() = name.lowercase()
}

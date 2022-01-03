/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.biome

import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.world.biome.Climate.Parameter
import org.kryptonmc.krypton.world.biome.Climate.ParameterPoint
import org.kryptonmc.krypton.world.biome.Climate.quantize
import java.util.function.BiConsumer

object OverworldBiomeBuilder {

    private const val VALLEY_SIZE = 0.05F
    private const val LOW_START = 0.26666668F
    private const val HIGH_START = 0.4F
    private const val HIGH_END = 0.93333334F
    private const val PEAK_SIZE = 0.1F
    private const val PEAK_START = 0.56666666F
    private const val PEAK_END = 0.7666667F
    private const val NEAR_INLAND_START = -0.11F
    private const val MID_INLAND_START = 0.03F
    private const val FAR_INLAND_START = 0.3F
    private const val EROSION_INDEX_1_START = -0.78F
    private const val EROSION_INDEX_2_START = -0.375F

    // Parameter constants.
    private val FULL_RANGE = Parameter.range(-1F, 1F)
    private val TEMPERATURES = arrayOf(
        Parameter.range(-1F, -0.45F),
        Parameter.range(-0.45F, -0.15F),
        Parameter.range(-0.15F, 0.2F),
        Parameter.range(0.2F, 0.55F),
        Parameter.range(0.55F, 1F)
    )
    private val HUMIDITIES = arrayOf(
        Parameter.range(-1.0F, -0.35F),
        Parameter.range(-0.35F, -PEAK_SIZE),
        Parameter.range(-PEAK_SIZE, PEAK_SIZE),
        Parameter.range(PEAK_SIZE, FAR_INLAND_START),
        Parameter.range(FAR_INLAND_START, 1.0F)
    )
    private val EROSIONS = arrayOf(
        Parameter.range(-1.0F, EROSION_INDEX_1_START),
        Parameter.range(EROSION_INDEX_1_START, EROSION_INDEX_2_START),
        Parameter.range(EROSION_INDEX_2_START, -0.2225F),
        Parameter.range(-0.2225F, VALLEY_SIZE),
        Parameter.range(VALLEY_SIZE, 0.45F),
        Parameter.range(0.45F, 0.55F),
        Parameter.range(0.55F, 1.0F)
    )
    private val FROZEN_RANGE = TEMPERATURES[0]
    private val UNFROZEN_RANGE = TEMPERATURES[1]..TEMPERATURES[4]

    // Continentalness range constants.
    private val MUSHROOM_FIELDS_CONTINENTALNESS = Parameter.range(-1.2F, -1.05F)
    private val DEEP_OCEAN_CONTINENTALNESS = Parameter.range(-1.05F, -0.455F)
    private val OCEAN_CONTINENTALNESS = Parameter.range(-0.455F, -0.19F)
    private val COAST_CONTINENTALNESS = Parameter.range(-0.19F, NEAR_INLAND_START)
    private val INLAND_CONTINENTALNESS = Parameter.range(NEAR_INLAND_START, 0.55F)
    private val NEAR_INLAND_CONTINENTALNESS = Parameter.range(NEAR_INLAND_START, MID_INLAND_START)
    private val MID_INLAND_CONTINENTALNESS = Parameter.range(MID_INLAND_START, FAR_INLAND_START)
    private val FAR_INLAND_CONTINENTALNESS = Parameter.range(FAR_INLAND_START, 1F)

    // Continentalness compound range constants.
    private val COAST_NEAR_CONTINENTALNESS = COAST_CONTINENTALNESS..NEAR_INLAND_CONTINENTALNESS
    private val COAST_FAR_CONTINENTALNESS = COAST_CONTINENTALNESS..FAR_INLAND_CONTINENTALNESS
    private val MID_FAR_CONTINENTALNESS = MID_INLAND_CONTINENTALNESS..FAR_INLAND_CONTINENTALNESS
    private val NEAR_MID_CONTINENTALNESS = NEAR_INLAND_CONTINENTALNESS..MID_INLAND_CONTINENTALNESS
    private val NEAR_FAR_CONTINENTALNESS = NEAR_INLAND_CONTINENTALNESS..FAR_INLAND_CONTINENTALNESS
    private val INLAND_FAR_CONTINENTALNESS = INLAND_CONTINENTALNESS..FAR_INLAND_CONTINENTALNESS

    // Erosion compound range constants.
    private val FIRST_SECOND_EROSION = EROSIONS[0]..EROSIONS[1]
    private val FIRST_THIRD_EROSION = EROSIONS[0]..EROSIONS[2]
    private val THIRD_FOURTH_EROSION = EROSIONS[2]..EROSIONS[3]
    private val FOURTH_FIFTH_EROSION = EROSIONS[3]..EROSIONS[4]
    private val THIRD_SIXTH_EROSION = EROSIONS[3]..EROSIONS[5]

    // Other constants
    private val UNDERGROUND_DEPTH = Parameter.range(0.2F, 0.9F)
    private val DRIPSTONE_CAVES_CONTINENTALNESS = Parameter.range(0.8F, 1F)
    private val LUSH_CAVES_HUMIDITY = Parameter.range(0.7F, 1F)
    private val BOTTOM_MID_SLICE_WEIRDNESS = Parameter.range(-1F, -HIGH_END)
    private val BOTTOM_HIGH_SLICE_WEIRDNESS = Parameter.range(-HIGH_END, -PEAK_END)
    private val BOTTOM_PEAKS_WEIRDNESS = Parameter.range(-PEAK_END, -PEAK_START)
    private val LOWER_HIGH_SLICE_WEIRDNESS = Parameter.range(-PEAK_START, -HIGH_START)
    private val LOWER_MID_SLICE_WEIRDNESS = Parameter.range(-HIGH_START, -LOW_START)
    private val LOWER_LOW_SLICE_WEIRDNESS = Parameter.range(-LOW_START, -VALLEY_SIZE)
    private val VALLEY_WEIRDNESS = Parameter.range(-VALLEY_SIZE, VALLEY_SIZE)
    private val UPPER_LOW_SLICE_WEIRDNESS = Parameter.range(VALLEY_SIZE, LOW_START)
    private val UPPER_MID_SLICE_WEIRDNESS = Parameter.range(LOW_START, HIGH_START)
    private val UPPER_HIGH_SLICE_WEIRDNESS = Parameter.range(HIGH_START, PEAK_START)
    private val TOP_PEAKS_WEIRDNESS = Parameter.range(PEAK_START, PEAK_END)
    private val TOP_HIGH_SLICE_WEIRDNESS = Parameter.range(PEAK_END, HIGH_END)
    private val TOP_MID_SLICE_WEIRDNESS = Parameter.range(HIGH_END, 1F)

    /*
     * The arrays below are stored based on a temperature-humidity index. That
     * is, the first dimension is indexed by temperature, and the second is
     * indexed by humidity.
     *
     * The indices of this are the same as in their respective arrays.
     * That is to say that the temperature indices of the first dimension
     * will match up with the indices of the TEMPERATURES array, and the same
     * can be said for the HUMIDITIES array.
     */

    private val OCEANS = arrayOf(
        arrayOf(
            BiomeKeys.DEEP_FROZEN_OCEAN,
            BiomeKeys.DEEP_COLD_OCEAN,
            BiomeKeys.DEEP_OCEAN,
            BiomeKeys.DEEP_LUKEWARM_OCEAN,
            BiomeKeys.WARM_OCEAN
        ),
        arrayOf(
            BiomeKeys.FROZEN_OCEAN,
            BiomeKeys.COLD_OCEAN,
            BiomeKeys.OCEAN,
            BiomeKeys.LUKEWARM_OCEAN,
            BiomeKeys.WARM_OCEAN
        )
    )
    private val MIDDLE_BIOMES = arrayOf(
        arrayOf(
            BiomeKeys.SNOWY_PLAINS,
            BiomeKeys.SNOWY_PLAINS,
            BiomeKeys.SNOWY_PLAINS,
            BiomeKeys.SNOWY_TAIGA,
            BiomeKeys.TAIGA
        ),
        arrayOf(
            BiomeKeys.PLAINS,
            BiomeKeys.PLAINS,
            BiomeKeys.FOREST,
            BiomeKeys.TAIGA,
            BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA
        ),
        arrayOf(
            BiomeKeys.FLOWER_FOREST,
            BiomeKeys.PLAINS,
            BiomeKeys.FOREST,
            BiomeKeys.BIRCH_FOREST,
            BiomeKeys.DARK_FOREST
        ),
        arrayOf(
            BiomeKeys.SAVANNA,
            BiomeKeys.SAVANNA,
            BiomeKeys.FOREST,
            BiomeKeys.JUNGLE,
            BiomeKeys.JUNGLE
        ),
        arrayOf(
            BiomeKeys.DESERT,
            BiomeKeys.DESERT,
            BiomeKeys.DESERT,
            BiomeKeys.DESERT,
            BiomeKeys.DESERT
        )
    )
    private val MIDDLE_BIOMES_VARIANT: Array<Array<ResourceKey<Biome>?>> = arrayOf(
        arrayOf(BiomeKeys.ICE_SPIKES, null, BiomeKeys.SNOWY_TAIGA, null, null),
        arrayOf(null, null, null, null, BiomeKeys.OLD_GROWTH_PINE_TAIGA),
        arrayOf(
            BiomeKeys.SUNFLOWER_PLAINS,
            null,
            null,
            BiomeKeys.OLD_GROWTH_BIRCH_FOREST,
            null
        ),
        arrayOf(
            null,
            null,
            BiomeKeys.PLAINS,
            BiomeKeys.SPARSE_JUNGLE,
            BiomeKeys.BAMBOO_JUNGLE
        ),
        arrayOf(null, null, null, null, null)
    )
    private val PLATEAU_BIOMES = arrayOf(
        arrayOf(
            BiomeKeys.SNOWY_PLAINS,
            BiomeKeys.SNOWY_PLAINS,
            BiomeKeys.SNOWY_PLAINS,
            BiomeKeys.SNOWY_TAIGA,
            BiomeKeys.SNOWY_TAIGA
        ),
        arrayOf(
            BiomeKeys.MEADOW,
            BiomeKeys.MEADOW,
            BiomeKeys.FOREST,
            BiomeKeys.TAIGA,
            BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA
        ),
        arrayOf(
            BiomeKeys.MEADOW,
            BiomeKeys.MEADOW,
            BiomeKeys.MEADOW,
            BiomeKeys.MEADOW,
            BiomeKeys.DARK_FOREST
        ),
        arrayOf(
            BiomeKeys.SAVANNA_PLATEAU,
            BiomeKeys.SAVANNA_PLATEAU,
            BiomeKeys.FOREST,
            BiomeKeys.FOREST,
            BiomeKeys.JUNGLE
        ),
        arrayOf(
            BiomeKeys.BADLANDS,
            BiomeKeys.BADLANDS,
            BiomeKeys.BADLANDS,
            BiomeKeys.WOODED_BADLANDS,
            BiomeKeys.WOODED_BADLANDS
        )
    )
    private val PLATEAU_BIOMES_VARIANT: Array<Array<ResourceKey<Biome>?>> = arrayOf(
        arrayOf(BiomeKeys.ICE_SPIKES, null, null, null, null),
        arrayOf(
            null,
            null,
            BiomeKeys.MEADOW,
            BiomeKeys.MEADOW,
            BiomeKeys.OLD_GROWTH_PINE_TAIGA
        ),
        arrayOf(null, null, BiomeKeys.FOREST, BiomeKeys.BIRCH_FOREST, null),
        arrayOf(null, null, null, null, null),
        arrayOf(BiomeKeys.ERODED_BADLANDS, BiomeKeys.ERODED_BADLANDS, null, null, null)
    )
    private val EXTREME_HILLS: Array<Array<ResourceKey<Biome>?>> = arrayOf(
        arrayOf(
            BiomeKeys.WINDSWEPT_GRAVELLY_HILLS,
            BiomeKeys.WINDSWEPT_GRAVELLY_HILLS,
            BiomeKeys.WINDSWEPT_HILLS,
            BiomeKeys.WINDSWEPT_FOREST,
            BiomeKeys.WINDSWEPT_FOREST
        ),
        arrayOf(
            BiomeKeys.WINDSWEPT_GRAVELLY_HILLS,
            BiomeKeys.WINDSWEPT_GRAVELLY_HILLS,
            BiomeKeys.WINDSWEPT_HILLS,
            BiomeKeys.WINDSWEPT_FOREST,
            BiomeKeys.WINDSWEPT_FOREST
        ),
        arrayOf(
            BiomeKeys.WINDSWEPT_HILLS,
            BiomeKeys.WINDSWEPT_HILLS,
            BiomeKeys.WINDSWEPT_HILLS,
            BiomeKeys.WINDSWEPT_FOREST,
            BiomeKeys.WINDSWEPT_FOREST
        ),
        arrayOf(null, null, null, null, null),
        arrayOf(null, null, null, null, null)
    )

    @JvmStatic
    fun addBiomes(consumer: BiConsumer<ParameterPoint, ResourceKey<Biome>>) {
        addOffCoastBiomes(consumer)
        addInlandBiomes(consumer)
        addUndergroundBiomes(consumer)
    }

    @JvmStatic
    private fun addOffCoastBiomes(consumer: BiConsumer<ParameterPoint, ResourceKey<Biome>>) {
        addSurfaceBiome(consumer, FULL_RANGE, FULL_RANGE, MUSHROOM_FIELDS_CONTINENTALNESS, FULL_RANGE, FULL_RANGE, BiomeKeys.MUSHROOM_FIELDS)
        for (i in TEMPERATURES.indices) {
            val temperature = TEMPERATURES[i]
            addSurfaceBiome(consumer, temperature, FULL_RANGE, DEEP_OCEAN_CONTINENTALNESS, FULL_RANGE, FULL_RANGE, OCEANS[0][i])
            addSurfaceBiome(consumer, temperature, FULL_RANGE, OCEAN_CONTINENTALNESS, FULL_RANGE, FULL_RANGE, OCEANS[1][i])
        }
    }

    @JvmStatic
    private fun addInlandBiomes(consumer: BiConsumer<ParameterPoint, ResourceKey<Biome>>) {
        addMidSlice(consumer, BOTTOM_MID_SLICE_WEIRDNESS)
        addHighSlice(consumer, BOTTOM_HIGH_SLICE_WEIRDNESS)
        addPeaks(consumer, BOTTOM_PEAKS_WEIRDNESS)
        addHighSlice(consumer, LOWER_HIGH_SLICE_WEIRDNESS)
        addMidSlice(consumer, LOWER_MID_SLICE_WEIRDNESS)
        addLowSlice(consumer, LOWER_LOW_SLICE_WEIRDNESS)
        addValleys(consumer, VALLEY_WEIRDNESS)
        addLowSlice(consumer, UPPER_LOW_SLICE_WEIRDNESS)
        addMidSlice(consumer, UPPER_MID_SLICE_WEIRDNESS)
        addHighSlice(consumer, UPPER_HIGH_SLICE_WEIRDNESS)
        addPeaks(consumer, TOP_PEAKS_WEIRDNESS)
        addHighSlice(consumer, TOP_HIGH_SLICE_WEIRDNESS)
        addMidSlice(consumer, TOP_MID_SLICE_WEIRDNESS)
    }

    @JvmStatic
    private fun addPeaks(consumer: BiConsumer<ParameterPoint, ResourceKey<Biome>>, weirdness: Parameter) {
        for (i in TEMPERATURES.indices) {
            val temperature = TEMPERATURES[i]
            for (j in HUMIDITIES.indices) {
                val humidity = HUMIDITIES[i]

                // Select all of the keys
                val middle = selectMiddle(i, j, weirdness)
                val middleBadlands = selectMiddleOrBadlands(i, j, weirdness)
                val middleBadlandsSlope = selectMiddleOrBadlandsOrSlope(i, j, weirdness)
                val plateau = selectPlateau(i, j, weirdness)
                val extremeHills = selectExtremeHills(i, j, weirdness)
                val shattered = selectShatteredBiomeOrDefault(i, j, weirdness, extremeHills)
                val peak = selectPeak(i, j, weirdness)

                // Add the biomes
                addSurfaceBiome(consumer, temperature, humidity, NEAR_FAR_CONTINENTALNESS, EROSIONS[0], weirdness, peak)
                addSurfaceBiome(consumer, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, EROSIONS[1], weirdness, middleBadlandsSlope)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, EROSIONS[1], weirdness, peak)
                addSurfaceBiome(consumer, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, EROSIONS[3], weirdness, middle)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, EROSIONS[2], weirdness, plateau)
                addSurfaceBiome(consumer, temperature, humidity, MID_INLAND_CONTINENTALNESS, EROSIONS[3], weirdness, middleBadlands)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, EROSIONS[3], weirdness, plateau)
                addSurfaceBiome(consumer, temperature, humidity, COAST_FAR_CONTINENTALNESS, EROSIONS[4], weirdness, middle)
                addSurfaceBiome(consumer, temperature, humidity, COAST_NEAR_CONTINENTALNESS, EROSIONS[5], weirdness, shattered)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, EROSIONS[5], weirdness, extremeHills)
                addSurfaceBiome(consumer, temperature, humidity, COAST_FAR_CONTINENTALNESS, EROSIONS[6], weirdness, middle)
            }
        }
    }

    @JvmStatic
    private fun addHighSlice(consumer: BiConsumer<ParameterPoint, ResourceKey<Biome>>, weirdness: Parameter) {
        for (i in TEMPERATURES.indices) {
            val temperature = TEMPERATURES[i]
            for (j in HUMIDITIES.indices) {
                val humidity = HUMIDITIES[j]

                // Select all of the keys
                val middle = selectMiddle(i, j, weirdness)
                val middleBadlands = selectMiddleOrBadlands(i, j, weirdness)
                val middleBadlandsSlope = selectMiddleOrBadlandsOrSlope(i, j, weirdness)
                val plateau = selectPlateau(i, j, weirdness)
                val extremeHills = selectExtremeHills(i, j, weirdness)
                val shattered = selectShatteredBiomeOrDefault(i, j, weirdness, middle)
                val slope = selectSlope(i, j, weirdness)
                val peak = selectPeak(i, j, weirdness)

                // Add the biomes
                addSurfaceBiome(consumer, temperature, humidity, COAST_CONTINENTALNESS, FIRST_SECOND_EROSION, weirdness, middle)
                addSurfaceBiome(consumer, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, EROSIONS[0], weirdness, slope)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, EROSIONS[0], weirdness, peak)
                addSurfaceBiome(consumer, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, EROSIONS[1], weirdness, middleBadlandsSlope)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, EROSIONS[1], weirdness, slope)
                addSurfaceBiome(consumer, temperature, humidity, COAST_NEAR_CONTINENTALNESS, THIRD_FOURTH_EROSION, weirdness, middle)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, EROSIONS[2], weirdness, plateau)
                addSurfaceBiome(consumer, temperature, humidity, MID_INLAND_CONTINENTALNESS, EROSIONS[3], weirdness, middleBadlands)
                addSurfaceBiome(consumer, temperature, humidity, FAR_INLAND_CONTINENTALNESS, EROSIONS[3], weirdness, plateau)
                addSurfaceBiome(consumer, temperature, humidity, COAST_FAR_CONTINENTALNESS, EROSIONS[4], weirdness, middle)
                addSurfaceBiome(consumer, temperature, humidity, COAST_NEAR_CONTINENTALNESS, EROSIONS[5], weirdness, shattered)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, EROSIONS[5], weirdness, extremeHills)
                addSurfaceBiome(consumer, temperature, humidity, COAST_FAR_CONTINENTALNESS, EROSIONS[6], weirdness, middle)
            }
        }
    }

    @JvmStatic
    private fun addMidSlice(consumer: BiConsumer<ParameterPoint, ResourceKey<Biome>>, weirdness: Parameter) {
        addCommonLowMidSlice(consumer, weirdness)

        for (i in TEMPERATURES.indices) {
            val temperature = TEMPERATURES[i]
            for (j in HUMIDITIES.indices) {
                val humidity = HUMIDITIES[j]

                // Select all of the keys
                val middle = selectMiddle(i, j, weirdness)
                val middleBadlands = selectMiddleOrBadlands(i, j, weirdness)
                val middleBadlandsSlope = selectMiddleOrBadlandsOrSlope(i, j, weirdness)
                val extremeHills = selectExtremeHills(i, j, weirdness)
                val plateau = selectPlateau(i, j, weirdness)
                val beach = selectBeach(i, j)
                val shattered = selectShatteredBiomeOrDefault(i, j, weirdness, middle)
                val shatteredCoast = selectShatteredCoast(i, j, weirdness)
                val slope = selectSlope(i, j, weirdness)

                // Add the biomes
                addSurfaceBiome(consumer, temperature, humidity, NEAR_FAR_CONTINENTALNESS, EROSIONS[0], weirdness, slope)
                addSurfaceBiome(consumer, temperature, humidity, NEAR_MID_CONTINENTALNESS, EROSIONS[1], weirdness, middleBadlandsSlope)
                addSurfaceBiome(consumer, temperature, humidity, FAR_INLAND_CONTINENTALNESS, EROSIONS[1], weirdness, if (i == 0) slope else plateau)
                addSurfaceBiome(consumer, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, EROSIONS[2], weirdness, middle)
                addSurfaceBiome(consumer, temperature, humidity, MID_INLAND_CONTINENTALNESS, EROSIONS[2], weirdness, middleBadlands)
                addSurfaceBiome(consumer, temperature, humidity, FAR_INLAND_CONTINENTALNESS, EROSIONS[2], weirdness, plateau)
                addSurfaceBiome(consumer, temperature, humidity, COAST_NEAR_CONTINENTALNESS, EROSIONS[3], weirdness, middle)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, EROSIONS[3], weirdness, middleBadlands)
                if (weirdness.maximum < 0L) {
                    addSurfaceBiome(consumer, temperature, humidity, COAST_CONTINENTALNESS, EROSIONS[4], weirdness, beach)
                    addSurfaceBiome(consumer, temperature, humidity, NEAR_FAR_CONTINENTALNESS, EROSIONS[4], weirdness, middle)
                } else {
                    addSurfaceBiome(consumer, temperature, humidity, COAST_FAR_CONTINENTALNESS, EROSIONS[4], weirdness, middle)
                }

                addSurfaceBiome(consumer, temperature, humidity, COAST_CONTINENTALNESS, EROSIONS[5], weirdness, shatteredCoast)
                addSurfaceBiome(consumer, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, EROSIONS[5], weirdness, shattered)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, EROSIONS[5], weirdness, extremeHills)
                val coastKey = if (weirdness.maximum < 0L) beach else middle
                addSurfaceBiome(consumer, temperature, humidity, COAST_CONTINENTALNESS, EROSIONS[6], weirdness, coastKey)
                if (i == 0) addSurfaceBiome(consumer, temperature, humidity, NEAR_FAR_CONTINENTALNESS, EROSIONS[6], weirdness, middle)
            }
        }
    }

    @JvmStatic
    private fun addLowSlice(consumer: BiConsumer<ParameterPoint, ResourceKey<Biome>>, weirdness: Parameter) {
        addCommonLowMidSlice(consumer, weirdness)

        for (i in TEMPERATURES.indices) {
            val temperature = TEMPERATURES[i]
            for (j in HUMIDITIES.indices) {
                val humidity = HUMIDITIES[i]

                // Select all of the keys
                val middle = selectMiddle(i, j, weirdness)
                val middleBadlands = selectMiddleOrBadlands(i, j, weirdness)
                val middleBadlandsSlope = selectMiddleOrBadlandsOrSlope(i, j, weirdness)
                val beach = selectBeach(i, j)
                val shattered = selectShatteredBiomeOrDefault(i, j, weirdness, middle)
                val shatteredCoast = selectShatteredCoast(i, j, weirdness)

                // Add the biomes
                addSurfaceBiome(consumer, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, FIRST_SECOND_EROSION, weirdness, middleBadlands)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, FIRST_SECOND_EROSION, weirdness, middleBadlandsSlope)
                addSurfaceBiome(consumer, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, THIRD_FOURTH_EROSION, weirdness, middle)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, THIRD_FOURTH_EROSION, weirdness, middleBadlands)
                addSurfaceBiome(consumer, temperature, humidity, COAST_CONTINENTALNESS, FOURTH_FIFTH_EROSION, weirdness, beach)
                addSurfaceBiome(consumer, temperature, humidity, NEAR_FAR_CONTINENTALNESS, EROSIONS[4], weirdness, middle)
                addSurfaceBiome(consumer, temperature, humidity, COAST_CONTINENTALNESS, EROSIONS[5], weirdness, shatteredCoast)
                addSurfaceBiome(consumer, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, EROSIONS[5], weirdness, shattered)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, EROSIONS[5], weirdness, middle)
                addSurfaceBiome(consumer, temperature, humidity, COAST_CONTINENTALNESS, EROSIONS[6], weirdness, beach)
                if (i == 0) addSurfaceBiome(consumer, temperature, humidity, NEAR_FAR_CONTINENTALNESS, EROSIONS[6], weirdness, middle)
            }
        }
    }

    @JvmStatic
    private fun addValleys(consumer: BiConsumer<ParameterPoint, ResourceKey<Biome>>, weirdness: Parameter) {
        val frozenShoreKey = if (weirdness.maximum < 0L) BiomeKeys.STONY_SHORE else BiomeKeys.FROZEN_RIVER
        addSurfaceBiome(consumer, FROZEN_RANGE, FULL_RANGE, COAST_CONTINENTALNESS, FIRST_SECOND_EROSION, weirdness, frozenShoreKey)
        val shoreKey = if (weirdness.maximum < 0L) BiomeKeys.STONY_SHORE else BiomeKeys.RIVER
        addSurfaceBiome(consumer, UNFROZEN_RANGE, FULL_RANGE, COAST_CONTINENTALNESS, FIRST_SECOND_EROSION, weirdness, shoreKey)
        addSurfaceBiome(consumer, FROZEN_RANGE, FULL_RANGE, NEAR_INLAND_CONTINENTALNESS, FIRST_SECOND_EROSION, weirdness, BiomeKeys.FROZEN_RIVER)
        addSurfaceBiome(consumer, UNFROZEN_RANGE, FULL_RANGE, NEAR_INLAND_CONTINENTALNESS, FIRST_SECOND_EROSION, weirdness, BiomeKeys.RIVER)
        addSurfaceBiome(consumer, FROZEN_RANGE, FULL_RANGE, COAST_FAR_CONTINENTALNESS, THIRD_SIXTH_EROSION, weirdness, BiomeKeys.FROZEN_RIVER)
        addSurfaceBiome(consumer, UNFROZEN_RANGE, FULL_RANGE, COAST_FAR_CONTINENTALNESS, THIRD_SIXTH_EROSION, weirdness, BiomeKeys.RIVER)
        addSurfaceBiome(consumer, FROZEN_RANGE, FULL_RANGE, COAST_CONTINENTALNESS, EROSIONS[6], weirdness, BiomeKeys.FROZEN_RIVER)
        addSurfaceBiome(consumer, UNFROZEN_RANGE, FULL_RANGE, COAST_CONTINENTALNESS, EROSIONS[6], weirdness, BiomeKeys.RIVER)
        addSurfaceBiome(consumer, UNFROZEN_RANGE, FULL_RANGE, INLAND_FAR_CONTINENTALNESS, EROSIONS[6], weirdness, BiomeKeys.SWAMP)
        addSurfaceBiome(consumer, FROZEN_RANGE, FULL_RANGE, INLAND_FAR_CONTINENTALNESS, EROSIONS[6], weirdness, BiomeKeys.FROZEN_RIVER)

        for (i in TEMPERATURES.indices) {
            val temperature = TEMPERATURES[i]
            for (j in HUMIDITIES.indices) {
                val humidity = HUMIDITIES[j]
                val middleBadlands = selectMiddleOrBadlands(i, j, weirdness)
                addSurfaceBiome(consumer, temperature, humidity, MID_FAR_CONTINENTALNESS, FIRST_SECOND_EROSION, weirdness, middleBadlands)
            }
        }
    }

    @JvmStatic
    private fun addUndergroundBiomes(consumer: BiConsumer<ParameterPoint, ResourceKey<Biome>>) {
        addUndergroundBiome(consumer, FULL_RANGE, FULL_RANGE, DRIPSTONE_CAVES_CONTINENTALNESS, FULL_RANGE, FULL_RANGE, BiomeKeys.DRIPSTONE_CAVES)
        addUndergroundBiome(consumer, FULL_RANGE, LUSH_CAVES_HUMIDITY, FULL_RANGE, FULL_RANGE, FULL_RANGE, BiomeKeys.LUSH_CAVES)
    }

    @JvmStatic
    private fun addCommonLowMidSlice(consumer: BiConsumer<ParameterPoint, ResourceKey<Biome>>, weirdness: Parameter) {
        addSurfaceBiome(consumer, FULL_RANGE, FULL_RANGE, COAST_CONTINENTALNESS, FIRST_THIRD_EROSION, weirdness, BiomeKeys.STONY_SHORE)
        addSurfaceBiome(consumer, UNFROZEN_RANGE, FULL_RANGE, NEAR_FAR_CONTINENTALNESS, EROSIONS[6], weirdness, BiomeKeys.SWAMP)
    }

    @JvmStatic
    private fun addSurfaceBiome(
        consumer: BiConsumer<ParameterPoint, ResourceKey<Biome>>,
        temperature: Parameter,
        humidity: Parameter,
        continentalness: Parameter,
        erosion: Parameter,
        weirdness: Parameter,
        key: ResourceKey<Biome>,
        offset: Float = 0F,
    ) {
        consumer.accept(
            ParameterPoint(temperature, humidity, continentalness, erosion, Parameter.ZERO, weirdness, offset.quantize()),
            key
        )
        consumer.accept(
            ParameterPoint(temperature, humidity, continentalness, erosion, Parameter.ONE, weirdness, offset.quantize()),
            key
        )
    }

    @JvmStatic
    private fun addUndergroundBiome(
        consumer: BiConsumer<ParameterPoint, ResourceKey<Biome>>,
        temperature: Parameter,
        humidity: Parameter,
        continentalness: Parameter,
        erosion: Parameter,
        weirdness: Parameter,
        key: ResourceKey<Biome>,
        offset: Float = 0F,
    ) {
        consumer.accept(ParameterPoint(temperature, humidity, continentalness, erosion, UNDERGROUND_DEPTH, weirdness, offset.quantize()), key)
    }

    @JvmStatic
    private fun selectMiddle(temperatureIndex: Int, humidityIndex: Int, weirdness: Parameter): ResourceKey<Biome> {
        val middle = MIDDLE_BIOMES[temperatureIndex][humidityIndex]
        if (weirdness.maximum < 0L) return middle
        return MIDDLE_BIOMES_VARIANT[temperatureIndex][humidityIndex] ?: middle
    }

    @JvmStatic
    private fun selectMiddleOrBadlands(temperatureIndex: Int, humidityIndex: Int, weirdness: Parameter): ResourceKey<Biome> {
        if (temperatureIndex == 4) return selectBadlands(humidityIndex, weirdness)
        return selectMiddle(temperatureIndex, humidityIndex, weirdness)
    }

    @JvmStatic
    private fun selectMiddleOrBadlandsOrSlope(temperatureIndex: Int, humidityIndex: Int, weirdness: Parameter): ResourceKey<Biome> {
        if (temperatureIndex == 0) return selectSlope(temperatureIndex, humidityIndex, weirdness)
        return selectMiddleOrBadlands(temperatureIndex, humidityIndex, weirdness)
    }

    @JvmStatic
    private fun selectShatteredBiomeOrDefault(
        temperatureIndex: Int,
        humidityIndex: Int,
        weirdness: Parameter,
        default: ResourceKey<Biome>
    ): ResourceKey<Biome> {
        if (temperatureIndex > 1 && humidityIndex < 4 && weirdness.maximum >= 0L) return BiomeKeys.WINDSWEPT_SAVANNA
        return default
    }

    @JvmStatic
    private fun selectShatteredCoast(temperatureIndex: Int, humidityIndex: Int, weirdness: Parameter): ResourceKey<Biome> {
        val default = if (weirdness.maximum >= 0L) {
            selectMiddle(temperatureIndex, humidityIndex, weirdness)
        } else {
            selectBeach(temperatureIndex, humidityIndex)
        }
        return selectShatteredBiomeOrDefault(temperatureIndex, humidityIndex, weirdness, default)
    }

    @JvmStatic
    private fun selectBeach(temperatureIndex: Int, humidityIndex: Int): ResourceKey<Biome> {
        if (temperatureIndex == 0) return BiomeKeys.SNOWY_BEACH
        if (temperatureIndex == 4) return BiomeKeys.DESERT
        return BiomeKeys.BEACH
    }

    @JvmStatic
    private fun selectBadlands(humidityIndex: Int, weirdness: Parameter): ResourceKey<Biome> {
        if (humidityIndex < 2) {
            if (weirdness.maximum < 0L) return BiomeKeys.ERODED_BADLANDS
            return BiomeKeys.BADLANDS
        }
        if (humidityIndex < 3) return BiomeKeys.BADLANDS
        return BiomeKeys.WOODED_BADLANDS
    }

    @JvmStatic
    private fun selectPlateau(temperatureIndex: Int, humidityIndex: Int, weirdness: Parameter): ResourceKey<Biome> {
        val plateau = PLATEAU_BIOMES[temperatureIndex][humidityIndex]
        if (weirdness.maximum < 0L) return plateau
        return PLATEAU_BIOMES_VARIANT[temperatureIndex][humidityIndex] ?: plateau
    }

    @JvmStatic
    private fun selectPeak(temperatureIndex: Int, humidityIndex: Int, weirdness: Parameter): ResourceKey<Biome> {
        if (temperatureIndex <= 2) {
            if (weirdness.maximum < 0L) return BiomeKeys.JAGGED_PEAKS
            return BiomeKeys.FROZEN_PEAKS
        }
        if (temperatureIndex == 3) return BiomeKeys.STONY_PEAKS
        return selectBadlands(humidityIndex, weirdness)
    }

    @JvmStatic
    private fun selectSlope(temperatureIndex: Int, humidityIndex: Int, weirdness: Parameter): ResourceKey<Biome> {
        if (temperatureIndex >= 3) return selectPlateau(temperatureIndex, humidityIndex, weirdness)
        if (humidityIndex <= 1) return BiomeKeys.SNOWY_SLOPES
        return BiomeKeys.GROVE
    }

    @JvmStatic
    private fun selectExtremeHills(
        temperatureIndex: Int,
        humidityIndex: Int,
        weirdness: Parameter
    ): ResourceKey<Biome> = EXTREME_HILLS[temperatureIndex][humidityIndex] ?: selectMiddle(temperatureIndex, humidityIndex, weirdness)
}

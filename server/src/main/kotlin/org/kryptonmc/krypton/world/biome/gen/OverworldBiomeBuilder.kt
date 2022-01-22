package org.kryptonmc.krypton.world.biome.gen

import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.Biomes
import java.util.function.BiConsumer

object OverworldBiomeBuilder {

    private val FULL_RANGE = Climate.Parameter.span(-1F, 1F)
    private val TEMPERATURES = arrayOf(
        Climate.Parameter.span(-1F, -0.45F),
        Climate.Parameter.span(-0.45F, -0.15F),
        Climate.Parameter.span(-0.15F, 0.2F),
        Climate.Parameter.span(0.2F, 0.55F),
        Climate.Parameter.span(0.55F, 1F)
    )
    private val HUMIDITIES = arrayOf(
        Climate.Parameter.span(-1F, -0.35F),
        Climate.Parameter.span(-0.35F, -0.1F),
        Climate.Parameter.span(-0.1F, 0.1F),
        Climate.Parameter.span(0.1F, 0.3F),
        Climate.Parameter.span(0.3F, 1F)
    )
    private val EROSIONS = arrayOf(
        Climate.Parameter.span(-1F, -0.78F),
        Climate.Parameter.span(-0.78F, -0.375F),
        Climate.Parameter.span(-0.375F, -0.2225F),
        Climate.Parameter.span(-0.2225F, 0.05F),
        Climate.Parameter.span(0.05F, 0.45F),
        Climate.Parameter.span(0.45F, 0.55F),
        Climate.Parameter.span(0.55F, 1F)
    )
    private val FROZEN_RANGE = TEMPERATURES[0]
    private val UNFROZEN_RANGE = Climate.Parameter.span(TEMPERATURES[1], TEMPERATURES[4])
    private val MUSHROOM_FIELDS_CONTINENTALNESS = Climate.Parameter.span(-1.2F, -1.05F)
    private val DEEP_OCEAN_CONTINENTALNESS = Climate.Parameter.span(-1.05F, -0.455F)
    private val OCEAN_CONTINENTALNESS = Climate.Parameter.span(-0.455F, -0.19F)
    private val COAST_CONTINENTALNESS = Climate.Parameter.span(-0.19F, -0.11F)
    private val INLAND_CONTINENTALNESS = Climate.Parameter.span(-0.11F, 0.55F)
    private val NEAR_INLAND_CONTINENTALNESS = Climate.Parameter.span(-0.11F, 0.03F)
    private val MID_INLAND_CONTINENTALNESS = Climate.Parameter.span(0.03F, 0.3F)
    private val FAR_INLAND_CONTINENTALNESS = Climate.Parameter.span(0.3F, 1F)
    private val OCEANS = arrayOf(
        arrayOf(Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.WARM_OCEAN),
        arrayOf(Biomes.FROZEN_OCEAN, Biomes.COLD_OCEAN, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN)
    )
    private val MIDDLE_BIOMES = arrayOf(
        arrayOf(Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.TAIGA),
        arrayOf(Biomes.PLAINS, Biomes.PLAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA),
        arrayOf(Biomes.FLOWER_FOREST, Biomes.PLAINS, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST),
        arrayOf(Biomes.SAVANNA, Biomes.SAVANNA, Biomes.FOREST, Biomes.JUNGLE, Biomes.JUNGLE),
        arrayOf(Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT)
    )
    private val MIDDLE_BIOMES_VARIANT: Array<Array<Biome?>> = arrayOf(
        arrayOf(Biomes.ICE_SPIKES, null, Biomes.SNOWY_TAIGA, null, null),
        arrayOf(null, null, null, null, Biomes.OLD_GROWTH_PINE_TAIGA),
        arrayOf(Biomes.SUNFLOWER_PLAINS, null, null, Biomes.OLD_GROWTH_BIRCH_FOREST, null),
        arrayOf(null, null, Biomes.PLAINS, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE),
        arrayOfNulls(5)
    )
    private val PLATEAU_BIOMES = arrayOf(
        arrayOf(Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA),
        arrayOf(Biomes.MEADOW, Biomes.MEADOW, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA),
        arrayOf(Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.DARK_FOREST),
        arrayOf(Biomes.SAVANNA_PLATEAU, Biomes.SAVANNA_PLATEAU, Biomes.FOREST, Biomes.FOREST, Biomes.JUNGLE),
        arrayOf(Biomes.BADLANDS, Biomes.BADLANDS, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.WOODED_BADLANDS)
    )
    private val PLATEAU_BIOMES_VARIANT: Array<Array<Biome?>> = arrayOf(
        arrayOf(Biomes.ICE_SPIKES, null, null, null, null),
        arrayOf(null, null, Biomes.MEADOW, Biomes.MEADOW, Biomes.OLD_GROWTH_PINE_TAIGA),
        arrayOf(null, null, Biomes.FOREST, Biomes.BIRCH_FOREST, null),
        arrayOfNulls(5),
        arrayOf(Biomes.ERODED_BADLANDS, Biomes.ERODED_BADLANDS, null, null, null)
    )
    private val EXTREME_HILLS: Array<Array<Biome?>> = arrayOf(
        arrayOf(
            Biomes.WINDSWEPT_GRAVELLY_HILLS,
            Biomes.WINDSWEPT_GRAVELLY_HILLS,
            Biomes.WINDSWEPT_HILLS,
            Biomes.WINDSWEPT_FOREST,
            Biomes.WINDSWEPT_FOREST
        ),
        arrayOf(
            Biomes.WINDSWEPT_GRAVELLY_HILLS,
            Biomes.WINDSWEPT_GRAVELLY_HILLS,
            Biomes.WINDSWEPT_HILLS,
            Biomes.WINDSWEPT_FOREST,
            Biomes.WINDSWEPT_FOREST
        ),
        arrayOf(
            Biomes.WINDSWEPT_HILLS,
            Biomes.WINDSWEPT_HILLS,
            Biomes.WINDSWEPT_HILLS,
            Biomes.WINDSWEPT_FOREST,
            Biomes.WINDSWEPT_FOREST
        ),
        arrayOfNulls(5),
        arrayOfNulls(5)
    )

    @JvmStatic
    fun addBiomes(consumer: BiConsumer<Climate.ParameterPoint, Biome>) {
        addOffCoastBiomes(consumer)
        addInlandBiomes(consumer)
        addUndergroundBiomes(consumer)
    }

    @JvmStatic
    private fun addOffCoastBiomes(consumer: BiConsumer<Climate.ParameterPoint, Biome>) {
        addSurfaceBiome(consumer, Biomes.MUSHROOM_FIELDS, FULL_RANGE, FULL_RANGE, MUSHROOM_FIELDS_CONTINENTALNESS, FULL_RANGE, FULL_RANGE, 0F)
        for (temperatureIndex in TEMPERATURES.indices) {
            val temperature = TEMPERATURES[temperatureIndex]
            addSurfaceBiome(consumer, OCEANS[0][temperatureIndex], temperature, FULL_RANGE, DEEP_OCEAN_CONTINENTALNESS, FULL_RANGE, FULL_RANGE, 0F)
            addSurfaceBiome(consumer, OCEANS[1][temperatureIndex], temperature, FULL_RANGE, OCEAN_CONTINENTALNESS, FULL_RANGE, FULL_RANGE, 0F)
        }
    }

    @JvmStatic
    private fun addInlandBiomes(consumer: BiConsumer<Climate.ParameterPoint, Biome>) {
        addMidSlice(consumer, Climate.Parameter.span(-1.0F, -0.93333334F))
        addHighSlice(consumer, Climate.Parameter.span(-0.93333334F, -0.7666667F))
        addPeaks(consumer, Climate.Parameter.span(-0.7666667F, -0.56666666F))
        addHighSlice(consumer, Climate.Parameter.span(-0.56666666F, -0.4F))
        addMidSlice(consumer, Climate.Parameter.span(-0.4F, -0.26666668F))
        addLowSlice(consumer, Climate.Parameter.span(-0.26666668F, -0.05F))
        addValleys(consumer, Climate.Parameter.span(-0.05F, 0.05F))
        addLowSlice(consumer, Climate.Parameter.span(0.05F, 0.26666668F))
        addMidSlice(consumer, Climate.Parameter.span(0.26666668F, 0.4F))
        addHighSlice(consumer, Climate.Parameter.span(0.4F, 0.56666666F))
        addPeaks(consumer, Climate.Parameter.span(0.56666666F, 0.7666667F))
        addHighSlice(consumer, Climate.Parameter.span(0.7666667F, 0.93333334F))
        addMidSlice(consumer, Climate.Parameter.span(0.93333334F, 1.0F))
    }

    @JvmStatic
    private fun addPeaks(consumer: BiConsumer<Climate.ParameterPoint, Biome>, weirdness: Climate.Parameter) {
        val coastFarContinentalness = Climate.Parameter.span(COAST_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS)
        val coastNearContinentalness = Climate.Parameter.span(COAST_CONTINENTALNESS, NEAR_INLAND_CONTINENTALNESS)
        val midFarContinentalness = Climate.Parameter.span(MID_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS)
        val middleErosion = Climate.Parameter.span(EROSIONS[2], EROSIONS[3])
        for (temperatureIndex in TEMPERATURES.indices) {
            val temperature = TEMPERATURES[temperatureIndex]
            for (humidityIndex in HUMIDITIES.indices) {
                val humidity = HUMIDITIES[humidityIndex]
                val middle = selectMiddle(temperatureIndex, humidityIndex, weirdness)
                val middleBadlands = selectMiddleOrBadlands(temperatureIndex, humidityIndex, weirdness)
                val middleBadlandsSlope = selectMiddleOrBadlandsOrSlope(temperatureIndex, humidityIndex, weirdness)
                val plateau = selectPlateau(temperatureIndex, humidityIndex, weirdness)
                val extremeHills = selectExtremeHills(temperatureIndex, humidityIndex, weirdness)
                val shattered = maybeSelectShattered(temperatureIndex, humidityIndex, weirdness, extremeHills)
                val peak = selectPeak(temperatureIndex, humidityIndex, weirdness)

                addSurfaceBiome(consumer, peak, temperature, humidity, coastFarContinentalness, EROSIONS[0], weirdness, 0F)
                addSurfaceBiome(consumer, middleBadlandsSlope, temperature, humidity, coastNearContinentalness, EROSIONS[1], weirdness, 0F)
                addSurfaceBiome(consumer, peak, temperature, humidity, midFarContinentalness, EROSIONS[1], weirdness, 0F)
                addSurfaceBiome(consumer, middle, temperature, humidity, coastNearContinentalness, middleErosion, weirdness, 0F)
                addSurfaceBiome(consumer, plateau, temperature, humidity, midFarContinentalness, EROSIONS[2], weirdness, 0F)
                addSurfaceBiome(consumer, middleBadlands, temperature, humidity, MID_INLAND_CONTINENTALNESS, EROSIONS[3], weirdness, 0F)
                addSurfaceBiome(consumer, plateau, temperature, humidity, FAR_INLAND_CONTINENTALNESS, EROSIONS[3], weirdness, 0F)
                addSurfaceBiome(consumer, middle, temperature, humidity, coastFarContinentalness, EROSIONS[4], weirdness, 0F)
                addSurfaceBiome(consumer, shattered, temperature, humidity, coastNearContinentalness, EROSIONS[5], weirdness, 0F)
                addSurfaceBiome(consumer, extremeHills, temperature, humidity, midFarContinentalness, EROSIONS[5], weirdness, 0F)
                addSurfaceBiome(consumer, middle, temperature, humidity, coastFarContinentalness, EROSIONS[6], weirdness, 0F)
            }
        }
    }

    @JvmStatic
    private fun addHighSlice(consumer: BiConsumer<Climate.ParameterPoint, Biome>, weirdness: Climate.Parameter) {
        val middleErosion = Climate.Parameter.span(EROSIONS[0], EROSIONS[1])
        val midFarContinentalness = Climate.Parameter.span(MID_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS)
        val coastFarContinentalness = Climate.Parameter.span(COAST_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS)
        val otherMiddleErosion = Climate.Parameter.span(EROSIONS[2], EROSIONS[3])
        val coastNearContinentalness = Climate.Parameter.span(COAST_CONTINENTALNESS, NEAR_INLAND_CONTINENTALNESS)
        for (temperatureIndex in TEMPERATURES.indices) {
            val temperature = TEMPERATURES[temperatureIndex]
            for (humidityIndex in HUMIDITIES.indices) {
                val humidity = HUMIDITIES[humidityIndex]
                val middle = selectMiddle(temperatureIndex, humidityIndex, weirdness)
                val middleBadlands = selectMiddleOrBadlands(temperatureIndex, humidityIndex, weirdness)
                val middleBadlandsSlope = selectMiddleOrBadlandsOrSlope(temperatureIndex, humidityIndex, weirdness)
                val plateau = selectPlateau(temperatureIndex, humidityIndex, weirdness)
                val extremeHills = selectExtremeHills(temperatureIndex, humidityIndex, weirdness)
                val shattered = maybeSelectShattered(temperatureIndex, humidityIndex, weirdness, middle)
                val slope = selectSlope(temperatureIndex, humidityIndex, weirdness)
                val peak = selectPeak(temperatureIndex, humidityIndex, weirdness)

                addSurfaceBiome(consumer, middle, temperature, humidity, COAST_CONTINENTALNESS, middleErosion, weirdness, 0F)
                addSurfaceBiome(consumer, slope, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, EROSIONS[0], weirdness, 0F)
                addSurfaceBiome(consumer, peak, temperature, humidity, midFarContinentalness, EROSIONS[1], weirdness, 0F)
                addSurfaceBiome(consumer, middleBadlandsSlope, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, EROSIONS[1], weirdness, 0F)
                addSurfaceBiome(consumer, slope, temperature, humidity, midFarContinentalness, EROSIONS[1], weirdness, 0F)
                addSurfaceBiome(consumer, middle, temperature, humidity, coastFarContinentalness, otherMiddleErosion, weirdness, 0F)
                addSurfaceBiome(consumer, plateau, temperature, humidity, midFarContinentalness, EROSIONS[2], weirdness, 0F)
                addSurfaceBiome(consumer, middleBadlands, temperature, humidity, MID_INLAND_CONTINENTALNESS, EROSIONS[3], weirdness, 0F)
                addSurfaceBiome(consumer, plateau, temperature, humidity, FAR_INLAND_CONTINENTALNESS, EROSIONS[3], weirdness, 0F)
                addSurfaceBiome(consumer, middle, temperature, humidity, coastFarContinentalness, EROSIONS[4], weirdness, 0F)
                addSurfaceBiome(consumer, shattered, temperature, humidity, coastNearContinentalness, EROSIONS[5], weirdness, 0F)
                addSurfaceBiome(consumer, extremeHills, temperature, humidity, midFarContinentalness, EROSIONS[5], weirdness, 0F)
                addSurfaceBiome(consumer, middle, temperature, humidity, coastFarContinentalness, EROSIONS[6], weirdness, 0F)
            }
        }
    }

    @JvmStatic
    private fun addMidSlice(consumer: BiConsumer<Climate.ParameterPoint, Biome>, weirdness: Climate.Parameter) {
        val stonyErosion = Climate.Parameter.span(EROSIONS[0], EROSIONS[2])
        addSurfaceBiome(consumer, Biomes.STONY_SHORE, FULL_RANGE, FULL_RANGE, COAST_CONTINENTALNESS, stonyErosion, weirdness, 0F)
        val nearFarContinentalness = Climate.Parameter.span(NEAR_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS)
        addSurfaceBiome(consumer, Biomes.SWAMP, UNFROZEN_RANGE, FULL_RANGE, nearFarContinentalness, EROSIONS[6], weirdness, 0F)

        val nearMidContinentalness = Climate.Parameter.span(NEAR_INLAND_CONTINENTALNESS, MID_INLAND_CONTINENTALNESS)
        val coastNearContinentalness = Climate.Parameter.span(COAST_CONTINENTALNESS, NEAR_INLAND_CONTINENTALNESS)
        val midFarContinentalness = Climate.Parameter.span(MID_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS)
        val coastFarContinentalness = Climate.Parameter.span(COAST_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS)
        for (temperatureIndex in TEMPERATURES.indices) {
            val temperature = TEMPERATURES[temperatureIndex]
            for (humidityIndex in HUMIDITIES.indices) {
                val humidity = HUMIDITIES[humidityIndex]
                val middle = selectMiddle(temperatureIndex, humidityIndex, weirdness)
                val middleBadlands = selectMiddleOrBadlands(temperatureIndex, humidityIndex, weirdness)
                val middleBadlandsSlope = selectMiddleOrBadlandsOrSlope(temperatureIndex, humidityIndex, weirdness)
                val extremeHills = selectExtremeHills(temperatureIndex, humidityIndex, weirdness)
                val plateau = selectPlateau(temperatureIndex, humidityIndex, weirdness)
                val beach = selectBeach(temperatureIndex, humidityIndex)
                val shattered = maybeSelectShattered(temperatureIndex, humidityIndex, weirdness, middle)
                val shatteredCoast = selectShatteredCoast(temperatureIndex, humidityIndex, weirdness)
                val slope = selectSlope(temperatureIndex, humidityIndex, weirdness)
                val slopeOrPlateau = if (temperatureIndex == 0) slope else plateau

                addSurfaceBiome(consumer, slope, temperature, humidity, nearFarContinentalness, EROSIONS[0], weirdness, 0F)
                addSurfaceBiome(consumer, middleBadlandsSlope, temperature, humidity, nearMidContinentalness, EROSIONS[1], weirdness, 0F)
                addSurfaceBiome(consumer, slopeOrPlateau, temperature, humidity, FAR_INLAND_CONTINENTALNESS, EROSIONS[1], weirdness, 0F)
                addSurfaceBiome(consumer, middle, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, EROSIONS[2], weirdness, 0F)
                addSurfaceBiome(consumer, middleBadlands, temperature, humidity, MID_INLAND_CONTINENTALNESS, EROSIONS[2], weirdness, 0F)
                addSurfaceBiome(consumer, plateau, temperature, humidity, FAR_INLAND_CONTINENTALNESS, EROSIONS[2], weirdness, 0F)
                addSurfaceBiome(consumer, middle, temperature, humidity, coastNearContinentalness, EROSIONS[3], weirdness, 0F)
                addSurfaceBiome(consumer, middleBadlands, temperature, humidity, midFarContinentalness, EROSIONS[3], weirdness, 0F)
                if (weirdness.maximum < 0L) {
                    addSurfaceBiome(consumer, beach, temperature, humidity, COAST_CONTINENTALNESS, EROSIONS[4], weirdness, 0F)
                    addSurfaceBiome(consumer, middle, temperature, humidity, nearFarContinentalness, EROSIONS[4], weirdness, 0F)
                } else {
                    addSurfaceBiome(consumer, middle, temperature, humidity, coastFarContinentalness, EROSIONS[4], weirdness, 0F)
                }
                addSurfaceBiome(consumer, shatteredCoast, temperature, humidity, COAST_CONTINENTALNESS, EROSIONS[5], weirdness, 0F)
                addSurfaceBiome(consumer, shattered, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, EROSIONS[5], weirdness, 0F)
                addSurfaceBiome(consumer, extremeHills, temperature, humidity, midFarContinentalness, EROSIONS[5], weirdness, 0F)
                val beachOrMiddle = if (weirdness.maximum < 0L) beach else middle
                addSurfaceBiome(consumer, beachOrMiddle, temperature, humidity, COAST_CONTINENTALNESS, EROSIONS[6], weirdness, 0F)
                if (temperatureIndex == 0) {
                    addSurfaceBiome(consumer, middle, temperature, humidity, nearFarContinentalness, EROSIONS[6], weirdness, 0F)
                }
            }
        }
    }

    @JvmStatic
    private fun addLowSlice(consumer: BiConsumer<Climate.ParameterPoint, Biome>, weirdness: Climate.Parameter) {
        val stonyShoreErosion = Climate.Parameter.span(EROSIONS[0], EROSIONS[2])
        addSurfaceBiome(consumer, Biomes.STONY_SHORE, FULL_RANGE, FULL_RANGE, COAST_CONTINENTALNESS, stonyShoreErosion, weirdness, 0F)
        val nearFarContinentalness = Climate.Parameter.span(NEAR_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS)
        addSurfaceBiome(consumer, Biomes.SWAMP, UNFROZEN_RANGE, FULL_RANGE, nearFarContinentalness, EROSIONS[6], weirdness, 0F)

        val middleBadlandsErosion = Climate.Parameter.span(EROSIONS[0], EROSIONS[1])
        val midFarContinentalness = Climate.Parameter.span(MID_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS)
        val middleErosion = Climate.Parameter.span(EROSIONS[2], EROSIONS[3])
        val beachErosion = Climate.Parameter.span(EROSIONS[3], EROSIONS[4])
        for (temperatureIndex in TEMPERATURES.indices) {
            val temperature = TEMPERATURES[temperatureIndex]
            for (humidityIndex in HUMIDITIES.indices) {
                val humidity = HUMIDITIES[humidityIndex]
                val middle = selectMiddle(temperatureIndex, humidityIndex, weirdness)
                val middleBadlands = selectMiddleOrBadlands(temperatureIndex, humidityIndex, weirdness)
                val middleBadlandsSlope = selectMiddleOrBadlandsOrSlope(temperatureIndex, humidityIndex, weirdness)
                val beach = selectBeach(temperatureIndex, humidityIndex)
                val shattered = maybeSelectShattered(temperatureIndex, humidityIndex, weirdness, middle)
                val shatteredCoast = selectShatteredCoast(temperatureIndex, humidityIndex, weirdness)

                addSurfaceBiome(consumer, middleBadlands, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, middleBadlandsErosion, weirdness, 0F)
                addSurfaceBiome(consumer, middleBadlandsSlope, temperature, humidity, midFarContinentalness, middleBadlandsErosion, weirdness, 0F)
                addSurfaceBiome(consumer, middle, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, middleErosion, weirdness, 0F)
                addSurfaceBiome(consumer, middleBadlands, temperature, humidity, midFarContinentalness, middleErosion, weirdness, 0F)
                addSurfaceBiome(consumer, beach, temperature, humidity, COAST_CONTINENTALNESS, beachErosion, weirdness, 0F)
                addSurfaceBiome(consumer, middle, temperature, humidity, nearFarContinentalness, EROSIONS[4], weirdness, 0F)
                addSurfaceBiome(consumer, shatteredCoast, temperature, humidity, COAST_CONTINENTALNESS, EROSIONS[5], weirdness, 0F)
                addSurfaceBiome(consumer, shattered, temperature, humidity, NEAR_INLAND_CONTINENTALNESS, EROSIONS[5], weirdness, 0F)
                addSurfaceBiome(consumer, middle, temperature, humidity, midFarContinentalness, EROSIONS[5], weirdness, 0F)
                addSurfaceBiome(consumer, beach, temperature, humidity, COAST_CONTINENTALNESS, EROSIONS[6], weirdness, 0F)
                if (temperatureIndex == 0) {
                    addSurfaceBiome(consumer, middle, temperature, humidity, nearFarContinentalness, EROSIONS[6], weirdness, 0F)
                }
            }
        }
    }

    @JvmStatic
    private fun addValleys(consumer: BiConsumer<Climate.ParameterPoint, Biome>, weirdness: Climate.Parameter) {
        val stonyShoreFrozenRiver = if (weirdness.maximum < 0L) Biomes.STONY_SHORE else Biomes.FROZEN_RIVER
        val stonyErosion = Climate.Parameter.span(EROSIONS[0], EROSIONS[1])
        addSurfaceBiome(consumer, stonyShoreFrozenRiver, FROZEN_RANGE, FULL_RANGE, COAST_CONTINENTALNESS, stonyErosion, weirdness, 0F)
        val stonyShoreRiver = if (weirdness.maximum < 0L) Biomes.STONY_SHORE else Biomes.RIVER
        addSurfaceBiome(consumer, stonyShoreRiver, UNFROZEN_RANGE, FULL_RANGE, COAST_CONTINENTALNESS, stonyErosion, weirdness, 0F)
        addSurfaceBiome(consumer, Biomes.FROZEN_RIVER, FROZEN_RANGE, FULL_RANGE, NEAR_INLAND_CONTINENTALNESS, stonyErosion, weirdness, 0F)
        addSurfaceBiome(consumer, Biomes.RIVER, UNFROZEN_RANGE, FULL_RANGE, NEAR_INLAND_CONTINENTALNESS, stonyErosion, weirdness, 0F)
        val coastFarContinentalness = Climate.Parameter.span(COAST_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS)
        val frozenErosion = Climate.Parameter.span(EROSIONS[2], EROSIONS[5])
        addSurfaceBiome(consumer, Biomes.FROZEN_RIVER, FROZEN_RANGE, FULL_RANGE, coastFarContinentalness, frozenErosion, weirdness, 0F)
        addSurfaceBiome(consumer, Biomes.RIVER, UNFROZEN_RANGE, FULL_RANGE, coastFarContinentalness, frozenErosion, weirdness, 0F)
        addSurfaceBiome(consumer, Biomes.FROZEN_RIVER, FROZEN_RANGE, FULL_RANGE, COAST_CONTINENTALNESS, EROSIONS[6], weirdness, 0F)
        addSurfaceBiome(consumer, Biomes.RIVER, UNFROZEN_RANGE, FULL_RANGE, COAST_CONTINENTALNESS, EROSIONS[6], weirdness, 0F)
        val inlandFarContinentalness = Climate.Parameter.span(INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS)
        addSurfaceBiome(consumer, Biomes.SWAMP, UNFROZEN_RANGE, FULL_RANGE, inlandFarContinentalness, EROSIONS[6], weirdness, 0F)
        addSurfaceBiome(consumer, Biomes.FROZEN_RIVER, FROZEN_RANGE, FULL_RANGE, inlandFarContinentalness, EROSIONS[6], weirdness, 0F)

        for (temperatureIndex in TEMPERATURES.indices) {
            val temperature = TEMPERATURES[temperatureIndex]
            for (humidityIndex in HUMIDITIES.indices) {
                val humidity = HUMIDITIES[humidityIndex]
                val biome = selectMiddleOrBadlands(temperatureIndex, humidityIndex, weirdness)
                val continentalness = Climate.Parameter.span(MID_INLAND_CONTINENTALNESS, FAR_INLAND_CONTINENTALNESS)
                addSurfaceBiome(consumer, biome, temperature, humidity, continentalness, stonyErosion, weirdness, 0F)
            }
        }
    }

    @JvmStatic
    private fun addUndergroundBiomes(consumer: BiConsumer<Climate.ParameterPoint, Biome>) {
        addUndergroundBiome(consumer, Biomes.DRIPSTONE_CAVES, FULL_RANGE, FULL_RANGE, Climate.Parameter.span(0.8F, 1F), FULL_RANGE, FULL_RANGE, 0F)
        addUndergroundBiome(consumer, Biomes.LUSH_CAVES, FULL_RANGE, Climate.Parameter.span(0.7F, 1F), FULL_RANGE, FULL_RANGE, FULL_RANGE, 0F)
    }

    @JvmStatic
    private fun selectMiddle(temperatureIndex: Int, humidityIndex: Int, weirdness: Climate.Parameter): Biome {
        if (weirdness.maximum < 0L) return MIDDLE_BIOMES[temperatureIndex][humidityIndex]
        return MIDDLE_BIOMES_VARIANT[temperatureIndex][humidityIndex] ?: MIDDLE_BIOMES[temperatureIndex][humidityIndex]
    }

    @JvmStatic
    private fun selectMiddleOrBadlands(temperatureIndex: Int, humidityIndex: Int, weirdness: Climate.Parameter): Biome {
        if (temperatureIndex == 4) return selectBadlands(humidityIndex, weirdness)
        return selectMiddle(temperatureIndex, humidityIndex, weirdness)
    }

    @JvmStatic
    private fun selectMiddleOrBadlandsOrSlope(temperatureIndex: Int, humidityIndex: Int, weirdness: Climate.Parameter): Biome {
        if (temperatureIndex == 0) return selectSlope(temperatureIndex, humidityIndex, weirdness)
        return selectMiddleOrBadlands(temperatureIndex, humidityIndex, weirdness)
    }

    @JvmStatic
    private fun maybeSelectShattered(temperatureIndex: Int, humidityIndex: Int, weirdness: Climate.Parameter, biome: Biome): Biome {
        if (temperatureIndex > 1 && humidityIndex < 4 && weirdness.maximum >= 0L) return Biomes.WINDSWEPT_SAVANNA
        return biome
    }

    @JvmStatic
    private fun selectShatteredCoast(temperatureIndex: Int, humidityIndex: Int, weirdness: Climate.Parameter): Biome {
        val biome = if (weirdness.maximum >= 0L) {
            selectMiddle(temperatureIndex, humidityIndex, weirdness)
        } else {
            selectBeach(temperatureIndex, humidityIndex)
        }
        return maybeSelectShattered(temperatureIndex, humidityIndex, weirdness, biome)
    }

    @JvmStatic
    private fun selectBeach(temperatureIndex: Int, humidityIndex: Int): Biome {
        if (temperatureIndex == 0) return Biomes.SNOWY_BEACH
        if (temperatureIndex == 4) return Biomes.DESERT
        return Biomes.BEACH
    }

    @JvmStatic
    private fun selectBadlands(humidityIndex: Int, weirdness: Climate.Parameter): Biome {
        if (humidityIndex < 2) {
            if (weirdness.maximum < 0L) return Biomes.ERODED_BADLANDS
            return Biomes.BADLANDS
        }
        if (humidityIndex < 3) return Biomes.BADLANDS
        return Biomes.WOODED_BADLANDS
    }

    @JvmStatic
    private fun selectPlateau(temperatureIndex: Int, humidityIndex: Int, weirdness: Climate.Parameter): Biome {
        if (weirdness.maximum < 0L) return PLATEAU_BIOMES[temperatureIndex][humidityIndex]
        return PLATEAU_BIOMES_VARIANT[temperatureIndex][humidityIndex] ?: PLATEAU_BIOMES[temperatureIndex][humidityIndex]
    }

    @JvmStatic
    private fun selectPeak(temperatureIndex: Int, humidityIndex: Int, weirdness: Climate.Parameter): Biome {
        if (temperatureIndex <= 2) {
            if (weirdness.maximum < 0L) return Biomes.JAGGED_PEAKS
            return Biomes.FROZEN_PEAKS
        }
        if (temperatureIndex == 3) return Biomes.STONY_PEAKS
        return selectBadlands(humidityIndex, weirdness)
    }

    @JvmStatic
    private fun selectSlope(temperatureIndex: Int, humidityIndex: Int, weirdness: Climate.Parameter): Biome {
        if (temperatureIndex >= 3) return selectPlateau(temperatureIndex, humidityIndex, weirdness)
        if (humidityIndex <= 1) return Biomes.SNOWY_SLOPES
        return Biomes.GROVE
    }

    @JvmStatic
    private fun selectExtremeHills(
        temperatureIndex: Int,
        humidityIndex: Int,
        weirdness: Climate.Parameter
    ): Biome = EXTREME_HILLS[temperatureIndex][humidityIndex] ?: selectMiddle(temperatureIndex, humidityIndex, weirdness)

    @JvmStatic
    private fun addSurfaceBiome(
        consumer: BiConsumer<Climate.ParameterPoint, Biome>,
        biome: Biome,
        temperature: Climate.Parameter,
        humidity: Climate.Parameter,
        continentalness: Climate.Parameter,
        erosion: Climate.Parameter,
        weirdness: Climate.Parameter,
        offset: Float
    ) {
        consumer.accept(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(0F), weirdness, offset), biome)
        consumer.accept(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(1F), weirdness, offset), biome)
    }

    @JvmStatic
    private fun addUndergroundBiome(
        consumer: BiConsumer<Climate.ParameterPoint, Biome>,
        biome: Biome,
        temperature: Climate.Parameter,
        humidity: Climate.Parameter,
        continentalness: Climate.Parameter,
        erosion: Climate.Parameter,
        weirdness: Climate.Parameter,
        offset: Float
    ) {
        val depth = Climate.Parameter.span(0.2F, 0.9F)
        consumer.accept(Climate.parameters(temperature, humidity, continentalness, erosion, depth, weirdness, offset), biome)
    }
}

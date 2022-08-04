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

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.util.Color
import org.kryptonmc.api.world.biome.Climate
import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.api.world.biome.GrassColorModifier
import org.kryptonmc.api.world.biome.TemperatureModifier
import org.kryptonmc.krypton.effect.KryptonMusic
import org.kryptonmc.krypton.util.clamp

object OverworldBiomes {

    // Standard colours
    @JvmField
    val OVERWORLD_WATER: Color = Color.of(4159204)
    @JvmField
    val OVERWORLD_WATER_FOG: Color = Color.of(329011)

    private val OVERWORLD_FOG = Color.of(12638463)
    private val COLD_WATER = Color.of(3750089)
    private val LUKEWARM_WATER = Color.of(4566514)
    private val LUKEWARM_WATER_FOG = Color.of(267827)
    private val WARM_WATER = Color.of(4445678)
    private val WARM_WATER_FOG = Color.of(270131)
    private val BADLANDS_FOLIAGE = Color.of(10387789)
    private val BADLANDS_GRASS = Color.of(9470285)
    private val SNOWY_WATER = Color.of(4020182)
    private val SWAMP_WATER = Color.of(6388580)
    private val SWAMP_WATER_FOG = Color.of(2302743)
    private val SWAMP_FOLIAGE = Color.of(6975545)
    private val MEADOW_WATER = Color.of(937679)
    private val MANGROVE_SWAMP_WATER = Color.of(3832426)
    private val MANGROVE_SWAMP_WATER_FOG = Color.of(5077600)
    private val MANGROVE_SWAMP_FOLIAGE = Color.of(9285927)

    // ==============================
    // None
    // ==============================

    @JvmStatic
    fun theVoid(): KryptonBiome = createBiome(Precipitation.NONE, 0.5F, 0.5F)

    // ==============================
    // Taiga
    // ==============================

    @JvmStatic
    fun oldGrowthPineTaiga(): KryptonBiome = oldGrowthTaiga(false)

    @JvmStatic
    fun oldGrowthSpruceTaiga(): KryptonBiome = oldGrowthTaiga(true)

    @JvmStatic
    private fun oldGrowthTaiga(spruce: Boolean): KryptonBiome =
        createBiome(Precipitation.RAIN, if (spruce) 0.25F else 0.3F, 0.8F, music = SoundEvents.MUSIC_BIOME_OLD_GROWTH_TAIGA)

    @JvmStatic
    fun taiga(): KryptonBiome = taiga(false)

    @JvmStatic
    fun snowyTaiga(): KryptonBiome = taiga(true)

    @JvmStatic
    private fun taiga(snowy: Boolean): KryptonBiome {
        val precipitation = if (snowy) Precipitation.SNOW else Precipitation.RAIN
        val water = if (snowy) SNOWY_WATER else OVERWORLD_WATER
        return createBiome(precipitation, if (snowy) -0.5F else 0.25F, if (snowy) 0.4F else 0.8F, water)
    }

    // ==============================
    // Extreme Hills
    // ==============================

    @JvmStatic
    fun windsweptHills(): KryptonBiome = windsweptHills(false)

    @JvmStatic
    fun windsweptForest(): KryptonBiome = windsweptHills(true)

    @JvmStatic
    private fun windsweptHills(isEdge: Boolean): KryptonBiome = createBiome(Precipitation.RAIN, 0.2F, 0.3F)

    // ==============================
    // Jungle
    // ==============================

    @JvmStatic
    fun sparseJungle(): KryptonBiome = baseJungle(0.8F, false, true, false)

    @JvmStatic
    fun jungle(): KryptonBiome = baseJungle(0.9F, false, false, true)

    @JvmStatic
    fun bambooJungle(): KryptonBiome = baseJungle(0.9F, true, false, true)

    @JvmStatic
    private fun baseJungle(downfall: Float, isBamboo: Boolean, isEdge: Boolean, isLight: Boolean): KryptonBiome =
        createBiome(Precipitation.RAIN, 0.95F, downfall, music = SoundEvents.MUSIC_BIOME_JUNGLE_AND_FOREST)

    // ==============================
    // Mesa
    // ==============================

    @JvmStatic
    fun badlands(): KryptonBiome = badlands(false)

    @JvmStatic
    fun woodedBadlands(): KryptonBiome = badlands(true)

    @JvmStatic
    private fun badlands(wooded: Boolean): KryptonBiome = biome {
        val temperature = 2F
        climate(Climate.of(Precipitation.NONE, temperature, 0F, TemperatureModifier.NONE))
        effects {
            waterColor(OVERWORLD_WATER)
            waterFogColor(OVERWORLD_WATER_FOG)
            fogColor(OVERWORLD_FOG)
            skyColor(calculateSkyColor(temperature))
            foliageColor(BADLANDS_FOLIAGE)
            grassColor(BADLANDS_GRASS)
            mood(KryptonAmbientMoodSettings.CAVE)
        }
    }

    // ==============================
    // Plains/Icy
    // ==============================

    @JvmStatic
    fun plains(): KryptonBiome = plains(false, false, false)

    @JvmStatic
    fun sunflowerPlains(): KryptonBiome = plains(true, false, false)

    @JvmStatic
    fun snowyPlains(): KryptonBiome = plains(false, true, false)

    @JvmStatic
    fun iceSpikes(): KryptonBiome = plains(false, true, true)

    @JvmStatic
    private fun plains(sunflowers: Boolean, icy: Boolean, spikes: Boolean): KryptonBiome =
        createBiome(if (icy) Precipitation.SNOW else Precipitation.RAIN, if (icy) 0F else 0.8F, if (icy) 0F else 0.4F)

    // ==============================
    // Savanna
    // ==============================

    @JvmStatic
    fun savanna(): KryptonBiome = savanna(false, false)

    @JvmStatic
    fun savannaPlateau(): KryptonBiome = savanna(false, true)

    @JvmStatic
    fun windsweptSavanna(): KryptonBiome = savanna(true, false)

    @JvmStatic
    private fun savanna(shattered: Boolean, plateau: Boolean): KryptonBiome {
        val temperature = when {
            shattered -> 1.1F
            plateau -> 1F
            else -> 1.2F
        }
        return createBiome(Precipitation.NONE, temperature, 0F)
    }

    // ==============================
    // Beach
    // ==============================

    @JvmStatic
    fun beach(): KryptonBiome = beach(false, false)

    @JvmStatic
    fun snowyBeach(): KryptonBiome = beach(true, false)

    @JvmStatic
    fun stonyShore(): KryptonBiome = beach(false, true)

    @JvmStatic
    private fun beach(snowy: Boolean, stony: Boolean): KryptonBiome {
        val normal = !snowy && !stony
        val temperature = when {
            snowy -> 0.05F
            stony -> 0.2F
            else -> 0.8F
        }
        val precipitation = if (snowy) Precipitation.SNOW else Precipitation.RAIN
        val water = if (snowy) SNOWY_WATER else OVERWORLD_WATER
        return createBiome(precipitation, temperature, if (normal) 0.4F else 0.3F, water)
    }

    // ==============================
    // Forest
    // ==============================

    @JvmStatic
    fun forest(): KryptonBiome = forest(false, false, false)

    @JvmStatic
    fun flowerForest(): KryptonBiome = forest(false, false, true)

    @JvmStatic
    fun birchForest(): KryptonBiome = forest(true, false, false)

    @JvmStatic
    fun oldGrowthBirchForest(): KryptonBiome = forest(true, true, false)

    @JvmStatic
    private fun forest(birch: Boolean, tall: Boolean, flower: Boolean): KryptonBiome {
        val music = SoundEvents.MUSIC_BIOME_JUNGLE_AND_FOREST
        return createBiome(Precipitation.RAIN, if (birch) 0.6F else 0.7F, if (birch) 0.6F else 0.8F, music = music)
    }

    @JvmStatic
    fun darkForest(): KryptonBiome = biome {
        val temperature = 0.7F
        climate(Climate.of(Precipitation.RAIN, temperature, 0.8F, TemperatureModifier.NONE))
        effects {
            waterColor(OVERWORLD_WATER)
            waterFogColor(OVERWORLD_WATER_FOG)
            fogColor(OVERWORLD_FOG)
            skyColor(calculateSkyColor(temperature))
            grassColorModifier(GrassColorModifier.DARK_FOREST)
            mood(KryptonAmbientMoodSettings.CAVE)
            backgroundMusic(KryptonMusic.game(SoundEvents.MUSIC_BIOME_JUNGLE_AND_FOREST))
        }
    }

    // ==============================
    // Ocean
    // ==============================

    @JvmStatic
    fun coldOcean(): KryptonBiome = coldOcean(false)

    @JvmStatic
    fun deepColdOcean(): KryptonBiome = coldOcean(true)

    @JvmStatic
    fun ocean(): KryptonBiome = ocean(false)

    @JvmStatic
    fun deepOcean(): KryptonBiome = ocean(true)

    @JvmStatic
    fun lukewarmOcean(): KryptonBiome = lukewarmOcean(false)

    @JvmStatic
    fun deepLukewarmOcean(): KryptonBiome = lukewarmOcean(true)

    @JvmStatic
    fun warmOcean(): KryptonBiome = baseOcean(WARM_WATER, WARM_WATER_FOG)

    @JvmStatic
    fun frozenOcean(): KryptonBiome = frozenOcean(false)

    @JvmStatic
    fun deepFrozenOcean(): KryptonBiome = frozenOcean(true)

    @JvmStatic
    private fun coldOcean(deep: Boolean): KryptonBiome = baseOcean(COLD_WATER, OVERWORLD_WATER_FOG)

    @JvmStatic
    private fun ocean(deep: Boolean): KryptonBiome = baseOcean(OVERWORLD_WATER, OVERWORLD_WATER_FOG)

    @JvmStatic
    private fun lukewarmOcean(deep: Boolean): KryptonBiome = baseOcean(LUKEWARM_WATER, LUKEWARM_WATER_FOG)

    @JvmStatic
    private fun frozenOcean(deep: Boolean): KryptonBiome {
        val temperature = if (deep) 0.5F else 0F
        return biome {
            climate(Climate.of(if (deep) Precipitation.RAIN else Precipitation.SNOW, temperature, 0.5F, TemperatureModifier.FROZEN))
            effects {
                waterColor(COLD_WATER)
                waterFogColor(OVERWORLD_WATER_FOG)
                fogColor(OVERWORLD_FOG)
                skyColor(calculateSkyColor(temperature))
                mood(KryptonAmbientMoodSettings.CAVE)
            }
        }
    }

    @JvmStatic
    private fun baseOcean(waterColor: Color, waterFogColor: Color): KryptonBiome =
        createBiome(Precipitation.RAIN, 0.5F, 0.5F, waterColor, waterFogColor)

    // ==============================
    // Desert
    // ==============================

    @JvmStatic
    fun desert(): KryptonBiome = createBiome(Precipitation.NONE, 2F, 0F)

    // ==============================
    // River
    // ==============================

    @JvmStatic
    fun river(): KryptonBiome = river(false)

    @JvmStatic
    fun frozenRiver(): KryptonBiome = river(true)

    @JvmStatic
    private fun river(frozen: Boolean): KryptonBiome {
        val precipitation = if (frozen) Precipitation.SNOW else Precipitation.RAIN
        return createBiome(precipitation, if (frozen) 0F else 0.5F, 0.5F, if (frozen) COLD_WATER else OVERWORLD_WATER)
    }

    // ==============================
    // Swamp
    // ==============================

    @JvmStatic
    fun swamp(): KryptonBiome = baseSwamp(SWAMP_WATER, SWAMP_WATER_FOG, SWAMP_FOLIAGE)

    @JvmStatic
    fun mangroveSwamp(): KryptonBiome = baseSwamp(MANGROVE_SWAMP_WATER, MANGROVE_SWAMP_WATER_FOG, MANGROVE_SWAMP_FOLIAGE)

    @JvmStatic
    private fun baseSwamp(waterColor: Color, waterFogColor: Color, foliageColor: Color): KryptonBiome = biome {
        val temperature = 0.8F
        climate(Climate.of(Precipitation.RAIN, temperature, 0.9F, TemperatureModifier.NONE))
        effects {
            waterColor(waterColor)
            waterFogColor(waterFogColor)
            fogColor(OVERWORLD_FOG)
            skyColor(calculateSkyColor(temperature))
            foliageColor(foliageColor)
            grassColorModifier(GrassColorModifier.SWAMP)
            mood(KryptonAmbientMoodSettings.CAVE)
            backgroundMusic(KryptonMusic.game(SoundEvents.MUSIC_BIOME_SWAMP))
        }
    }

    // ==============================
    // Mushroom
    // ==============================

    @JvmStatic
    fun mushroomFields(): KryptonBiome = createBiome(Precipitation.RAIN, 0.9F, 1F)

    // ==============================
    // Underground
    // ==============================

    @JvmStatic
    fun lushCaves(): KryptonBiome = createBiome(Precipitation.RAIN, 0.5F, 0.5F, music = SoundEvents.MUSIC_BIOME_LUSH_CAVES)

    @JvmStatic
    fun dripstoneCaves(): KryptonBiome = createBiome(Precipitation.RAIN, 0.8F, 0.4F, music = SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES)

    // ==============================
    // Mountain
    // ==============================

    @JvmStatic
    fun meadow(): KryptonBiome = createBiome(Precipitation.RAIN, 0.5F, 0.8F, MEADOW_WATER, OVERWORLD_WATER_FOG, SoundEvents.MUSIC_BIOME_MEADOW)

    @JvmStatic
    fun frozenPeaks(): KryptonBiome = createBiome(Precipitation.RAIN, -0.7F, 0.9F, music = SoundEvents.MUSIC_BIOME_FROZEN_PEAKS)

    @JvmStatic
    fun jaggedPeaks(): KryptonBiome = createBiome(Precipitation.SNOW, -0.7F, 0.9F, music = SoundEvents.MUSIC_BIOME_JAGGED_PEAKS)

    @JvmStatic
    fun stonyPeaks(): KryptonBiome = createBiome(Precipitation.RAIN, 1F, 0.3F, music = SoundEvents.MUSIC_BIOME_STONY_PEAKS)

    @JvmStatic
    fun snowySlopes(): KryptonBiome = createBiome(Precipitation.SNOW, -0.3F, 0.9F, music = SoundEvents.MUSIC_BIOME_SNOWY_SLOPES)

    @JvmStatic
    fun grove(): KryptonBiome = createBiome(Precipitation.SNOW, -0.2F, 0.8F, music = SoundEvents.MUSIC_BIOME_GROVE)

    @JvmStatic
    fun deepDark(): KryptonBiome = createBiome(Precipitation.RAIN, 0.8F, 0.4F, music = SoundEvents.MUSIC_BIOME_DEEP_DARK)

    // ******************************
    // Helpers
    // ******************************

    @JvmStatic
    private fun createBiome(
        precipitation: Precipitation,
        temperature: Float,
        downfall: Float,
        waterColor: Color = OVERWORLD_WATER,
        waterFogColor: Color = OVERWORLD_WATER_FOG,
        music: SoundEvent? = null
    ): KryptonBiome = biome {
        climate {
            precipitation(precipitation)
            temperature(temperature)
            downfall(downfall)
        }
        effects {
            waterColor(waterColor)
            waterFogColor(waterFogColor)
            fogColor(OVERWORLD_FOG)
            skyColor(calculateSkyColor(temperature))
            mood(KryptonAmbientMoodSettings.CAVE)
            if (music != null) backgroundMusic(KryptonMusic.game(music))
        }
    }

    @JvmStatic
    fun calculateSkyColor(temperature: Float): Color {
        val value = (temperature / 3F).clamp(-1F, 1F)
        return Color.of(0.62222224F - value * 0.05F, 0.5F + value * 0.1F, 1F)
    }

    @JvmStatic
    private inline fun biome(builder: KryptonBiome.Builder.() -> Unit): KryptonBiome = KryptonBiome.Builder().apply(builder).build()
}

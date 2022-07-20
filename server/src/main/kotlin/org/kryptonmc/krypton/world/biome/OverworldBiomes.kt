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

import net.kyori.adventure.key.Key
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
    fun theVoid(key: Key): KryptonBiome = createBiome(key, Precipitation.NONE, 0.5F, 0.5F)

    // ==============================
    // Taiga
    // ==============================

    @JvmStatic
    fun oldGrowthPineTaiga(key: Key): KryptonBiome = oldGrowthTaiga(key, false)

    @JvmStatic
    fun oldGrowthSpruceTaiga(key: Key): KryptonBiome = oldGrowthTaiga(key, true)

    @JvmStatic
    private fun oldGrowthTaiga(key: Key, spruce: Boolean): KryptonBiome =
        createBiome(key, Precipitation.RAIN, if (spruce) 0.25F else 0.3F, 0.8F, music = SoundEvents.MUSIC_BIOME_OLD_GROWTH_TAIGA)

    @JvmStatic
    fun taiga(key: Key): KryptonBiome = taiga(key, false)

    @JvmStatic
    fun snowyTaiga(key: Key): KryptonBiome = taiga(key, true)

    @JvmStatic
    private fun taiga(key: Key, snowy: Boolean): KryptonBiome {
        val precipitation = if (snowy) Precipitation.SNOW else Precipitation.RAIN
        val water = if (snowy) SNOWY_WATER else OVERWORLD_WATER
        return createBiome(key, precipitation, if (snowy) -0.5F else 0.25F, if (snowy) 0.4F else 0.8F, water)
    }

    // ==============================
    // Extreme Hills
    // ==============================

    @JvmStatic
    fun windsweptHills(key: Key): KryptonBiome = windsweptHills(key, false)

    @JvmStatic
    fun windsweptForest(key: Key): KryptonBiome = windsweptHills(key, true)

    @JvmStatic
    private fun windsweptHills(key: Key, isEdge: Boolean): KryptonBiome = createBiome(key, Precipitation.RAIN, 0.2F, 0.3F)

    // ==============================
    // Jungle
    // ==============================

    @JvmStatic
    fun sparseJungle(key: Key): KryptonBiome = baseJungle(key, 0.8F, false, true, false)

    @JvmStatic
    fun jungle(key: Key): KryptonBiome = baseJungle(key, 0.9F, false, false, true)

    @JvmStatic
    fun bambooJungle(key: Key): KryptonBiome = baseJungle(key, 0.9F, true, false, true)

    @JvmStatic
    private fun baseJungle(key: Key, downfall: Float, isBamboo: Boolean, isEdge: Boolean, isLight: Boolean): KryptonBiome =
        createBiome(key, Precipitation.RAIN, 0.95F, downfall, music = SoundEvents.MUSIC_BIOME_JUNGLE_AND_FOREST)

    // ==============================
    // Mesa
    // ==============================

    @JvmStatic
    fun badlands(key: Key): KryptonBiome = badlands(key, false)

    @JvmStatic
    fun woodedBadlands(key: Key): KryptonBiome = badlands(key, true)

    @JvmStatic
    private fun badlands(key: Key, wooded: Boolean): KryptonBiome = biome(key) {
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
    fun plains(key: Key): KryptonBiome = plains(key, false, false, false)

    @JvmStatic
    fun sunflowerPlains(key: Key): KryptonBiome = plains(key, true, false, false)

    @JvmStatic
    fun snowyPlains(key: Key): KryptonBiome = plains(key, false, true, false)

    @JvmStatic
    fun iceSpikes(key: Key): KryptonBiome = plains(key, false, true, true)

    @JvmStatic
    private fun plains(key: Key, sunflowers: Boolean, icy: Boolean, spikes: Boolean): KryptonBiome =
        createBiome(key, if (icy) Precipitation.SNOW else Precipitation.RAIN, if (icy) 0F else 0.8F, if (icy) 0F else 0.4F)

    // ==============================
    // Savanna
    // ==============================

    @JvmStatic
    fun savanna(key: Key): KryptonBiome = savanna(key, false, false)

    @JvmStatic
    fun savannaPlateau(key: Key): KryptonBiome = savanna(key, false, true)

    @JvmStatic
    fun windsweptSavanna(key: Key): KryptonBiome = savanna(key, true, false)

    @JvmStatic
    private fun savanna(key: Key, shattered: Boolean, plateau: Boolean): KryptonBiome {
        val temperature = when {
            shattered -> 1.1F
            plateau -> 1F
            else -> 1.2F
        }
        return createBiome(key, Precipitation.NONE, temperature, 0F)
    }

    // ==============================
    // Beach
    // ==============================

    @JvmStatic
    fun beach(key: Key): KryptonBiome = beach(key, false, false)

    @JvmStatic
    fun snowyBeach(key: Key): KryptonBiome = beach(key, true, false)

    @JvmStatic
    fun stonyShore(key: Key): KryptonBiome = beach(key, false, true)

    @JvmStatic
    private fun beach(key: Key, snowy: Boolean, stony: Boolean): KryptonBiome {
        val normal = !snowy && !stony
        val temperature = when {
            snowy -> 0.05F
            stony -> 0.2F
            else -> 0.8F
        }
        val precipitation = if (snowy) Precipitation.SNOW else Precipitation.RAIN
        val water = if (snowy) SNOWY_WATER else OVERWORLD_WATER
        return createBiome(key, precipitation, temperature, if (normal) 0.4F else 0.3F, water)
    }

    // ==============================
    // Forest
    // ==============================

    @JvmStatic
    fun forest(key: Key): KryptonBiome = forest(key, false, false, false)

    @JvmStatic
    fun flowerForest(key: Key): KryptonBiome = forest(key, false, false, true)

    @JvmStatic
    fun birchForest(key: Key): KryptonBiome = forest(key, true, false, false)

    @JvmStatic
    fun oldGrowthBirchForest(key: Key): KryptonBiome = forest(key, true, true, false)

    @JvmStatic
    private fun forest(key: Key, birch: Boolean, tall: Boolean, flower: Boolean): KryptonBiome {
        val music = SoundEvents.MUSIC_BIOME_JUNGLE_AND_FOREST
        return createBiome(key, Precipitation.RAIN, if (birch) 0.6F else 0.7F, if (birch) 0.6F else 0.8F, music = music)
    }

    @JvmStatic
    fun darkForest(key: Key): KryptonBiome = biome(key) {
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
    fun coldOcean(key: Key): KryptonBiome = coldOcean(key, false)

    @JvmStatic
    fun deepColdOcean(key: Key): KryptonBiome = coldOcean(key, true)

    @JvmStatic
    fun ocean(key: Key): KryptonBiome = ocean(key, false)

    @JvmStatic
    fun deepOcean(key: Key): KryptonBiome = ocean(key, true)

    @JvmStatic
    fun lukewarmOcean(key: Key): KryptonBiome = lukewarmOcean(key, false)

    @JvmStatic
    fun deepLukewarmOcean(key: Key): KryptonBiome = lukewarmOcean(key, true)

    @JvmStatic
    fun warmOcean(key: Key): KryptonBiome = baseOcean(key, WARM_WATER, WARM_WATER_FOG)

    @JvmStatic
    fun frozenOcean(key: Key): KryptonBiome = frozenOcean(key, false)

    @JvmStatic
    fun deepFrozenOcean(key: Key): KryptonBiome = frozenOcean(key, true)

    @JvmStatic
    private fun coldOcean(key: Key, deep: Boolean): KryptonBiome = baseOcean(key, COLD_WATER, OVERWORLD_WATER_FOG)

    @JvmStatic
    private fun ocean(key: Key, deep: Boolean): KryptonBiome = baseOcean(key, OVERWORLD_WATER, OVERWORLD_WATER_FOG)

    @JvmStatic
    private fun lukewarmOcean(key: Key, deep: Boolean): KryptonBiome = baseOcean(key, LUKEWARM_WATER, LUKEWARM_WATER_FOG)

    @JvmStatic
    private fun frozenOcean(key: Key, deep: Boolean): KryptonBiome {
        val temperature = if (deep) 0.5F else 0F
        return biome(key) {
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
    private fun baseOcean(key: Key, waterColor: Color, waterFogColor: Color): KryptonBiome =
        createBiome(key, Precipitation.RAIN, 0.5F, 0.5F, waterColor, waterFogColor)

    // ==============================
    // Desert
    // ==============================

    @JvmStatic
    fun desert(key: Key): KryptonBiome = createBiome(key, Precipitation.NONE, 2F, 0F)

    // ==============================
    // River
    // ==============================

    @JvmStatic
    fun river(key: Key): KryptonBiome = river(key, false)

    @JvmStatic
    fun frozenRiver(key: Key): KryptonBiome = river(key, true)

    @JvmStatic
    private fun river(key: Key, frozen: Boolean): KryptonBiome {
        val precipitation = if (frozen) Precipitation.SNOW else Precipitation.RAIN
        return createBiome(key, precipitation, if (frozen) 0F else 0.5F, 0.5F, if (frozen) COLD_WATER else OVERWORLD_WATER)
    }

    // ==============================
    // Swamp
    // ==============================

    @JvmStatic
    fun swamp(key: Key): KryptonBiome = baseSwamp(key, SWAMP_WATER, SWAMP_WATER_FOG, SWAMP_FOLIAGE)

    @JvmStatic
    fun mangroveSwamp(key: Key): KryptonBiome = baseSwamp(key, MANGROVE_SWAMP_WATER, MANGROVE_SWAMP_WATER_FOG, MANGROVE_SWAMP_FOLIAGE)

    @JvmStatic
    private fun baseSwamp(key: Key, waterColor: Color, waterFogColor: Color, foliageColor: Color): KryptonBiome = biome(key) {
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
    fun mushroomFields(key: Key): KryptonBiome = createBiome(key, Precipitation.RAIN, 0.9F, 1F)

    // ==============================
    // Underground
    // ==============================

    @JvmStatic
    fun lushCaves(key: Key): KryptonBiome = createBiome(key, Precipitation.RAIN, 0.5F, 0.5F, music = SoundEvents.MUSIC_BIOME_LUSH_CAVES)

    @JvmStatic
    fun dripstoneCaves(key: Key): KryptonBiome = createBiome(key, Precipitation.RAIN, 0.8F, 0.4F, music = SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES)

    // ==============================
    // Mountain
    // ==============================

    @JvmStatic
    fun meadow(key: Key): KryptonBiome =
        createBiome(key, Precipitation.RAIN, 0.5F, 0.8F, MEADOW_WATER, OVERWORLD_WATER_FOG, SoundEvents.MUSIC_BIOME_MEADOW)

    @JvmStatic
    fun frozenPeaks(key: Key): KryptonBiome = createBiome(key, Precipitation.RAIN, -0.7F, 0.9F, music = SoundEvents.MUSIC_BIOME_FROZEN_PEAKS)

    @JvmStatic
    fun jaggedPeaks(key: Key): KryptonBiome = createBiome(key, Precipitation.SNOW, -0.7F, 0.9F, music = SoundEvents.MUSIC_BIOME_JAGGED_PEAKS)

    @JvmStatic
    fun stonyPeaks(key: Key): KryptonBiome = createBiome(key, Precipitation.RAIN, 1F, 0.3F, music = SoundEvents.MUSIC_BIOME_STONY_PEAKS)

    @JvmStatic
    fun snowySlopes(key: Key): KryptonBiome = createBiome(key, Precipitation.SNOW, -0.3F, 0.9F, music = SoundEvents.MUSIC_BIOME_SNOWY_SLOPES)

    @JvmStatic
    fun grove(key: Key): KryptonBiome = createBiome(key, Precipitation.SNOW, -0.2F, 0.8F, music = SoundEvents.MUSIC_BIOME_GROVE)

    @JvmStatic
    fun deepDark(key: Key): KryptonBiome = createBiome(key, Precipitation.RAIN, 0.8F, 0.4F, music = SoundEvents.MUSIC_BIOME_DEEP_DARK)

    // ******************************
    // Helpers
    // ******************************

    @JvmStatic
    private fun createBiome(
        key: Key,
        precipitation: Precipitation,
        temperature: Float,
        downfall: Float,
        waterColor: Color = OVERWORLD_WATER,
        waterFogColor: Color = OVERWORLD_WATER_FOG,
        music: SoundEvent? = null
    ): KryptonBiome = biome(key) {
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
    private inline fun biome(key: Key, builder: KryptonBiome.Builder.() -> Unit): KryptonBiome = KryptonBiome.Builder(key).apply(builder).build()
}

/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.api.effect.particle.ParticleTypes
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.world.biome.BiomeCategories
import org.kryptonmc.api.world.biome.GrassColorModifiers
import org.kryptonmc.api.world.biome.Precipitations
import org.kryptonmc.api.world.biome.TemperatureModifiers
import org.kryptonmc.krypton.effect.KryptonMusic
import org.kryptonmc.krypton.effect.KryptonMusics
import org.kryptonmc.krypton.util.clamp
import java.awt.Color

object BuiltInBiomes {

    fun ocean(key: Key, isDeep: Boolean) = ocean(key, isDeep, 4159204, 329011)

    fun coldOcean(key: Key, isDeep: Boolean) = ocean(key, isDeep, 4020182, 329011)

    fun lukewarmOcean(key: Key, isDeep: Boolean) = ocean(key, isDeep, 4566514, 267827)

    fun warmOcean(key: Key, isDeep: Boolean) = ocean(key, isDeep, 4445678, 270131)

    fun frozenOcean(key: Key, isDeep: Boolean) = KryptonBiome(
        key,
        KryptonClimate(
            if (isDeep) Precipitations.RAIN else Precipitations.SNOW,
            if (isDeep) 0.5F else 0F, 0.5F, TemperatureModifiers.FROZEN
        ),
        if (isDeep) -1.8F else -1F,
        0.1F,
        BiomeCategories.OCEAN,
        KryptonBiomeEffects(
            Color(12638463),
            Color(3750089),
            Color(329011),
            (if (isDeep) 0.5F else 0F).calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    private fun ocean(key: Key, isDeep: Boolean, waterColor: Int, waterFogColor: Int) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.RAIN, 0.5F, 0.5F),
        if (isDeep) -1.8F else -1F,
        0.1F,
        BiomeCategories.OCEAN,
        KryptonBiomeEffects(
            Color(12638463),
            Color(waterColor),
            Color(waterFogColor),
            0.5F.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun desert(key: Key, depth: Float, scale: Float) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.NONE, 2F, 0F),
        depth,
        scale,
        BiomeCategories.DESERT,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            2F.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun mountain(key: Key, depth: Float, scale: Float) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.RAIN, 0.2F, 0.3F),
        depth,
        scale,
        BiomeCategories.EXTREME_HILLS,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.2F.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun forest(key: Key, depth: Float, scale: Float) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.RAIN, 0.7F, 0.8F),
        depth,
        scale,
        BiomeCategories.FOREST,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.7F.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun birchForest(key: Key, depth: Float, scale: Float) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.RAIN, 0.6F, 0.6F),
        depth,
        scale,
        BiomeCategories.FOREST,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.6F.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun flowerForest(key: Key) = forest(key, 0.1F, 0.4F)

    fun darkForest(key: Key, depth: Float, scale: Float) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.RAIN, 0.7F, 0.8F),
        depth,
        scale,
        BiomeCategories.FOREST,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.7F.calculateSkyColor(),
            GrassColorModifiers.DARK_FOREST,
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun taiga(key: Key, depth: Float, scale: Float, isSnowy: Boolean) = KryptonBiome(
        key,
        KryptonClimate(
            if (isSnowy) Precipitations.SNOW else Precipitations.RAIN,
            if (isSnowy) -0.5F else -0.25F, if (isSnowy) 0.4F else 0.8F
        ),
        depth,
        scale,
        BiomeCategories.TAIGA,
        KryptonBiomeEffects(
            Color(12638463),
            Color(if (isSnowy) 4020182 else 4159204),
            Color(329011),
            (if (isSnowy) -0.5F else -0.25F).calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun giantTreeTaiga(key: Key, depth: Float, scale: Float, temperature: Float) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.RAIN, temperature, 0.8F),
        depth,
        scale,
        BiomeCategories.TAIGA,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            temperature.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun swamp(key: Key, depth: Float, scale: Float) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.RAIN, 0.8F, 0.9F),
        depth,
        scale,
        BiomeCategories.SWAMP,
        KryptonBiomeEffects(
            Color(12638463),
            Color(6388580),
            Color(2302743),
            0.8F.calculateSkyColor(),
            GrassColorModifiers.SWAMP,
            Color(6975545),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun tundra(key: Key, depth: Float, scale: Float) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.SNOW, 0F, 0.5F),
        depth,
        scale,
        BiomeCategories.ICY,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0F.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun river(key: Key, depth: Float, scale: Float, temperature: Float, waterColor: Int, isSnowy: Boolean) = KryptonBiome(
        key,
        KryptonClimate(if (isSnowy) Precipitations.SNOW else Precipitations.RAIN, temperature, 0.5F),
        depth,
        scale,
        BiomeCategories.RIVER,
        KryptonBiomeEffects(
            Color(12638463),
            Color(waterColor),
            Color(329011),
            temperature.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun beach(
        key: Key,
        depth: Float,
        scale: Float,
        temperature: Float,
        downfall: Float,
        waterColor: Int,
        isSnowy: Boolean,
        isShore: Boolean
    ) = KryptonBiome(
        key,
        KryptonClimate(if (isSnowy) Precipitations.SNOW else Precipitations.RAIN, temperature, downfall),
        depth,
        scale,
        if (isShore) BiomeCategories.NONE else BiomeCategories.BEACH,
        KryptonBiomeEffects(
            Color(12638463),
            Color(waterColor),
            Color(329011),
            temperature.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun jungle(key: Key) = jungle(key, 0.1F, 0.2F)

    fun jungleEdge(key: Key) = jungle(key, 0.1F, 0.2F, 0.8F)

    fun modifiedJungle(key: Key) = jungle(key, 0.2F, 0.4F, 0.9F)

    fun modifiedJungleEdge(key: Key) = jungle(key, 0.2F, 0.4F, 0.8F)

    fun jungleHills(key: Key) = jungle(key, 0.45F, 0.3F)

    fun bambooJungle(key: Key) = jungle(key, 0.1F, 0.2F)

    fun bambooJungleHills(key: Key) = jungle(key, 0.45F, 0.3F)

    private fun jungle(key: Key, depth: Float, scale: Float) = jungle(key, depth, scale, 0.9F)

    private fun jungle(key: Key, depth: Float, scale: Float, downfall: Float) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.RAIN, 0.95F, downfall),
        depth,
        scale,
        BiomeCategories.JUNGLE,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.95F.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun plains(key: Key) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.RAIN, 0.8F, 0.4F),
        0.125F,
        0.05F,
        BiomeCategories.PLAINS,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.8F.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun end(key: Key) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.NONE, 0.5F, 0.5F),
        0.1F,
        0.2F,
        BiomeCategories.THE_END,
        KryptonBiomeEffects(
            Color(10518688),
            Color(4159204),
            Color(329011),
            Color(0),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun mushroomFields(key: Key, depth: Float, scale: Float) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.RAIN, 0.9F, 1F),
        depth,
        scale,
        BiomeCategories.MUSHROOM,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.9F.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun savanna(key: Key, depth: Float, scale: Float, temperature: Float) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.NONE, temperature, 0F),
        depth,
        scale,
        BiomeCategories.SAVANNA,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            temperature.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun savannaPlateau(key: Key) = savanna(key, 1.5F, 0.025F, 1F)

    fun badlands(key: Key, depth: Float, scale: Float) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.NONE, 2F, 0F),
        depth,
        scale,
        BiomeCategories.MESA,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            2F.calculateSkyColor(),
            foliageColor = Color(10387789),
            grassColor = Color(9470285),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun erodedBadlands(key: Key) = badlands(key, 0.1F, 0.2F)

    fun void(key: Key) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.NONE, 0.5F, 0.5F),
        0.1F,
        0.2F,
        BiomeCategories.NONE,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.5F.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    fun netherWastes(key: Key) = nether(
        key,
        3344392,
        329011,
        SoundEvents.AMBIENT_NETHER_WASTES_LOOP,
        SoundEvents.AMBIENT_NETHER_WASTES_MOOD,
        SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS,
        KryptonMusics.NETHER_WASTES
    )

    fun soulSandValley(key: Key) = nether(
        key,
        1787717,
        329011,
        SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP,
        SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD,
        SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS,
        KryptonMusics.SOUL_SAND_VALLEY,
        KryptonAmbientParticleSettings(ParticleTypes.ASH, null, 0.00625F)
    )

    fun basaltDeltas(key: Key) = nether(
        key,
        6840176,
        4341314,
        SoundEvents.AMBIENT_BASALT_DELTAS_LOOP,
        SoundEvents.AMBIENT_BASALT_DELTAS_MOOD,
        SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS,
        KryptonMusics.BASALT_DELTAS,
        KryptonAmbientParticleSettings(ParticleTypes.WHITE_ASH, null, 0.118093334F)
    )

    fun crimsonForest(key: Key) = nether(
        key,
        3343107,
        329011,
        SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP,
        SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD,
        SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS,
        KryptonMusics.CRIMSON_FOREST,
        KryptonAmbientParticleSettings(ParticleTypes.CRIMSON_SPORE, null, 0.025F)
    )

    fun warpedForest(key: Key) = nether(
        key,
        1705242,
        329011,
        SoundEvents.AMBIENT_WARPED_FOREST_LOOP,
        SoundEvents.AMBIENT_WARPED_FOREST_MOOD,
        SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS,
        KryptonMusics.WARPED_FOREST,
        KryptonAmbientParticleSettings(ParticleTypes.WARPED_SPORE, null, 0.01428F)
    )

    private fun nether(
        key: Key,
        fogColor: Int,
        waterFogColor: Int,
        loop: SoundEvent,
        mood: SoundEvent,
        additions: SoundEvent,
        music: KryptonMusic,
        particles: KryptonAmbientParticleSettings? = null
    ) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.NONE, 2F, 0F),
        0.1F,
        0.2F,
        BiomeCategories.NETHER,
        KryptonBiomeEffects(
            Color(fogColor),
            Color(4159204),
            Color(waterFogColor),
            2F.calculateSkyColor(),
            ambientParticleSettings = particles,
            ambientLoopSound = loop,
            ambientMoodSettings = KryptonAmbientMoodSettings(mood, 6000, 8, 2.0),
            ambientAdditionsSettings = KryptonAmbientAdditionsSettings(additions, 0.0111),
            backgroundMusic = music
        )
    )

    fun lushCaves(key: Key) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.RAIN, 0.5F, 0.5F),
        0.1F,
        0.2F,
        BiomeCategories.UNDERGROUND,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.5F.calculateSkyColor()
        )
    )

    fun dripstoneCaves(key: Key) = KryptonBiome(
        key,
        KryptonClimate(Precipitations.RAIN, 0.8F, 0.4F),
        0.125F,
        0.05F,
        BiomeCategories.UNDERGROUND,
        KryptonBiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.8F.calculateSkyColor(),
            ambientMoodSettings = KryptonAmbientMoodSettings.CAVE
        )
    )

    private fun Float.calculateSkyColor(): Color {
        val temp = (this / 3F).clamp(-1F, 1F)
        return Color.getHSBColor(0.62222224F - temp * 0.05F, 0.5F + temp * 0.1F, 1F)
    }
}

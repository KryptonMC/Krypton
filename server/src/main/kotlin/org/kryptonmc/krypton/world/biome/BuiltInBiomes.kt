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

import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.krypton.effect.Music
import org.kryptonmc.krypton.effect.Musics
import org.kryptonmc.krypton.entity.MobCategory
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.world.biome.MobSpawnSettings.SpawnerData
import java.awt.Color
import java.util.Optional

object BuiltInBiomes {

    fun ocean(isDeep: Boolean) = ocean(isDeep, 4159204, 329011, MobSpawnSettings.Builder()
        .ocean(1, 4, 10)
        .spawn(MobCategory.WATER_CREATURE, SpawnerData(EntityTypes.DOLPHIN, 1, 1, 2))
    )

    fun coldOcean(isDeep: Boolean) = ocean(isDeep, 4020182, 329011, MobSpawnSettings.Builder()
        .ocean(3, 4, 15)
        .spawn(MobCategory.WATER_AMBIENT, SpawnerData(EntityTypes.SALMON, 15, 1, 5))
    )

    fun lukewarmOcean(isDeep: Boolean) = ocean(isDeep, 4566514, 267827, MobSpawnSettings.Builder()
        .ocean(if (isDeep) 8 else 10, if (isDeep) 4 else 2, if (isDeep) 8 else 15)
        .spawn(MobCategory.WATER_AMBIENT, SpawnerData(EntityTypes.PUFFERFISH, 5, 1, 3))
        .spawn(MobCategory.WATER_AMBIENT, SpawnerData(EntityTypes.TROPICAL_FISH, 25, 8, 8))
        .spawn(MobCategory.WATER_CREATURE, SpawnerData(EntityTypes.DOLPHIN, 2, 1, 2))
    )

    fun warmOcean() = ocean(false, 4445678, 270131, MobSpawnSettings.Builder()
        .spawn(MobCategory.WATER_AMBIENT, SpawnerData(EntityTypes.PUFFERFISH, 15, 1, 3))
        .warmOcean(10, 4)
    )

    fun deepWarmOcean() = ocean(false, 4445678, 270131, MobSpawnSettings.Builder()
        .warmOcean(5, 1)
        .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.DROWNED, 5, 1, 1))
    )

    fun frozenOcean(isDeep: Boolean) = KryptonBiome(
        ClimateSettings(if (isDeep) Precipitation.RAIN else Precipitation.SNOW, if (isDeep) 0.5F else 0F, 0.5F, TemperatureModifier.FROZEN),
        if (isDeep) -1.8F else -1F,
        0.1F,
        BiomeCategory.OCEAN,
        BiomeEffects(
            Color(12638463),
            Color(3750089),
            Color(329011),
            (if (isDeep) 0.5F else 0F).calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        MobSpawnSettings.Builder()
            .spawn(MobCategory.WATER_CREATURE, SpawnerData(EntityTypes.SQUID, 1, 1, 4))
            .spawn(MobCategory.WATER_AMBIENT, SpawnerData(EntityTypes.SALMON, 15, 1, 5))
            .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.POLAR_BEAR, 1, 1, 2))
            .common()
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.DROWNED, 5, 1, 1))
            .build()
    )

    private fun ocean(isDeep: Boolean, waterColor: Int, waterFogColor: Int, mobSettings: MobSpawnSettings.Builder) = KryptonBiome(
        ClimateSettings(Precipitation.RAIN, 0.5F, 0.5F),
        if (isDeep) -1.8F else -1F,
        0.1F,
        BiomeCategory.OCEAN,
        BiomeEffects(Color(12638463), Color(waterColor), Color(waterFogColor), 0.5F.calculateSkyColor(), ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)),
        mobSettings.build()
    )

    fun desert(depth: Float, scale: Float) = KryptonBiome(
        ClimateSettings(Precipitation.NONE, 2F, 0F),
        depth,
        scale,
        BiomeCategory.DESERT,
        BiomeEffects(Color(12638463), Color(4159204), Color(329011), 2F.calculateSkyColor(), ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)),
        MobSpawnSettings.Builder()
            .desert()
            .build()
    )

    fun mountain(depth: Float, scale: Float) = KryptonBiome(
        ClimateSettings(Precipitation.RAIN, 0.2F, 0.3F),
        depth,
        scale,
        BiomeCategory.EXTREME_HILLS,
        BiomeEffects(Color(12638463), Color(4159204), Color(329011), 0.2F.calculateSkyColor(), ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)),
        MobSpawnSettings.Builder()
            .farmAnimals()
            .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.LLAMA, 5, 4, 6))
            .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.GOAT, 10, 4, 6))
            .common()
            .build()
    )

    fun forest(depth: Float, scale: Float) = forest(depth, scale, MobSpawnSettings.Builder()
        .default()
        .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.WOLF, 5, 4, 4))
        .playerCanSpawn()
    )

    fun birchForest(depth: Float, scale: Float) = KryptonBiome(
        ClimateSettings(Precipitation.RAIN, 0.6F, 0.6F),
        depth,
        scale,
        BiomeCategory.FOREST,
        BiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.6F.calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        MobSpawnSettings.Builder()
            .farmAnimals()
            .common()
            .build()
    )

    fun flowerForest() = forest(0.1F, 0.4F, MobSpawnSettings.Builder()
        .default()
        .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.RABBIT, 4, 2, 3))
    )

    fun darkForest(depth: Float, scale: Float) = KryptonBiome(
        ClimateSettings(Precipitation.RAIN, 0.7F, 0.8F),
        depth,
        scale,
        BiomeCategory.FOREST,
        BiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.7F.calculateSkyColor(),
            GrassColorModifier.DARK_FOREST,
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        MobSpawnSettings.Builder()
            .farmAnimals()
            .common()
            .build()
    )

    private fun forest(depth: Float, scale: Float, mobSettings: MobSpawnSettings.Builder) = KryptonBiome(
        ClimateSettings(Precipitation.RAIN, 0.7F, 0.8F),
        depth,
        scale,
        BiomeCategory.FOREST,
        BiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.7F.calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        mobSettings.build()
    )

    fun taiga(depth: Float, scale: Float, isSnowy: Boolean, isMountains: Boolean) = KryptonBiome(
        ClimateSettings(if (isSnowy) Precipitation.SNOW else Precipitation.RAIN, if (isSnowy) -0.5F else -0.25F, if (isSnowy) 0.4F else 0.8F),
        depth,
        scale,
        BiomeCategory.TAIGA,
        BiomeEffects(
            Color(12638463),
            Color(if (isSnowy) 4020182 else 4159204),
            Color(329011),
            (if (isSnowy) -0.5F else -0.25F).calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        MobSpawnSettings.Builder()
            .farmAnimals()
            .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.WOLF, 8, 4, 4))
            .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.RABBIT, 4, 2, 3))
            .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.FOX, 8, 2, 4))
            .apply { if (!isSnowy && !isMountains) playerCanSpawn() }
            .common()
            .build()
    )

    fun giantTreeTaiga(depth: Float, scale: Float, temperature: Float, isSpruce: Boolean) = KryptonBiome(
        ClimateSettings(Precipitation.RAIN, temperature, 0.8F),
        depth,
        scale,
        BiomeCategory.TAIGA,
        BiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            temperature.calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        MobSpawnSettings.Builder()
            .farmAnimals()
            .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.WOLF, 8, 4, 4))
            .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.RABBIT, 4, 2, 3))
            .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.FOX, 8, 2, 4))
            .apply { if (isSpruce) common() else caves().monsters(100, 25, 100) }
            .build()
    )

    fun swamp(depth: Float, scale: Float) = KryptonBiome(
        ClimateSettings(Precipitation.RAIN, 0.8F, 0.9F),
        depth,
        scale,
        BiomeCategory.SWAMP,
        BiomeEffects(
            Color(12638463),
            Color(6388580),
            Color(2302743),
            0.8F.calculateSkyColor(),
            GrassColorModifier.SWAMP,
            Optional.of(Color(6975545)),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        MobSpawnSettings.Builder()
            .farmAnimals()
            .common()
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.SLIME, 1, 1, 1))
            .build()
    )

    fun tundra(depth: Float, scale: Float) = KryptonBiome(
        ClimateSettings(Precipitation.SNOW, 0F, 0.5F),
        depth,
        scale,
        BiomeCategory.ICY,
        BiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0F.calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        MobSpawnSettings.Builder()
            .creatureGenerationProbability(0.07F)
            .snowy()
            .build()
    )

    fun river(depth: Float, scale: Float, temperature: Float, waterColor: Int, isSnowy: Boolean) = KryptonBiome(
        ClimateSettings(if (isSnowy) Precipitation.SNOW else Precipitation.RAIN, temperature, 0.5F),
        depth,
        scale,
        BiomeCategory.RIVER,
        BiomeEffects(
            Color(12638463),
            Color(waterColor),
            Color(329011),
            temperature.calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        MobSpawnSettings.Builder()
            .spawn(MobCategory.WATER_CREATURE, SpawnerData(EntityTypes.SQUID, 2, 1, 4))
            .spawn(MobCategory.WATER_AMBIENT, SpawnerData(EntityTypes.SALMON, 5, 1, 5))
            .common()
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.DROWNED, if (isSnowy) 1 else 100, 1, 1))
            .build()
    )

    fun beach(depth: Float, scale: Float, temperature: Float, downfall: Float, waterColor: Int, isSnowy: Boolean, isShore: Boolean) = KryptonBiome(
        ClimateSettings(if (isSnowy) Precipitation.SNOW else Precipitation.RAIN, temperature, downfall),
        depth,
        scale,
        if (isShore) BiomeCategory.NONE else BiomeCategory.BEACH,
        BiomeEffects(
            Color(12638463),
            Color(waterColor),
            Color(329011),
            temperature.calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        MobSpawnSettings.Builder()
            .apply { if (!isSnowy && !isShore) spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.TURTLE, 5, 2, 5)) }
            .common()
            .build()
    )

    fun jungle() = jungle(0.1F, 0.2F, 40, 2, 3)

    fun jungleEdge() = jungle(0.1F, 0.2F, 0.8F, MobSpawnSettings.Builder().jungle())

    fun modifiedJungle() = jungle(0.2F, 0.4F, 0.9F, MobSpawnSettings.Builder()
        .jungle()
        .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.PARROT, 10, 1, 1))
        .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.OCELOT, 2, 1, 1))
    )

    fun modifiedJungleEdge() = jungle(0.2F, 0.4F, 0.8F, MobSpawnSettings.Builder().jungle())

    fun jungleHills() = jungle(0.45F, 0.3F, 10, 1, 1)

    fun bambooJungle() = bambooJungle(0.1F, 0.2F, 40, 2)

    fun bambooJungleHills() = bambooJungle(0.45F, 0.3F, 10, 1)

    private fun jungle(depth: Float, scale: Float, parrotWeight: Int, maxParrots: Int, maxOcelots: Int) = jungle(depth, scale, 0.9F, MobSpawnSettings.Builder()
        .jungle()
        .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.PARROT, parrotWeight, 1, maxParrots))
        .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.OCELOT, 2, 1, maxOcelots))
        .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.PANDA, 1, 1, 2))
    )

    private fun bambooJungle(depth: Float, scale: Float, parrotWeight: Int, maxParrots: Int) = jungle(depth, scale, 0.9F, MobSpawnSettings.Builder()
        .jungle()
        .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.PARROT, parrotWeight, 1, maxParrots))
        .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.PANDA, 80, 1, 2))
        .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.OCELOT, 2, 1, 1))
    )

    private fun jungle(depth: Float, scale: Float, downfall: Float, mobSettings: MobSpawnSettings.Builder) = KryptonBiome(
        ClimateSettings(Precipitation.RAIN, 0.95F, downfall),
        depth,
        scale,
        BiomeCategory.JUNGLE,
        BiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.95F.calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        mobSettings.build()
    )

    fun plains(isSunflower: Boolean) = KryptonBiome(
        ClimateSettings(Precipitation.RAIN, 0.8F, 0.4F),
        0.125F,
        0.05F,
        BiomeCategory.PLAINS,
        BiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.8F.calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        MobSpawnSettings.Builder()
            .plains()
            .apply { if (!isSunflower) playerCanSpawn() }
            .build()
    )

    fun end() = KryptonBiome(
        ClimateSettings(Precipitation.NONE, 0.5F, 0.5F),
        0.1F,
        0.2F,
        BiomeCategory.THE_END,
        BiomeEffects(
            Color(10518688),
            Color(4159204),
            Color(329011),
            Color(0),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        )
    )

    fun mushroomFields(depth: Float, scale: Float) = KryptonBiome(
        ClimateSettings(Precipitation.RAIN, 0.9F, 1F),
        depth,
        scale,
        BiomeCategory.MUSHROOM,
        BiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.9F.calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        MobSpawnSettings.Builder().mooshroom().build()
    )

    fun savanna(depth: Float, scale: Float, temperature: Float) = savanna(depth, scale, temperature, MobSpawnSettings.Builder().savanna())

    fun savannaPlateau() = savanna(1.5F, 0.025F, 1F, MobSpawnSettings.Builder()
        .savanna()
        .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.LLAMA, 8, 4, 4))
    )

    private fun savanna(depth: Float, scale: Float, temperature: Float, mobSettings: MobSpawnSettings.Builder) = KryptonBiome(
        ClimateSettings(Precipitation.NONE, temperature, 0F),
        depth,
        scale,
        BiomeCategory.SAVANNA,
        BiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            temperature.calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        mobSettings.build()
    )

    fun badlands(depth: Float, scale: Float) = KryptonBiome(
        ClimateSettings(Precipitation.NONE, 2F, 0F),
        depth,
        scale,
        BiomeCategory.MESA,
        BiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            2F.calculateSkyColor(),
            foliageColor = Optional.of(Color(10387789)),
            grassColor = Optional.of(Color(9470285)),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        MobSpawnSettings.Builder().common().build()
    )

    fun erodedBadlands() = badlands(0.1F, 0.2F)

    fun void() = KryptonBiome(
        ClimateSettings(Precipitation.NONE, 0.5F, 0.5F),
        0.1F,
        0.2F,
        BiomeCategory.NONE,
        BiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.5F.calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        )
    )

    fun netherWastes() = nether(
        3344392,
        329011,
        SoundEvents.AMBIENT_NETHER_WASTES_LOOP,
        SoundEvents.AMBIENT_NETHER_WASTES_MOOD,
        SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS,
        Musics.NETHER_WASTES,
        null,
        MobSpawnSettings.Builder()
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.GHAST, 50, 4, 4))
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.ZOMBIFIED_PIGLIN, 100, 4, 4))
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.MAGMA_CUBE, 2, 4, 4))
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.ENDERMAN, 1, 4, 4))
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.PIG, 15, 4, 4))
            .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.STRIDER, 60, 1, 2))
    )

    fun soulSandValley() = nether(
        1787717,
        329011,
        SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP,
        SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD,
        SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS,
        Musics.SOUL_SAND_VALLEY,
        AmbientParticleSettings(ParticleType.ASH, 0.00625F),
        MobSpawnSettings.Builder()
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.SKELETON, 20, 5, 5))
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.GHAST, 50, 4, 4))
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.ENDERMAN, 1, 4, 4))
            .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.STRIDER, 60, 1, 2))
            .mobCharge(EntityTypes.SKELETON, 0.7, 0.15)
            .mobCharge(EntityTypes.GHAST, 0.7, 0.15)
            .mobCharge(EntityTypes.ENDERMAN, 0.7, 0.15)
            .mobCharge(EntityTypes.STRIDER, 0.7, 0.15)
    )

    fun basaltDeltas() = nether(
        6840176,
        4341314,
        SoundEvents.AMBIENT_BASALT_DELTAS_LOOP,
        SoundEvents.AMBIENT_BASALT_DELTAS_MOOD,
        SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS,
        Musics.BASALT_DELTAS,
        AmbientParticleSettings(ParticleType.WHITE_ASH, 0.118093334F),
        MobSpawnSettings.Builder()
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.GHAST, 40, 1, 1))
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.MAGMA_CUBE, 100, 2, 5))
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.STRIDER, 60, 1, 2))
    )

    fun crimsonForest() = nether(
        3343107,
        329011,
        SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP,
        SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD,
        SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS,
        Musics.CRIMSON_FOREST,
        AmbientParticleSettings(ParticleType.CRIMSON_SPORE, 0.025F),
        MobSpawnSettings.Builder()
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.ZOMBIFIED_PIGLIN, 1, 2, 4))
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.HOGLIN, 9, 3, 4))
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.PIGLIN, 5, 3, 4))
            .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.STRIDER, 60, 1, 2))
    )

    fun warpedForest() = nether(
        1705242,
        329011,
        SoundEvents.AMBIENT_WARPED_FOREST_LOOP,
        SoundEvents.AMBIENT_WARPED_FOREST_MOOD,
        SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS,
        Musics.WARPED_FOREST,
        AmbientParticleSettings(ParticleType.WARPED_SPORE, 0.01428F),
        MobSpawnSettings.Builder()
            .spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.ENDERMAN, 1, 4, 4))
            .spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.STRIDER, 60, 1, 2))
            .mobCharge(EntityTypes.ENDERMAN, 1.0, 0.12)
    )

    private fun nether(
        fogColor: Int,
        waterFogColor: Int,
        loop: SoundEvent,
        mood: SoundEvent,
        additions: SoundEvent,
        music: Music,
        particles: AmbientParticleSettings? = null,
        mobSettings: MobSpawnSettings.Builder
    ) = KryptonBiome(
        ClimateSettings(Precipitation.NONE, 2F, 0F),
        0.1F,
        0.2F,
        BiomeCategory.NETHER,
        BiomeEffects(
            Color(fogColor),
            Color(4159204),
            Color(waterFogColor),
            2F.calculateSkyColor(),
            ambientParticleSettings = Optional.ofNullable(particles),
            ambientLoopSound = Optional.of(loop),
            ambientMoodSettings = Optional.of(AmbientMoodSettings(mood, 6000, 8, 2.0)),
            ambientAdditionsSettings = Optional.of(AmbientAdditionsSettings(additions, 0.0111)),
            backgroundMusic = Optional.of(music)
        ),
        mobSettings.build()
    )

    fun lushCaves() = KryptonBiome(
        ClimateSettings(Precipitation.RAIN, 0.5F, 0.5F),
        0.1F,
        0.2F,
        BiomeCategory.UNDERGROUND,
        BiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.5F.calculateSkyColor()
        ),
        MobSpawnSettings.Builder().common().build()
    )

    fun dripstoneCaves() = KryptonBiome(
        ClimateSettings(Precipitation.RAIN, 0.8F, 0.4F),
        0.125F,
        0.05F,
        BiomeCategory.UNDERGROUND,
        BiomeEffects(
            Color(12638463),
            Color(4159204),
            Color(329011),
            0.8F.calculateSkyColor(),
            ambientMoodSettings = Optional.of(AmbientMoodSettings.CAVE)
        ),
        MobSpawnSettings.Builder().common().build()
    )
}

private fun Float.calculateSkyColor(): Color {
    val temp = (this / 3F).clamp(-1F, 1F)
    return Color.getHSBColor(0.62222224F - temp * 0.05F, 0.5F + temp * 0.1F, 1F)
}

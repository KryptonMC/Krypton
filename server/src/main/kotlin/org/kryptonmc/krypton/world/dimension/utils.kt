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
package org.kryptonmc.krypton.world.dimension

import com.mojang.serialization.DataResult
import com.mojang.serialization.Dynamic
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.biome.gen.MultiNoiseBiomeGenerator
import org.kryptonmc.krypton.world.biome.gen.TheEndBiomeGenerator
import org.kryptonmc.krypton.world.generation.NoiseGenerator
import org.kryptonmc.krypton.world.generation.noise.NoiseGeneratorSettings
import java.nio.file.Path

fun Registry<KryptonDimensionType>.defaults(biomes: Registry<KryptonBiome>, noiseSettings: Registry<NoiseGeneratorSettings>, seed: Long) = KryptonRegistry(InternalResourceKeys.DIMENSION).apply {
    register(Dimension.NETHER, Dimension(defaultNether(biomes, noiseSettings, seed)) { this@defaults[DimensionTypes.NETHER_KEY]!! })
    register(Dimension.END, Dimension(defaultEnd(biomes, noiseSettings, seed)) { this@defaults[DimensionTypes.END_KEY]!! })
}

val Key.storageFolder: String
    get() = when (this) {
        World.OVERWORLD.location -> ""
        World.NETHER.location -> "DIM-1"
        World.END.location -> "DIM1"
        else -> value()
    }

fun ResourceKey<World>.storageFolder(path: Path): Path = when (this) {
    World.OVERWORLD -> path
    World.END -> path.resolve("DIM1")
    World.NETHER -> path.resolve("DIM-1")
    else -> path.resolve("dimensions/${location.namespace()}/${location.value()}")
}

fun Dynamic<*>.parseDimension(): DataResult<ResourceKey<World>> {
    val id = asNumber().result()
    if (id.isPresent) when (id.get().toInt()) {
        -1 -> return DataResult.success(World.NETHER)
        0 -> return DataResult.success(World.OVERWORLD)
        1 -> return DataResult.success(World.END)
    }
    return KryptonWorld.RESOURCE_KEY_CODEC.parse(this)
}

private fun defaultNether(biomes: Registry<KryptonBiome>, noiseSettings: Registry<NoiseGeneratorSettings>, seed: Long) = NoiseGenerator(
    MultiNoiseBiomeGenerator.Preset.NETHER.generator(biomes, seed),
    seed
) { noiseSettings[NoiseGeneratorSettings.NETHER]!! }

private fun defaultEnd(biomes: Registry<KryptonBiome>, noiseSettings: Registry<NoiseGeneratorSettings>, seed: Long) = NoiseGenerator(
    TheEndBiomeGenerator(biomes, seed),
    seed
) { noiseSettings[NoiseGeneratorSettings.END]!! }

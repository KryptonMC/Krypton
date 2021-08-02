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
package org.kryptonmc.krypton.registry

import com.mojang.serialization.Codec
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.krypton.entity.memory.MemoryKey
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.event.GameEvent
import org.kryptonmc.krypton.world.fluid.Fluid
import org.kryptonmc.krypton.world.generation.Generator
import org.kryptonmc.krypton.world.biome.gen.BiomeGenerator
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.generation.feature.ConfiguredFeature
import org.kryptonmc.krypton.world.generation.feature.Feature
import org.kryptonmc.krypton.world.generation.feature.StructureFeature
import org.kryptonmc.krypton.world.generation.noise.NoiseGeneratorSettings

object InternalResourceKeys {

    val MEMORIES = ResourceKeys.minecraft<MemoryKey<Any>>("memory_module_type")
    val WORLD = ResourceKeys.minecraft<KryptonWorld>("dimension")
    val DIMENSION = ResourceKeys.minecraft<Dimension>("dimension")
    val DIMENSION_TYPE = ResourceKeys.minecraft<KryptonDimensionType>("dimension_type")
    val GAME_EVENT = ResourceKeys.minecraft<GameEvent>("game_event")
    val FLUID = ResourceKeys.minecraft<Fluid>("fluid")

    // World generation resources
    val BIOME = ResourceKeys.minecraft<KryptonBiome>("worldgen/biome")
    val GENERATOR = ResourceKeys.minecraft<Codec<out Generator>>("worldgen/chunk_generator")
    val BIOME_GENERATOR = ResourceKeys.minecraft<Codec<out BiomeGenerator>>("worldgen/biome_source")
    val FEATURE = ResourceKeys.minecraft<Feature<*>>("worldgen/feature")
    val CONFIGURED_FEATURE = ResourceKeys.minecraft<ConfiguredFeature<*, *>>("worldgen/configured_feature")
    val STRUCTURE = ResourceKeys.minecraft<StructureFeature<*>>("worldgen/structure_feature")
    val NOISE_GENERATOR_SETTINGS = ResourceKeys.minecraft<NoiseGeneratorSettings>("worldgen/noise_settings")
    val CHUNK_STATUS = ResourceKeys.minecraft<ChunkStatus>("chunk_status")
}

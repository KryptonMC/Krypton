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
package org.kryptonmc.krypton.world.biome.gen

import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.registry.RegistryLookupCodec
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.biome.layer.Layers

class VanillaLayeredBiomeGenerator(
    private val seed: Long,
    private val legacyBiomeInitLayer: Boolean,
    private val largeBiomes: Boolean,
    private val biomes: Registry<KryptonBiome>
) : BiomeGenerator(POSSIBLE_BIOMES.asSequence().map { { biomes[it]!! } }) {

    private val noiseBiomeLayer = Layers.default(seed, legacyBiomeInitLayer, if (largeBiomes) 6 else 4, 4)
    override val codec = CODEC

    override fun get(x: Int, y: Int, z: Int) = noiseBiomeLayer[biomes, x, z]

    companion object {

        val CODEC: Codec<VanillaLayeredBiomeGenerator> = RecordCodecBuilder.create {
            it.group(
                Codec.LONG.fieldOf("seed").stable().forGetter(VanillaLayeredBiomeGenerator::seed),
                Codec.BOOL.optionalFieldOf("legacy_biome_init_layer", false, Lifecycle.stable()).forGetter(VanillaLayeredBiomeGenerator::legacyBiomeInitLayer),
                Codec.BOOL.fieldOf("large_biomes").orElse(false).stable().forGetter(VanillaLayeredBiomeGenerator::largeBiomes),
                RegistryLookupCodec(InternalResourceKeys.BIOME).forGetter(VanillaLayeredBiomeGenerator::biomes)
            ).apply(it, ::VanillaLayeredBiomeGenerator)
        }
        private val POSSIBLE_BIOMES = listOf(
            BiomeKeys.OCEAN,
            BiomeKeys.PLAINS,
            BiomeKeys.DESERT,
            BiomeKeys.MOUNTAINS,
            BiomeKeys.FOREST,
            BiomeKeys.TAIGA,
            BiomeKeys.SWAMP,
            BiomeKeys.RIVER,
            BiomeKeys.FROZEN_OCEAN,
            BiomeKeys.FROZEN_RIVER,
            BiomeKeys.SNOWY_TUNDRA,
            BiomeKeys.SNOWY_MOUNTAINS,
            BiomeKeys.MUSHROOM_FIELDS,
            BiomeKeys.MUSHROOM_FIELD_SHORE,
            BiomeKeys.BEACH,
            BiomeKeys.DESERT_HILLS,
            BiomeKeys.WOODED_HILLS,
            BiomeKeys.TAIGA_HILLS,
            BiomeKeys.MOUNTAIN_EDGE,
            BiomeKeys.JUNGLE,
            BiomeKeys.JUNGLE_HILLS,
            BiomeKeys.JUNGLE_EDGE,
            BiomeKeys.DEEP_OCEAN,
            BiomeKeys.STONE_SHORE,
            BiomeKeys.SNOWY_BEACH,
            BiomeKeys.BIRCH_FOREST,
            BiomeKeys.BIRCH_FOREST_HILLS,
            BiomeKeys.DARK_FOREST,
            BiomeKeys.SNOWY_TAIGA,
            BiomeKeys.SNOWY_TAIGA_HILLS,
            BiomeKeys.GIANT_TREE_TAIGA,
            BiomeKeys.GIANT_TREE_TAIGA_HILLS,
            BiomeKeys.WOODED_MOUNTAINS,
            BiomeKeys.SAVANNA,
            BiomeKeys.SAVANNA_PLATEAU,
            BiomeKeys.BADLANDS,
            BiomeKeys.WOODED_BADLANDS_PLATEAU,
            BiomeKeys.BADLANDS_PLATEAU,
            BiomeKeys.WARM_OCEAN,
            BiomeKeys.LUKEWARM_OCEAN,
            BiomeKeys.COLD_OCEAN,
            BiomeKeys.DEEP_WARM_OCEAN,
            BiomeKeys.DEEP_LUKEWARM_OCEAN,
            BiomeKeys.DEEP_COLD_OCEAN,
            BiomeKeys.DEEP_FROZEN_OCEAN,
            BiomeKeys.SUNFLOWER_PLAINS,
            BiomeKeys.DESERT_LAKES,
            BiomeKeys.GRAVELLY_MOUNTAINS,
            BiomeKeys.FLOWER_FOREST,
            BiomeKeys.TAIGA_MOUNTAINS,
            BiomeKeys.SWAMP_HILLS,
            BiomeKeys.ICE_SPIKES,
            BiomeKeys.MODIFIED_JUNGLE,
            BiomeKeys.MODIFIED_JUNGLE_EDGE,
            BiomeKeys.TALL_BIRCH_FOREST,
            BiomeKeys.TALL_BIRCH_HILLS,
            BiomeKeys.DARK_FOREST_HILLS,
            BiomeKeys.SNOWY_TAIGA_MOUNTAINS,
            BiomeKeys.GIANT_SPRUCE_TAIGA,
            BiomeKeys.GIANT_SPRUCE_TAIGA_HILLS,
            BiomeKeys.MODIFIED_GRAVELLY_MOUNTAINS,
            BiomeKeys.SHATTERED_SAVANNA,
            BiomeKeys.SHATTERED_SAVANNA_PLATEAU,
            BiomeKeys.ERODED_BADLANDS,
            BiomeKeys.MODIFIED_WOODED_BADLANDS_PLATEAU,
            BiomeKeys.MODIFIED_BADLANDS_PLATEAU
        )
    }
}

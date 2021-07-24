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

import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.InternalRegistries

object KryptonBiomes {

    val OCEAN = register(BiomeKeys.OCEAN, BuiltInBiomes.ocean(false))
    val PLAINS = register(BiomeKeys.PLAINS, BuiltInBiomes.plains())
    val DESERT = register(BiomeKeys.DESERT, BuiltInBiomes.desert(0.125F, 0.05F))
    val MOUNTAINS = register(BiomeKeys.MOUNTAINS, BuiltInBiomes.mountain(1F, 0.5F))
    val FOREST = register(BiomeKeys.FOREST, BuiltInBiomes.forest(0.1F, 0.2F))
    val TAIGA = register(BiomeKeys.TAIGA, BuiltInBiomes.taiga(0.2F, 0.2F, false))
    val SWAMP = register(BiomeKeys.SWAMP, BuiltInBiomes.swamp(-0.2F, 0.1F))
    val RIVER = register(BiomeKeys.RIVER, BuiltInBiomes.river(-0.5F, 0F, 0.5F, 4159204, false))
    val NETHER_WASTES = register(BiomeKeys.NETHER_WASTES, BuiltInBiomes.netherWastes())
    val THE_END = register(BiomeKeys.THE_END, BuiltInBiomes.end())
    val FROZEN_OCEAN = register(BiomeKeys.FROZEN_OCEAN, BuiltInBiomes.frozenOcean(false))
    val FROZEN_RIVER = register(BiomeKeys.FROZEN_RIVER, BuiltInBiomes.river(-0.5F, 0F, 0F, 3750089, true))
    val SNOWY_TUNDRA = register(BiomeKeys.SNOWY_TUNDRA, BuiltInBiomes.tundra(0.125F, 0.05F))
    val SNOWY_MOUNTAINS = register(BiomeKeys.SNOWY_MOUNTAINS, BuiltInBiomes.tundra(0.45F, 0.3F))
    val MUSHROOM_FIELDS = register(BiomeKeys.MUSHROOM_FIELDS, BuiltInBiomes.mushroomFields(0.2F, 0.3F))
    val MUSHROOM_FIELD_SHORE = register(BiomeKeys.MUSHROOM_FIELD_SHORE, BuiltInBiomes.mushroomFields(0F, 0.025F))
    val BEACH = register(BiomeKeys.BEACH, BuiltInBiomes.beach(0F, 0.025F, 0.8F, 0.4F, 4159204, false, false))
    val DESERT_HILLS = register(BiomeKeys.DESERT_HILLS, BuiltInBiomes.desert(0.45F, 0.3F))
    val WOODED_HILLS = register(BiomeKeys.WOODED_HILLS, BuiltInBiomes.forest(0.45F, 0.3F))
    val TAIGA_HILLS = register(BiomeKeys.TAIGA_HILLS, BuiltInBiomes.taiga(0.45F, 0.3F, false))
    val MOUNTAIN_EDGE = register(BiomeKeys.MOUNTAIN_EDGE, BuiltInBiomes.mountain(0.8F, 0.3F))
    val JUNGLE = register(BiomeKeys.JUNGLE, BuiltInBiomes.jungle())
    val JUNGLE_HILLS = register(BiomeKeys.JUNGLE_HILLS, BuiltInBiomes.jungleHills())
    val JUNGLE_EDGE = register(BiomeKeys.JUNGLE_EDGE, BuiltInBiomes.jungleEdge())
    val DEEP_OCEAN = register(BiomeKeys.DEEP_OCEAN, BuiltInBiomes.ocean(true))
    val STONE_SHORE = register(BiomeKeys.STONE_SHORE, BuiltInBiomes.beach(0.1F, 0.8F, 0.2F, 0.3F, 4159204, false, true))
    val SNOWY_BEACH = register(BiomeKeys.SNOWY_BEACH, BuiltInBiomes.beach(0F, 0.025F, 0.05F, 0.3F, 4020182, true, false))
    val BIRCH_FOREST = register(BiomeKeys.BIRCH_FOREST, BuiltInBiomes.birchForest(0.1F, 0.2F))
    val BIRCH_FOREST_HILLS = register(BiomeKeys.BIRCH_FOREST_HILLS, BuiltInBiomes.birchForest(0.45F, 0.3F))
    val DARK_FOREST = register(BiomeKeys.DARK_FOREST, BuiltInBiomes.darkForest(0.1F, 0.2F))
    val SNOWY_TAIGA = register(BiomeKeys.SNOWY_TAIGA, BuiltInBiomes.taiga(0.2F, 0.2F, true))
    val SNOWY_TAIGA_HILLS = register(BiomeKeys.SNOWY_TAIGA_HILLS, BuiltInBiomes.taiga(0.45F, 0.3F, true))
    val GIANT_TREE_TAIGA = register(BiomeKeys.GIANT_TREE_TAIGA, BuiltInBiomes.giantTreeTaiga(0.2F, 0.2F, 0.3F))
    val GIANT_TREE_TAIGA_HILLS = register(BiomeKeys.GIANT_TREE_TAIGA_HILLS, BuiltInBiomes.giantTreeTaiga(0.45F, 0.3F, 0.3F))
    val WOODED_MOUNTAINS = register(BiomeKeys.WOODED_MOUNTAINS, BuiltInBiomes.mountain(1F, 0.5F))
    val SAVANNA = register(BiomeKeys.SAVANNA, BuiltInBiomes.savanna(0.125F, 0.05F, 1.2F))
    val SAVANNA_PLATEAU = register(BiomeKeys.SAVANNA_PLATEAU, BuiltInBiomes.savannaPlateau())
    val BADLANDS = register(BiomeKeys.BADLANDS, BuiltInBiomes.badlands(0.1F, 0.2F))
    val WOODED_BADLANDS_PLATEAU = register(BiomeKeys.WOODED_BADLANDS_PLATEAU, BuiltInBiomes.badlands(1.5F, 0.025F))
    val BADLANDS_PLATEAU = register(BiomeKeys.BADLANDS_PLATEAU, BuiltInBiomes.badlands(1.5F, 0.025F))
    val SMALL_END_ISLANDS = register(BiomeKeys.SMALL_END_ISLANDS, BuiltInBiomes.end())
    val END_MIDLANDS = register(BiomeKeys.END_MIDLANDS, BuiltInBiomes.end())
    val END_HIGHLANDS = register(BiomeKeys.END_HIGHLANDS, BuiltInBiomes.end())
    val END_BARRENS = register(BiomeKeys.END_BARRENS, BuiltInBiomes.end())
    val WARM_OCEAN = register(BiomeKeys.WARM_OCEAN, BuiltInBiomes.warmOcean(false))
    val LUKEWARM_OCEAN = register(BiomeKeys.LUKEWARM_OCEAN, BuiltInBiomes.lukewarmOcean(false))
    val COLD_OCEAN = register(BiomeKeys.COLD_OCEAN, BuiltInBiomes.coldOcean(false))
    val DEEP_WARM_OCEAN = register(BiomeKeys.DEEP_WARM_OCEAN, BuiltInBiomes.warmOcean(true))
    val DEEP_LUKEWARM_OCEAN = register(BiomeKeys.DEEP_LUKEWARM_OCEAN, BuiltInBiomes.lukewarmOcean(true))
    val DEEP_COLD_OCEAN = register(BiomeKeys.DEEP_COLD_OCEAN, BuiltInBiomes.coldOcean(true))
    val DEEP_FROZEN_OCEAN = register(BiomeKeys.DEEP_FROZEN_OCEAN, BuiltInBiomes.frozenOcean(true))
    val THE_VOID = register(BiomeKeys.THE_VOID, 127, BuiltInBiomes.void())
    val SUNFLOWER_PLAINS = register(BiomeKeys.SUNFLOWER_PLAINS, 129, BuiltInBiomes.plains())
    val DESERT_LAKES = register(BiomeKeys.DESERT_LAKES, BuiltInBiomes.desert(0.225F, 0.25F))
    val GRAVELLY_MOUNTAINS = register(BiomeKeys.GRAVELLY_MOUNTAINS, BuiltInBiomes.mountain(1F, 0.5F))
    val FLOWER_FOREST = register(BiomeKeys.FLOWER_FOREST, BuiltInBiomes.flowerForest())
    val TAIGA_MOUNTAINS = register(BiomeKeys.TAIGA_MOUNTAINS, BuiltInBiomes.taiga(0.3F, 0.4F, false))
    val SWAMP_HILLS = register(BiomeKeys.SWAMP_HILLS, BuiltInBiomes.swamp(-0.1F, 0.3F))
    val ICE_SPIKES = register(BiomeKeys.ICE_SPIKES, 140, BuiltInBiomes.tundra(0.425F, 0.45000002F))
    val MODIFIED_JUNGLE = register(BiomeKeys.MODIFIED_JUNGLE, 149, BuiltInBiomes.modifiedJungle())
    val MODIFIED_JUNGLE_EDGE = register(BiomeKeys.MODIFIED_JUNGLE_EDGE, 151, BuiltInBiomes.modifiedJungleEdge())
    val TALL_BIRCH_FOREST = register(BiomeKeys.TALL_BIRCH_FOREST, 155, BuiltInBiomes.birchForest(0.2F, 0.4F))
    val TALL_BIRCH_HILLS = register(BiomeKeys.TALL_BIRCH_HILLS, BuiltInBiomes.birchForest(0.55F, 0.5F))
    val DARK_FOREST_HILLS = register(BiomeKeys.DARK_FOREST_HILLS, BuiltInBiomes.darkForest(0.2F, 0.4F))
    val SNOWY_TAIGA_MOUNTAINS = register(BiomeKeys.SNOWY_TAIGA_MOUNTAINS, BuiltInBiomes.taiga(0.3F, 0.4F, true))
    val GIANT_SPRUCE_TAIGA = register(BiomeKeys.GIANT_SPRUCE_TAIGA, 160, BuiltInBiomes.giantTreeTaiga(0.2F, 0.2F, 0.25F))
    val GIANT_SPRUCE_TAIGA_HILLS = register(BiomeKeys.GIANT_SPRUCE_TAIGA_HILLS, BuiltInBiomes.giantTreeTaiga(0.2F, 0.2F, 0.25F))
    val MODIFIED_GRAVELLY_MOUNTAINS = register(BiomeKeys.MODIFIED_GRAVELLY_MOUNTAINS, BuiltInBiomes.mountain(1F, 0.5F))
    val SHATTERED_SAVANNA = register(BiomeKeys.SHATTERED_SAVANNA, BuiltInBiomes.savanna(0.3625F, 1.225F, 1.1F))
    val SHATTERED_SAVANNA_PLATEAU = register(BiomeKeys.SHATTERED_SAVANNA_PLATEAU, BuiltInBiomes.savanna(1.05F, 1.2125001F, 1F))
    val ERODED_BADLANDS = register(BiomeKeys.ERODED_BADLANDS, BuiltInBiomes.erodedBadlands())
    val MODIFIED_WOODED_BADLANDS_PLATEAU = register(BiomeKeys.MODIFIED_WOODED_BADLANDS_PLATEAU, BuiltInBiomes.badlands(0.45F, 0.3F))
    val MODIFIED_BADLANDS_PLATEAU = register(BiomeKeys.MODIFIED_BADLANDS_PLATEAU, BuiltInBiomes.badlands(0.45F, 0.3F))
    val BAMBOO_JUNGLE = register(BiomeKeys.BAMBOO_JUNGLE, BuiltInBiomes.bambooJungle())
    val BAMBOO_JUNGLE_HILLS = register(BiomeKeys.BAMBOO_JUNGLE_HILLS, BuiltInBiomes.bambooJungleHills())
    val SOUL_SAND_VALLEY = register(BiomeKeys.SOUL_SAND_VALLEY, BuiltInBiomes.soulSandValley())
    val CRIMSON_FOREST = register(BiomeKeys.CRIMSON_FOREST, BuiltInBiomes.crimsonForest())
    val WARPED_FOREST = register(BiomeKeys.WARPED_FOREST, BuiltInBiomes.warpedForest())
    val BASALT_DELTAS = register(BiomeKeys.BASALT_DELTAS, BuiltInBiomes.basaltDeltas())
    val DRIPSTONE_CAVES = register(BiomeKeys.DRIPSTONE_CAVES, BuiltInBiomes.dripstoneCaves())
    val LUSH_CAVES = register(BiomeKeys.LUSH_CAVES, BuiltInBiomes.lushCaves())

    private fun register(key: ResourceKey<KryptonBiome>, biome: KryptonBiome) = InternalRegistries.BIOME.register(key, biome)

    private fun register(key: ResourceKey<KryptonBiome>, id: Int, biome: KryptonBiome) = InternalRegistries.BIOME.register(id, key, biome)
}

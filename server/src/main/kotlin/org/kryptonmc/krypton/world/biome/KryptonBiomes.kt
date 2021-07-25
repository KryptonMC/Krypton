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

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.InternalRegistries

object KryptonBiomes {

    val ID_TO_KEY = Int2ObjectArrayMap<ResourceKey<KryptonBiome>>()
    val PLAINS = register(BiomeKeys.PLAINS, 1, BuiltInBiomes.plains())
    val THE_VOID = register(BiomeKeys.THE_VOID, 127, BuiltInBiomes.void())

    init {
        register(BiomeKeys.OCEAN, 0, BuiltInBiomes.ocean(false))
        register(BiomeKeys.DESERT, 2, BuiltInBiomes.desert(0.125F, 0.05F))
        register(BiomeKeys.MOUNTAINS, 3, BuiltInBiomes.mountain(1F, 0.5F))
        register(BiomeKeys.FOREST, 4, BuiltInBiomes.forest(0.1F, 0.2F))
        register(BiomeKeys.TAIGA, 5, BuiltInBiomes.taiga(0.2F, 0.2F, false))
        register(BiomeKeys.SWAMP, 6, BuiltInBiomes.swamp(-0.2F, 0.1F))
        register(BiomeKeys.RIVER, 7, BuiltInBiomes.river(-0.5F, 0F, 0.5F, 4159204, false))
        register(BiomeKeys.NETHER_WASTES, 8, BuiltInBiomes.netherWastes())
        register(BiomeKeys.THE_END, 9, BuiltInBiomes.end())
        register(BiomeKeys.FROZEN_OCEAN, 10, BuiltInBiomes.frozenOcean(false))
        register(BiomeKeys.FROZEN_RIVER, 11, BuiltInBiomes.river(-0.5F, 0F, 0F, 3750089, true))
        register(BiomeKeys.SNOWY_TUNDRA, 12, BuiltInBiomes.tundra(0.125F, 0.05F))
        register(BiomeKeys.SNOWY_MOUNTAINS, 13, BuiltInBiomes.tundra(0.45F, 0.3F))
        register(BiomeKeys.MUSHROOM_FIELDS, 14, BuiltInBiomes.mushroomFields(0.2F, 0.3F))
        register(BiomeKeys.MUSHROOM_FIELD_SHORE, 15, BuiltInBiomes.mushroomFields(0F, 0.025F))
        register(BiomeKeys.BEACH, 16, BuiltInBiomes.beach(0F, 0.025F, 0.8F, 0.4F, 4159204, false, false))
        register(BiomeKeys.DESERT_HILLS, 17, BuiltInBiomes.desert(0.45F, 0.3F))
        register(BiomeKeys.WOODED_HILLS, 18, BuiltInBiomes.forest(0.45F, 0.3F))
        register(BiomeKeys.TAIGA_HILLS, 19, BuiltInBiomes.taiga(0.45F, 0.3F, false))
        register(BiomeKeys.MOUNTAIN_EDGE, 20, BuiltInBiomes.mountain(0.8F, 0.3F))
        register(BiomeKeys.JUNGLE, 21, BuiltInBiomes.jungle())
        register(BiomeKeys.JUNGLE_HILLS, 22, BuiltInBiomes.jungleHills())
        register(BiomeKeys.JUNGLE_EDGE, 23, BuiltInBiomes.jungleEdge())
        register(BiomeKeys.DEEP_OCEAN, 24, BuiltInBiomes.ocean(true))
        register(BiomeKeys.STONE_SHORE, 25, BuiltInBiomes.beach(0.1F, 0.8F, 0.2F, 0.3F, 4159204, false, true))
        register(BiomeKeys.SNOWY_BEACH, 26, BuiltInBiomes.beach(0F, 0.025F, 0.05F, 0.3F, 4020182, true, false))
        register(BiomeKeys.BIRCH_FOREST, 27, BuiltInBiomes.birchForest(0.1F, 0.2F))
        register(BiomeKeys.BIRCH_FOREST_HILLS, 28, BuiltInBiomes.birchForest(0.45F, 0.3F))
        register(BiomeKeys.DARK_FOREST, 29, BuiltInBiomes.darkForest(0.1F, 0.2F))
        register(BiomeKeys.SNOWY_TAIGA, 30, BuiltInBiomes.taiga(0.2F, 0.2F, true))
        register(BiomeKeys.SNOWY_TAIGA_HILLS, 31, BuiltInBiomes.taiga(0.45F, 0.3F, true))
        register(BiomeKeys.GIANT_TREE_TAIGA, 32, BuiltInBiomes.giantTreeTaiga(0.2F, 0.2F, 0.3F))
        register(BiomeKeys.GIANT_TREE_TAIGA_HILLS, 33, BuiltInBiomes.giantTreeTaiga(0.45F, 0.3F, 0.3F))
        register(BiomeKeys.WOODED_MOUNTAINS, 34, BuiltInBiomes.mountain(1F, 0.5F))
        register(BiomeKeys.SAVANNA, 35, BuiltInBiomes.savanna(0.125F, 0.05F, 1.2F))
        register(BiomeKeys.SAVANNA_PLATEAU, 36, BuiltInBiomes.savannaPlateau())
        register(BiomeKeys.BADLANDS, 37, BuiltInBiomes.badlands(0.1F, 0.2F))
        register(BiomeKeys.WOODED_BADLANDS_PLATEAU, 38, BuiltInBiomes.badlands(1.5F, 0.025F))
        register(BiomeKeys.BADLANDS_PLATEAU, 39, BuiltInBiomes.badlands(1.5F, 0.025F))
        register(BiomeKeys.SMALL_END_ISLANDS, 40, BuiltInBiomes.end())
        register(BiomeKeys.END_MIDLANDS, 41, BuiltInBiomes.end())
        register(BiomeKeys.END_HIGHLANDS, 42, BuiltInBiomes.end())
        register(BiomeKeys.END_BARRENS, 43, BuiltInBiomes.end())
        register(BiomeKeys.WARM_OCEAN, 44, BuiltInBiomes.warmOcean(false))
        register(BiomeKeys.LUKEWARM_OCEAN, 45, BuiltInBiomes.lukewarmOcean(false))
        register(BiomeKeys.COLD_OCEAN, 46, BuiltInBiomes.coldOcean(false))
        register(BiomeKeys.DEEP_WARM_OCEAN, 47, BuiltInBiomes.warmOcean(true))
        register(BiomeKeys.DEEP_LUKEWARM_OCEAN, 48, BuiltInBiomes.lukewarmOcean(true))
        register(BiomeKeys.DEEP_COLD_OCEAN, 49, BuiltInBiomes.coldOcean(true))
        register(BiomeKeys.DEEP_FROZEN_OCEAN, 50, BuiltInBiomes.frozenOcean(true))
        register(BiomeKeys.SUNFLOWER_PLAINS, 129, BuiltInBiomes.plains())
        register(BiomeKeys.DESERT_LAKES, 130, BuiltInBiomes.desert(0.225F, 0.25F))
        register(BiomeKeys.GRAVELLY_MOUNTAINS, 131, BuiltInBiomes.mountain(1F, 0.5F))
        register(BiomeKeys.FLOWER_FOREST, 132, BuiltInBiomes.flowerForest())
        register(BiomeKeys.TAIGA_MOUNTAINS, 133, BuiltInBiomes.taiga(0.3F, 0.4F, false))
        register(BiomeKeys.SWAMP_HILLS, 134, BuiltInBiomes.swamp(-0.1F, 0.3F))
        register(BiomeKeys.ICE_SPIKES, 140, BuiltInBiomes.tundra(0.425F, 0.45000002F))
        register(BiomeKeys.MODIFIED_JUNGLE, 149, BuiltInBiomes.modifiedJungle())
        register(BiomeKeys.MODIFIED_JUNGLE_EDGE, 151, BuiltInBiomes.modifiedJungleEdge())
        register(BiomeKeys.TALL_BIRCH_FOREST, 155, BuiltInBiomes.birchForest(0.2F, 0.4F))
        register(BiomeKeys.TALL_BIRCH_HILLS, 156, BuiltInBiomes.birchForest(0.55F, 0.5F))
        register(BiomeKeys.DARK_FOREST_HILLS, 157, BuiltInBiomes.darkForest(0.2F, 0.4F))
        register(BiomeKeys.SNOWY_TAIGA_MOUNTAINS, 158, BuiltInBiomes.taiga(0.3F, 0.4F, true))
        register(BiomeKeys.GIANT_SPRUCE_TAIGA, 160, BuiltInBiomes.giantTreeTaiga(0.2F, 0.2F, 0.25F))
        register(BiomeKeys.GIANT_SPRUCE_TAIGA_HILLS, 161, BuiltInBiomes.giantTreeTaiga(0.2F, 0.2F, 0.25F))
        register(BiomeKeys.MODIFIED_GRAVELLY_MOUNTAINS, 162, BuiltInBiomes.mountain(1F, 0.5F))
        register(BiomeKeys.SHATTERED_SAVANNA, 163, BuiltInBiomes.savanna(0.3625F, 1.225F, 1.1F))
        register(BiomeKeys.SHATTERED_SAVANNA_PLATEAU, 164, BuiltInBiomes.savanna(1.05F, 1.2125001F, 1F))
        register(BiomeKeys.ERODED_BADLANDS, 165, BuiltInBiomes.erodedBadlands())
        register(BiomeKeys.MODIFIED_WOODED_BADLANDS_PLATEAU, 166, BuiltInBiomes.badlands(0.45F, 0.3F))
        register(BiomeKeys.MODIFIED_BADLANDS_PLATEAU, 167, BuiltInBiomes.badlands(0.45F, 0.3F))
        register(BiomeKeys.BAMBOO_JUNGLE, 168, BuiltInBiomes.bambooJungle())
        register(BiomeKeys.BAMBOO_JUNGLE_HILLS, 169, BuiltInBiomes.bambooJungleHills())
        register(BiomeKeys.SOUL_SAND_VALLEY, 170, BuiltInBiomes.soulSandValley())
        register(BiomeKeys.CRIMSON_FOREST, 171, BuiltInBiomes.crimsonForest())
        register(BiomeKeys.WARPED_FOREST, 172, BuiltInBiomes.warpedForest())
        register(BiomeKeys.BASALT_DELTAS, 173, BuiltInBiomes.basaltDeltas())
        register(BiomeKeys.DRIPSTONE_CAVES, 174, BuiltInBiomes.dripstoneCaves())
        register(BiomeKeys.LUSH_CAVES, 175, BuiltInBiomes.lushCaves())
    }

    private fun register(key: ResourceKey<KryptonBiome>, id: Int, biome: KryptonBiome): KryptonBiome {
        ID_TO_KEY[id] = key
        return InternalRegistries.BIOME.register(id, key, biome)
    }
}

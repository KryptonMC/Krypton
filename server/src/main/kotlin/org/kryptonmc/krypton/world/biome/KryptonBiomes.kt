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
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.registry.InternalRegistries

object KryptonBiomes {

    val ID_TO_KEY = Int2ObjectArrayMap<ResourceKey<Biome>>()
    val PLAINS = register(BiomeKeys.PLAINS, 1, BuiltInBiomes.plains(BiomeKeys.PLAINS.location))
    val THE_VOID = register(BiomeKeys.THE_VOID, 127, BuiltInBiomes.void(BiomeKeys.THE_VOID.location))

    init {
        register(BiomeKeys.OCEAN, 0, BuiltInBiomes.ocean(BiomeKeys.OCEAN.location, false))
        register(BiomeKeys.DESERT, 2, BuiltInBiomes.desert(BiomeKeys.DESERT.location, 0.125F, 0.05F))
        register(BiomeKeys.MOUNTAINS, 3, BuiltInBiomes.mountain(BiomeKeys.MOUNTAINS.location, 1F, 0.5F))
        register(BiomeKeys.FOREST, 4, BuiltInBiomes.forest(BiomeKeys.FOREST.location, 0.1F, 0.2F))
        register(BiomeKeys.TAIGA, 5, BuiltInBiomes.taiga(BiomeKeys.TAIGA.location, 0.2F, 0.2F, false))
        register(BiomeKeys.SWAMP, 6, BuiltInBiomes.swamp(BiomeKeys.SWAMP.location, -0.2F, 0.1F))
        register(
            BiomeKeys.RIVER,
            7,
            BuiltInBiomes.river(BiomeKeys.RIVER.location, -0.5F, 0F, 0.5F, 4159204, false)
        )
        register(BiomeKeys.NETHER_WASTES, 8, BuiltInBiomes.netherWastes(BiomeKeys.NETHER_WASTES.location))
        register(BiomeKeys.THE_END, 9, BuiltInBiomes.end(BiomeKeys.THE_END.location))
        register(BiomeKeys.FROZEN_OCEAN, 10, BuiltInBiomes.frozenOcean(BiomeKeys.FROZEN_OCEAN.location, false))
        register(
            BiomeKeys.FROZEN_RIVER,
            11,
            BuiltInBiomes.river(BiomeKeys.FROZEN_RIVER.location, -0.5F, 0F, 0F, 3750089, true)
        )
        register(BiomeKeys.SNOWY_TUNDRA, 12, BuiltInBiomes.tundra(BiomeKeys.SNOWY_TUNDRA.location, 0.125F, 0.05F))
        register(BiomeKeys.SNOWY_MOUNTAINS, 13, BuiltInBiomes.tundra(BiomeKeys.SNOWY_MOUNTAINS.location, 0.45F, 0.3F))
        register(BiomeKeys.MUSHROOM_FIELDS, 14, BuiltInBiomes.mushroomFields(BiomeKeys.MUSHROOM_FIELDS.location, 0.2F, 0.3F))
        register(BiomeKeys.MUSHROOM_FIELD_SHORE, 15, BuiltInBiomes.mushroomFields(BiomeKeys.MUSHROOM_FIELD_SHORE.location, 0F, 0.025F))
        register(BiomeKeys.BEACH, 16, BuiltInBiomes.beach(
            BiomeKeys.BEACH.location,
            0F,
            0.025F,
            0.8F,
            0.4F,
            4159204,
            false,
            false
        ))
        register(BiomeKeys.DESERT_HILLS, 17, BuiltInBiomes.desert(BiomeKeys.DESERT_HILLS.location, 0.45F, 0.3F))
        register(BiomeKeys.WOODED_HILLS, 18, BuiltInBiomes.forest(BiomeKeys.WOODED_HILLS.location, 0.45F, 0.3F))
        register(BiomeKeys.TAIGA_HILLS, 19, BuiltInBiomes.taiga(BiomeKeys.TAIGA_HILLS.location, 0.45F, 0.3F, false))
        register(BiomeKeys.MOUNTAIN_EDGE, 20, BuiltInBiomes.mountain(BiomeKeys.MOUNTAIN_EDGE.location, 0.8F, 0.3F))
        register(BiomeKeys.JUNGLE, 21, BuiltInBiomes.jungle(BiomeKeys.JUNGLE.location))
        register(BiomeKeys.JUNGLE_HILLS, 22, BuiltInBiomes.jungleHills(BiomeKeys.JUNGLE_HILLS.location))
        register(BiomeKeys.JUNGLE_EDGE, 23, BuiltInBiomes.jungleEdge(BiomeKeys.JUNGLE_EDGE.location))
        register(BiomeKeys.DEEP_OCEAN, 24, BuiltInBiomes.ocean(BiomeKeys.DEEP_OCEAN.location, true))
        register(BiomeKeys.STONE_SHORE, 25, BuiltInBiomes.beach(
            BiomeKeys.STONE_SHORE.location,
            0.1F,
            0.8F,
            0.2F,
            0.3F,
            4159204,
            false,
            true
        ))
        register(BiomeKeys.SNOWY_BEACH, 26, BuiltInBiomes.beach(
            BiomeKeys.SNOWY_BEACH.location,
            0F,
            0.025F,
            0.05F,
            0.3F,
            4020182,
            true,
            false
        ))
        register(BiomeKeys.BIRCH_FOREST, 27, BuiltInBiomes.birchForest(BiomeKeys.BIRCH_FOREST.location, 0.1F, 0.2F))
        register(BiomeKeys.BIRCH_FOREST_HILLS, 28, BuiltInBiomes.birchForest(BiomeKeys.BIRCH_FOREST_HILLS.location, 0.45F, 0.3F))
        register(BiomeKeys.DARK_FOREST, 29, BuiltInBiomes.darkForest(BiomeKeys.DARK_FOREST.location, 0.1F, 0.2F))
        register(BiomeKeys.SNOWY_TAIGA, 30, BuiltInBiomes.taiga(BiomeKeys.SNOWY_TAIGA.location, 0.2F, 0.2F, true))
        register(BiomeKeys.SNOWY_TAIGA_HILLS, 31, BuiltInBiomes.taiga(BiomeKeys.SNOWY_TAIGA_HILLS.location, 0.45F, 0.3F, true))
        register(
            BiomeKeys.GIANT_TREE_TAIGA,
            32,
            BuiltInBiomes.giantTreeTaiga(BiomeKeys.GIANT_TREE_TAIGA.location, 0.2F, 0.2F, 0.3F)
        )
        register(
            BiomeKeys.GIANT_TREE_TAIGA_HILLS,
            33,
            BuiltInBiomes.giantTreeTaiga(BiomeKeys.GIANT_TREE_TAIGA_HILLS.location, 0.45F, 0.3F, 0.3F)
        )
        register(BiomeKeys.WOODED_MOUNTAINS, 34, BuiltInBiomes.mountain(BiomeKeys.WOODED_MOUNTAINS.location, 1F, 0.5F))
        register(BiomeKeys.SAVANNA, 35, BuiltInBiomes.savanna(BiomeKeys.SAVANNA.location, 0.125F, 0.05F, 1.2F))
        register(BiomeKeys.SAVANNA_PLATEAU, 36, BuiltInBiomes.savannaPlateau(BiomeKeys.SAVANNA_PLATEAU.location))
        register(BiomeKeys.BADLANDS, 37, BuiltInBiomes.badlands(BiomeKeys.BADLANDS.location, 0.1F, 0.2F))
        register(
            BiomeKeys.WOODED_BADLANDS_PLATEAU,
            38,
            BuiltInBiomes.badlands(BiomeKeys.WOODED_BADLANDS_PLATEAU.location, 1.5F, 0.025F)
        )
        register(BiomeKeys.BADLANDS_PLATEAU, 39, BuiltInBiomes.badlands(BiomeKeys.BADLANDS_PLATEAU.location, .5F, 0.025F))
        register(BiomeKeys.SMALL_END_ISLANDS, 40, BuiltInBiomes.end(BiomeKeys.SMALL_END_ISLANDS.location))
        register(BiomeKeys.END_MIDLANDS, 41, BuiltInBiomes.end(BiomeKeys.END_MIDLANDS.location))
        register(BiomeKeys.END_HIGHLANDS, 42, BuiltInBiomes.end(BiomeKeys.END_HIGHLANDS.location))
        register(BiomeKeys.END_BARRENS, 43, BuiltInBiomes.end(BiomeKeys.END_BARRENS.location))
        register(BiomeKeys.WARM_OCEAN, 44, BuiltInBiomes.warmOcean(BiomeKeys.WARM_OCEAN.location, false))
        register(BiomeKeys.LUKEWARM_OCEAN, 45, BuiltInBiomes.lukewarmOcean(BiomeKeys.LUKEWARM_OCEAN.location, false))
        register(BiomeKeys.COLD_OCEAN, 46, BuiltInBiomes.coldOcean(BiomeKeys.COLD_OCEAN.location, false))
        register(BiomeKeys.DEEP_WARM_OCEAN, 47, BuiltInBiomes.warmOcean(BiomeKeys.DEEP_WARM_OCEAN.location, true))
        register(BiomeKeys.DEEP_LUKEWARM_OCEAN, 48, BuiltInBiomes.lukewarmOcean(BiomeKeys.DEEP_LUKEWARM_OCEAN.location, true))
        register(BiomeKeys.DEEP_COLD_OCEAN, 49, BuiltInBiomes.coldOcean(BiomeKeys.DEEP_COLD_OCEAN.location, true))
        register(BiomeKeys.DEEP_FROZEN_OCEAN, 50, BuiltInBiomes.frozenOcean(BiomeKeys.DEEP_FROZEN_OCEAN.location, true))
        register(BiomeKeys.SUNFLOWER_PLAINS, 129, BuiltInBiomes.plains(BiomeKeys.SUNFLOWER_PLAINS.location))
        register(BiomeKeys.DESERT_LAKES, 130, BuiltInBiomes.desert(BiomeKeys.DESERT_LAKES.location, 0.225F, 0.25F))
        register(BiomeKeys.GRAVELLY_MOUNTAINS, 131, BuiltInBiomes.mountain(BiomeKeys.GRAVELLY_MOUNTAINS.location, 1F, 0.5F))
        register(BiomeKeys.FLOWER_FOREST, 132, BuiltInBiomes.flowerForest(BiomeKeys.FLOWER_FOREST.location))
        register(BiomeKeys.TAIGA_MOUNTAINS, 133, BuiltInBiomes.taiga(BiomeKeys.TAIGA_MOUNTAINS.location, 0.3F, 0.4F, false))
        register(BiomeKeys.SWAMP_HILLS, 134, BuiltInBiomes.swamp(BiomeKeys.SWAMP_HILLS.location, -0.1F, 0.3F))
        register(BiomeKeys.ICE_SPIKES, 140, BuiltInBiomes.tundra(BiomeKeys.ICE_SPIKES.location, 0.425F, 0.45000002F))
        register(BiomeKeys.MODIFIED_JUNGLE, 149, BuiltInBiomes.modifiedJungle(BiomeKeys.MODIFIED_JUNGLE.location))
        register(BiomeKeys.MODIFIED_JUNGLE_EDGE, 151, BuiltInBiomes.modifiedJungleEdge(BiomeKeys.MODIFIED_JUNGLE_EDGE.location))
        register(BiomeKeys.TALL_BIRCH_FOREST, 155, BuiltInBiomes.birchForest(BiomeKeys.TALL_BIRCH_FOREST.location, 0.2F, 0.4F))
        register(BiomeKeys.TALL_BIRCH_HILLS, 156, BuiltInBiomes.birchForest(BiomeKeys.TALL_BIRCH_HILLS.location, 0.55F, 0.5F))
        register(BiomeKeys.DARK_FOREST_HILLS, 157, BuiltInBiomes.darkForest(BiomeKeys.DARK_FOREST_HILLS.location, 0.2F, 0.4F))
        register(
            BiomeKeys.SNOWY_TAIGA_MOUNTAINS,
            158,
            BuiltInBiomes.taiga(BiomeKeys.SNOWY_TAIGA_MOUNTAINS.location, 0.3F, 0.4F, true)
        )
        register(
            BiomeKeys.GIANT_SPRUCE_TAIGA,
            160,
            BuiltInBiomes.giantTreeTaiga(BiomeKeys.GIANT_SPRUCE_TAIGA.location, 0.2F, 0.2F, 0.25F)
        )
        register(
            BiomeKeys.GIANT_SPRUCE_TAIGA_HILLS,
            161,
            BuiltInBiomes.giantTreeTaiga(BiomeKeys.GIANT_SPRUCE_TAIGA_HILLS.location, 0.2F, 0.2F, 0.25F)
        )
        register(
            BiomeKeys.MODIFIED_GRAVELLY_MOUNTAINS,
            162,
            BuiltInBiomes.mountain(BiomeKeys.MODIFIED_GRAVELLY_MOUNTAINS.location, 1F, 0.5F)
        )
        register(
            BiomeKeys.SHATTERED_SAVANNA,
            163,
            BuiltInBiomes.savanna(BiomeKeys.SHATTERED_SAVANNA.location, 0.3625F, 1.225F, 1.1F)
        )
        register(
            BiomeKeys.SHATTERED_SAVANNA_PLATEAU,
            164,
            BuiltInBiomes.savanna(BiomeKeys.SHATTERED_SAVANNA_PLATEAU.location, 1.05F, 1.2125001F, 1F)
        )
        register(BiomeKeys.ERODED_BADLANDS, 165, BuiltInBiomes.erodedBadlands(BiomeKeys.ERODED_BADLANDS.location))
        register(
            BiomeKeys.MODIFIED_WOODED_BADLANDS_PLATEAU,
            166,
            BuiltInBiomes.badlands(BiomeKeys.MODIFIED_WOODED_BADLANDS_PLATEAU.location, 0.45F, 0.3F)
        )
        register(
            BiomeKeys.MODIFIED_BADLANDS_PLATEAU,
            167,
            BuiltInBiomes.badlands(BiomeKeys.MODIFIED_BADLANDS_PLATEAU.location, 0.45F, 0.3F)
        )
        register(BiomeKeys.BAMBOO_JUNGLE, 168, BuiltInBiomes.bambooJungle(BiomeKeys.BAMBOO_JUNGLE.location))
        register(BiomeKeys.BAMBOO_JUNGLE_HILLS, 169, BuiltInBiomes.bambooJungleHills(BiomeKeys.BAMBOO_JUNGLE_HILLS.location))
        register(BiomeKeys.SOUL_SAND_VALLEY, 170, BuiltInBiomes.soulSandValley(BiomeKeys.SOUL_SAND_VALLEY.location))
        register(BiomeKeys.CRIMSON_FOREST, 171, BuiltInBiomes.crimsonForest(BiomeKeys.CRIMSON_FOREST.location))
        register(BiomeKeys.WARPED_FOREST, 172, BuiltInBiomes.warpedForest(BiomeKeys.WARPED_FOREST.location))
        register(BiomeKeys.BASALT_DELTAS, 173, BuiltInBiomes.basaltDeltas(BiomeKeys.BASALT_DELTAS.location))
        register(BiomeKeys.DRIPSTONE_CAVES, 174, BuiltInBiomes.dripstoneCaves(BiomeKeys.DRIPSTONE_CAVES.location))
        register(BiomeKeys.LUSH_CAVES, 175, BuiltInBiomes.lushCaves(BiomeKeys.LUSH_CAVES.location))
    }

    private fun register(key: ResourceKey<Biome>, id: Int, biome: KryptonBiome): KryptonBiome {
        ID_TO_KEY[id] = key
        return InternalRegistries.BIOME.register(id, key, biome)
    }
}

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
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys

object BiomeKeys {

    @JvmField val OCEAN = register("ocean")
    @JvmField val PLAINS = register("plains")
    @JvmField val DESERT = register("desert")
    @JvmField val MOUNTAINS = register("mountains")
    @JvmField val FOREST = register("forest")
    @JvmField val TAIGA = register("taiga")
    @JvmField val SWAMP = register("swamp")
    @JvmField val RIVER = register("river")
    @JvmField val NETHER_WASTES = register("nether_wastes")
    @JvmField val THE_END = register("the_end")
    @JvmField val FROZEN_OCEAN = register("frozen_ocean")
    @JvmField val FROZEN_RIVER = register("frozen_river")
    @JvmField val SNOWY_TUNDRA = register("snowy_tundra")
    @JvmField val SNOWY_MOUNTAINS = register("snowy_mountains")
    @JvmField val MUSHROOM_FIELDS = register("mushroom_fields")
    @JvmField val MUSHROOM_FIELD_SHORE = register("mushroom_field_shore")
    @JvmField val BEACH = register("beach")
    @JvmField val DESERT_HILLS = register("desert_hills")
    @JvmField val WOODED_HILLS = register("wooded_hills")
    @JvmField val TAIGA_HILLS = register("taiga_hills")
    @JvmField val MOUNTAIN_EDGE = register("mountain_edge")
    @JvmField val JUNGLE = register("jungle")
    @JvmField val JUNGLE_HILLS = register("jungle_hills")
    @JvmField val JUNGLE_EDGE = register("jungle_edge")
    @JvmField val DEEP_OCEAN = register("deep_ocean")
    @JvmField val STONE_SHORE = register("stone_shore")
    @JvmField val SNOWY_BEACH = register("snowy_beach")
    @JvmField val BIRCH_FOREST = register("birch_forest")
    @JvmField val BIRCH_FOREST_HILLS = register("birch_forest_hills")
    @JvmField val DARK_FOREST = register("dark_forest")
    @JvmField val SNOWY_TAIGA = register("snowy_taiga")
    @JvmField val SNOWY_TAIGA_HILLS = register("snowy_taiga_hills")
    @JvmField val GIANT_TREE_TAIGA = register("giant_tree_taiga")
    @JvmField val GIANT_TREE_TAIGA_HILLS = register("giant_tree_taiga_hills")
    @JvmField val WOODED_MOUNTAINS = register("wooded_mountains")
    @JvmField val SAVANNA = register("savanna")
    @JvmField val SAVANNA_PLATEAU = register("savanna_plateau")
    @JvmField val BADLANDS = register("badlands")
    @JvmField val WOODED_BADLANDS_PLATEAU = register("wooded_badlands_plateau")
    @JvmField val BADLANDS_PLATEAU = register("badlands_plateau")
    @JvmField val SMALL_END_ISLANDS = register("small_end_islands")
    @JvmField val END_MIDLANDS = register("end_midlands")
    @JvmField val END_HIGHLANDS = register("end_highlands")
    @JvmField val END_BARRENS = register("end_barrens")
    @JvmField val WARM_OCEAN = register("warm_ocean")
    @JvmField val LUKEWARM_OCEAN = register("lukewarm_ocean")
    @JvmField val COLD_OCEAN = register("cold_ocean")
    @JvmField val DEEP_WARM_OCEAN = register("deep_warm_ocean")
    @JvmField val DEEP_LUKEWARM_OCEAN = register("deep_lukewarm_ocean")
    @JvmField val DEEP_COLD_OCEAN = register("deep_cold_ocean")
    @JvmField val DEEP_FROZEN_OCEAN = register("deep_frozen_ocean")
    @JvmField val THE_VOID = register("the_void")
    @JvmField val SUNFLOWER_PLAINS = register("sunflower_plains")
    @JvmField val DESERT_LAKES = register("desert_lakes")
    @JvmField val GRAVELLY_MOUNTAINS = register("gravelly_mountains")
    @JvmField val FLOWER_FOREST = register("flower_forest")
    @JvmField val TAIGA_MOUNTAINS = register("taiga_mountains")
    @JvmField val SWAMP_HILLS = register("swamp_hills")
    @JvmField val ICE_SPIKES = register("ice_spikes")
    @JvmField val MODIFIED_JUNGLE = register("modified_jungle")
    @JvmField val MODIFIED_JUNGLE_EDGE = register("modified_jungle_edge")
    @JvmField val TALL_BIRCH_FOREST = register("tall_birch_forest")
    @JvmField val TALL_BIRCH_HILLS = register("tall_birch_hills")
    @JvmField val DARK_FOREST_HILLS = register("dark_forest_hills")
    @JvmField val SNOWY_TAIGA_MOUNTAINS = register("snowy_taiga_mountains")
    @JvmField val GIANT_SPRUCE_TAIGA = register("giant_spruce_taiga")
    @JvmField val GIANT_SPRUCE_TAIGA_HILLS = register("giant_spruce_taiga_hills")
    @JvmField val MODIFIED_GRAVELLY_MOUNTAINS = register("modified_gravelly_mountains")
    @JvmField val SHATTERED_SAVANNA = register("shattered_savanna")
    @JvmField val SHATTERED_SAVANNA_PLATEAU = register("shattered_savanna_plateau")
    @JvmField val ERODED_BADLANDS = register("eroded_badlands")
    @JvmField val MODIFIED_WOODED_BADLANDS_PLATEAU = register("modified_wooded_badlands_plateau")
    @JvmField val MODIFIED_BADLANDS_PLATEAU = register("modified_badlands_plateau")
    @JvmField val BAMBOO_JUNGLE = register("bamboo_jungle")
    @JvmField val BAMBOO_JUNGLE_HILLS = register("bamboo_jungle_hills")
    @JvmField val SOUL_SAND_VALLEY = register("soul_sand_valley")
    @JvmField val CRIMSON_FOREST = register("crimson_forest")
    @JvmField val WARPED_FOREST = register("warped_forest")
    @JvmField val BASALT_DELTAS = register("basalt_deltas")
    @JvmField val DRIPSTONE_CAVES = register("dripstone_caves")
    @JvmField val LUSH_CAVES = register("lush_caves")

    @JvmStatic
    private fun register(key: String) = ResourceKey.of(ResourceKeys.BIOME, Key.key(key))
}

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

    @JvmField val THE_VOID = register("the_void")
    @JvmField val PLAINS = register("plains")
    @JvmField val SUNFLOWER_PLAINS = register("sunflower_plains")
    @JvmField val SNOWY_PLAINS = register("snowy_plains")
    @JvmField val ICE_SPIKES = register("ice_spikes")
    @JvmField val DESERT = register("desert")
    @JvmField val SWAMP = register("swamp")
    @JvmField val FOREST = register("forest")
    @JvmField val FLOWER_FOREST = register("flower_forest")
    @JvmField val BIRCH_FOREST = register("birch_forest")
    @JvmField val DARK_FOREST = register("dark_forest")
    @JvmField val OLD_GROWTH_BIRCH_FOREST = register("old_growth_birch_forest")
    @JvmField val OLD_GROWTH_PINE_TAIGA = register("old_growth_pine_taiga")
    @JvmField val OLD_GROWTH_SPRUCE_TAIGA = register("old_growth_spruce_taiga")
    @JvmField val TAIGA = register("taiga")
    @JvmField val SNOWY_TAIGA = register("snowy_taiga")
    @JvmField val SAVANNA = register("savanna")
    @JvmField val SAVANNA_PLATEAU = register("savanna_plateau")
    @JvmField val WINDSWEPT_HILLS = register("windswept_hills")
    @JvmField val WINDSWEPT_GRAVELLY_HILLS = register("windswept_gravelly_hills")
    @JvmField val WINDSWEPT_FOREST = register("windswept_forest")
    @JvmField val WINDSWEPT_SAVANNA = register("windswept_savanna")
    @JvmField val JUNGLE = register("jungle")
    @JvmField val SPARSE_JUNGLE = register("sparse_jungle")
    @JvmField val BAMBOO_JUNGLE = register("bamboo_jungle")
    @JvmField val BADLANDS = register("badlands")
    @JvmField val ERODED_BADLANDS = register("eroded_badlands")
    @JvmField val WOODED_BADLANDS = register("wooded_badlands")
    @JvmField val MEADOW = register("meadow")
    @JvmField val GROVE = register("grove")
    @JvmField val SNOWY_SLOPES = register("snowy_slopes")
    @JvmField val FROZEN_PEAKS = register("frozen_peaks")
    @JvmField val JAGGED_PEAKS = register("jagged_peaks")
    @JvmField val STONY_PEAKS = register("stony_peaks")
    @JvmField val RIVER = register("river")
    @JvmField val FROZEN_RIVER = register("frozen_river")
    @JvmField val BEACH = register("beach")
    @JvmField val SNOWY_BEACH = register("snowy_beach")
    @JvmField val STONY_SHORE = register("stony_shore")
    @JvmField val WARM_OCEAN = register("warm_ocean")
    @JvmField val LUKEWARM_OCEAN = register("lukewarm_ocean")
    @JvmField val DEEP_LUKEWARM_OCEAN = register("deep_lukewarm_ocean")
    @JvmField val OCEAN = register("ocean")
    @JvmField val DEEP_OCEAN = register("deep_ocean")
    @JvmField val COLD_OCEAN = register("cold_ocean")
    @JvmField val DEEP_COLD_OCEAN = register("deep_cold_ocean")
    @JvmField val FROZEN_OCEAN = register("frozen_ocean")
    @JvmField val DEEP_FROZEN_OCEAN = register("deep_frozen_ocean")
    @JvmField val MUSHROOM_FIELDS = register("mushroom_fields")
    @JvmField val DRIPSTONE_CAVES = register("dripstone_caves")
    @JvmField val LUSH_CAVES = register("lush_caves")
    @JvmField val NETHER_WASTES = register("nether_wastes")
    @JvmField val WARPED_FOREST = register("warped_forest")
    @JvmField val CRIMSON_FOREST = register("crimson_forest")
    @JvmField val SOUL_SAND_VALLEY = register("soul_sand_valley")
    @JvmField val BASALT_DELTAS = register("basalt_deltas")
    @JvmField val THE_END = register("the_end")
    @JvmField val END_HIGHLANDS = register("end_highlands")
    @JvmField val END_MIDLANDS = register("end_midlands")
    @JvmField val SMALL_END_ISLANDS = register("small_end_islands")
    @JvmField val END_BARRENS = register("end_barrens")

    @JvmStatic
    private fun register(key: String) = ResourceKey.of(ResourceKeys.BIOME, Key.key(key))
}

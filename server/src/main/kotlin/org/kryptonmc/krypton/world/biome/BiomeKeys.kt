/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.api.world.biome.Biome

object BiomeKeys {

    @JvmField
    val THE_VOID: ResourceKey<Biome> = register("the_void")
    @JvmField
    val PLAINS: ResourceKey<Biome> = register("plains")
    @JvmField
    val SUNFLOWER_PLAINS: ResourceKey<Biome> = register("sunflower_plains")
    @JvmField
    val SNOWY_PLAINS: ResourceKey<Biome> = register("snowy_plains")
    @JvmField
    val ICE_SPIKES: ResourceKey<Biome> = register("ice_spikes")
    @JvmField
    val DESERT: ResourceKey<Biome> = register("desert")
    @JvmField
    val SWAMP: ResourceKey<Biome> = register("swamp")
    @JvmField
    val MANGROVE_SWAMP: ResourceKey<Biome> = register("mangrove_swamp")
    @JvmField
    val FOREST: ResourceKey<Biome> = register("forest")
    @JvmField
    val FLOWER_FOREST: ResourceKey<Biome> = register("flower_forest")
    @JvmField
    val BIRCH_FOREST: ResourceKey<Biome> = register("birch_forest")
    @JvmField
    val DARK_FOREST: ResourceKey<Biome> = register("dark_forest")
    @JvmField
    val OLD_GROWTH_BIRCH_FOREST: ResourceKey<Biome> = register("old_growth_birch_forest")
    @JvmField
    val OLD_GROWTH_PINE_TAIGA: ResourceKey<Biome> = register("old_growth_pine_taiga")
    @JvmField
    val OLD_GROWTH_SPRUCE_TAIGA: ResourceKey<Biome> = register("old_growth_spruce_taiga")
    @JvmField
    val TAIGA: ResourceKey<Biome> = register("taiga")
    @JvmField
    val SNOWY_TAIGA: ResourceKey<Biome> = register("snowy_taiga")
    @JvmField
    val SAVANNA: ResourceKey<Biome> = register("savanna")
    @JvmField
    val SAVANNA_PLATEAU: ResourceKey<Biome> = register("savanna_plateau")
    @JvmField
    val WINDSWEPT_HILLS: ResourceKey<Biome> = register("windswept_hills")
    @JvmField
    val WINDSWEPT_GRAVELLY_HILLS: ResourceKey<Biome> = register("windswept_gravelly_hills")
    @JvmField
    val WINDSWEPT_FOREST: ResourceKey<Biome> = register("windswept_forest")
    @JvmField
    val WINDSWEPT_SAVANNA: ResourceKey<Biome> = register("windswept_savanna")
    @JvmField
    val JUNGLE: ResourceKey<Biome> = register("jungle")
    @JvmField
    val SPARSE_JUNGLE: ResourceKey<Biome> = register("sparse_jungle")
    @JvmField
    val BAMBOO_JUNGLE: ResourceKey<Biome> = register("bamboo_jungle")
    @JvmField
    val BADLANDS: ResourceKey<Biome> = register("badlands")
    @JvmField
    val ERODED_BADLANDS: ResourceKey<Biome> = register("eroded_badlands")
    @JvmField
    val WOODED_BADLANDS: ResourceKey<Biome> = register("wooded_badlands")
    @JvmField
    val MEADOW: ResourceKey<Biome> = register("meadow")
    @JvmField
    val GROVE: ResourceKey<Biome> = register("grove")
    @JvmField
    val SNOWY_SLOPES: ResourceKey<Biome> = register("snowy_slopes")
    @JvmField
    val FROZEN_PEAKS: ResourceKey<Biome> = register("frozen_peaks")
    @JvmField
    val JAGGED_PEAKS: ResourceKey<Biome> = register("jagged_peaks")
    @JvmField
    val STONY_PEAKS: ResourceKey<Biome> = register("stony_peaks")
    @JvmField
    val RIVER: ResourceKey<Biome> = register("river")
    @JvmField
    val FROZEN_RIVER: ResourceKey<Biome> = register("frozen_river")
    @JvmField
    val BEACH: ResourceKey<Biome> = register("beach")
    @JvmField
    val SNOWY_BEACH: ResourceKey<Biome> = register("snowy_beach")
    @JvmField
    val STONY_SHORE: ResourceKey<Biome> = register("stony_shore")
    @JvmField
    val WARM_OCEAN: ResourceKey<Biome> = register("warm_ocean")
    @JvmField
    val LUKEWARM_OCEAN: ResourceKey<Biome> = register("lukewarm_ocean")
    @JvmField
    val DEEP_LUKEWARM_OCEAN: ResourceKey<Biome> = register("deep_lukewarm_ocean")
    @JvmField
    val OCEAN: ResourceKey<Biome> = register("ocean")
    @JvmField
    val DEEP_OCEAN: ResourceKey<Biome> = register("deep_ocean")
    @JvmField
    val COLD_OCEAN: ResourceKey<Biome> = register("cold_ocean")
    @JvmField
    val DEEP_COLD_OCEAN: ResourceKey<Biome> = register("deep_cold_ocean")
    @JvmField
    val FROZEN_OCEAN: ResourceKey<Biome> = register("frozen_ocean")
    @JvmField
    val DEEP_FROZEN_OCEAN: ResourceKey<Biome> = register("deep_frozen_ocean")
    @JvmField
    val MUSHROOM_FIELDS: ResourceKey<Biome> = register("mushroom_fields")
    @JvmField
    val DRIPSTONE_CAVES: ResourceKey<Biome> = register("dripstone_caves")
    @JvmField
    val LUSH_CAVES: ResourceKey<Biome> = register("lush_caves")
    @JvmField
    val DEEP_DARK: ResourceKey<Biome> = register("deep_dark")
    @JvmField
    val NETHER_WASTES: ResourceKey<Biome> = register("nether_wastes")
    @JvmField
    val WARPED_FOREST: ResourceKey<Biome> = register("warped_forest")
    @JvmField
    val CRIMSON_FOREST: ResourceKey<Biome> = register("crimson_forest")
    @JvmField
    val SOUL_SAND_VALLEY: ResourceKey<Biome> = register("soul_sand_valley")
    @JvmField
    val BASALT_DELTAS: ResourceKey<Biome> = register("basalt_deltas")
    @JvmField
    val THE_END: ResourceKey<Biome> = register("the_end")
    @JvmField
    val END_HIGHLANDS: ResourceKey<Biome> = register("end_highlands")
    @JvmField
    val END_MIDLANDS: ResourceKey<Biome> = register("end_midlands")
    @JvmField
    val SMALL_END_ISLANDS: ResourceKey<Biome> = register("small_end_islands")
    @JvmField
    val END_BARRENS: ResourceKey<Biome> = register("end_barrens")

    @JvmStatic
    private fun register(key: String): ResourceKey<Biome> = ResourceKey.of(ResourceKeys.BIOME, Key.key(key))
}

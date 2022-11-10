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
import org.kryptonmc.krypton.resource.KryptonResourceKey

object BiomeKeys {

    @JvmField
    val THE_VOID: ResourceKey<Biome> = create("the_void")
    @JvmField
    val PLAINS: ResourceKey<Biome> = create("plains")
    @JvmField
    val SUNFLOWER_PLAINS: ResourceKey<Biome> = create("sunflower_plains")
    @JvmField
    val SNOWY_PLAINS: ResourceKey<Biome> = create("snowy_plains")
    @JvmField
    val ICE_SPIKES: ResourceKey<Biome> = create("ice_spikes")
    @JvmField
    val DESERT: ResourceKey<Biome> = create("desert")
    @JvmField
    val SWAMP: ResourceKey<Biome> = create("swamp")
    @JvmField
    val MANGROVE_SWAMP: ResourceKey<Biome> = create("mangrove_swamp")
    @JvmField
    val FOREST: ResourceKey<Biome> = create("forest")
    @JvmField
    val FLOWER_FOREST: ResourceKey<Biome> = create("flower_forest")
    @JvmField
    val BIRCH_FOREST: ResourceKey<Biome> = create("birch_forest")
    @JvmField
    val DARK_FOREST: ResourceKey<Biome> = create("dark_forest")
    @JvmField
    val OLD_GROWTH_BIRCH_FOREST: ResourceKey<Biome> = create("old_growth_birch_forest")
    @JvmField
    val OLD_GROWTH_PINE_TAIGA: ResourceKey<Biome> = create("old_growth_pine_taiga")
    @JvmField
    val OLD_GROWTH_SPRUCE_TAIGA: ResourceKey<Biome> = create("old_growth_spruce_taiga")
    @JvmField
    val TAIGA: ResourceKey<Biome> = create("taiga")
    @JvmField
    val SNOWY_TAIGA: ResourceKey<Biome> = create("snowy_taiga")
    @JvmField
    val SAVANNA: ResourceKey<Biome> = create("savanna")
    @JvmField
    val SAVANNA_PLATEAU: ResourceKey<Biome> = create("savanna_plateau")
    @JvmField
    val WINDSWEPT_HILLS: ResourceKey<Biome> = create("windswept_hills")
    @JvmField
    val WINDSWEPT_GRAVELLY_HILLS: ResourceKey<Biome> = create("windswept_gravelly_hills")
    @JvmField
    val WINDSWEPT_FOREST: ResourceKey<Biome> = create("windswept_forest")
    @JvmField
    val WINDSWEPT_SAVANNA: ResourceKey<Biome> = create("windswept_savanna")
    @JvmField
    val JUNGLE: ResourceKey<Biome> = create("jungle")
    @JvmField
    val SPARSE_JUNGLE: ResourceKey<Biome> = create("sparse_jungle")
    @JvmField
    val BAMBOO_JUNGLE: ResourceKey<Biome> = create("bamboo_jungle")
    @JvmField
    val BADLANDS: ResourceKey<Biome> = create("badlands")
    @JvmField
    val ERODED_BADLANDS: ResourceKey<Biome> = create("eroded_badlands")
    @JvmField
    val WOODED_BADLANDS: ResourceKey<Biome> = create("wooded_badlands")
    @JvmField
    val MEADOW: ResourceKey<Biome> = create("meadow")
    @JvmField
    val GROVE: ResourceKey<Biome> = create("grove")
    @JvmField
    val SNOWY_SLOPES: ResourceKey<Biome> = create("snowy_slopes")
    @JvmField
    val FROZEN_PEAKS: ResourceKey<Biome> = create("frozen_peaks")
    @JvmField
    val JAGGED_PEAKS: ResourceKey<Biome> = create("jagged_peaks")
    @JvmField
    val STONY_PEAKS: ResourceKey<Biome> = create("stony_peaks")
    @JvmField
    val RIVER: ResourceKey<Biome> = create("river")
    @JvmField
    val FROZEN_RIVER: ResourceKey<Biome> = create("frozen_river")
    @JvmField
    val BEACH: ResourceKey<Biome> = create("beach")
    @JvmField
    val SNOWY_BEACH: ResourceKey<Biome> = create("snowy_beach")
    @JvmField
    val STONY_SHORE: ResourceKey<Biome> = create("stony_shore")
    @JvmField
    val WARM_OCEAN: ResourceKey<Biome> = create("warm_ocean")
    @JvmField
    val LUKEWARM_OCEAN: ResourceKey<Biome> = create("lukewarm_ocean")
    @JvmField
    val DEEP_LUKEWARM_OCEAN: ResourceKey<Biome> = create("deep_lukewarm_ocean")
    @JvmField
    val OCEAN: ResourceKey<Biome> = create("ocean")
    @JvmField
    val DEEP_OCEAN: ResourceKey<Biome> = create("deep_ocean")
    @JvmField
    val COLD_OCEAN: ResourceKey<Biome> = create("cold_ocean")
    @JvmField
    val DEEP_COLD_OCEAN: ResourceKey<Biome> = create("deep_cold_ocean")
    @JvmField
    val FROZEN_OCEAN: ResourceKey<Biome> = create("frozen_ocean")
    @JvmField
    val DEEP_FROZEN_OCEAN: ResourceKey<Biome> = create("deep_frozen_ocean")
    @JvmField
    val MUSHROOM_FIELDS: ResourceKey<Biome> = create("mushroom_fields")
    @JvmField
    val DRIPSTONE_CAVES: ResourceKey<Biome> = create("dripstone_caves")
    @JvmField
    val LUSH_CAVES: ResourceKey<Biome> = create("lush_caves")
    @JvmField
    val DEEP_DARK: ResourceKey<Biome> = create("deep_dark")
    @JvmField
    val NETHER_WASTES: ResourceKey<Biome> = create("nether_wastes")
    @JvmField
    val WARPED_FOREST: ResourceKey<Biome> = create("warped_forest")
    @JvmField
    val CRIMSON_FOREST: ResourceKey<Biome> = create("crimson_forest")
    @JvmField
    val SOUL_SAND_VALLEY: ResourceKey<Biome> = create("soul_sand_valley")
    @JvmField
    val BASALT_DELTAS: ResourceKey<Biome> = create("basalt_deltas")
    @JvmField
    val THE_END: ResourceKey<Biome> = create("the_end")
    @JvmField
    val END_HIGHLANDS: ResourceKey<Biome> = create("end_highlands")
    @JvmField
    val END_MIDLANDS: ResourceKey<Biome> = create("end_midlands")
    @JvmField
    val SMALL_END_ISLANDS: ResourceKey<Biome> = create("small_end_islands")
    @JvmField
    val END_BARRENS: ResourceKey<Biome> = create("end_barrens")

    @JvmStatic
    private fun create(key: String): ResourceKey<Biome> = KryptonResourceKey.of(ResourceKeys.BIOME, Key.key(key))
}

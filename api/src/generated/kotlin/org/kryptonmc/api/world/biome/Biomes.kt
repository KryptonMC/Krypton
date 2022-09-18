/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(Biome::class)
public object Biomes {

    // @formatter:off
    @JvmField
    public val THE_VOID: Biome = get("the_void")
    @JvmField
    public val PLAINS: Biome = get("plains")
    @JvmField
    public val SUNFLOWER_PLAINS: Biome = get("sunflower_plains")
    @JvmField
    public val SNOWY_PLAINS: Biome = get("snowy_plains")
    @JvmField
    public val ICE_SPIKES: Biome = get("ice_spikes")
    @JvmField
    public val DESERT: Biome = get("desert")
    @JvmField
    public val SWAMP: Biome = get("swamp")
    @JvmField
    public val MANGROVE_SWAMP: Biome = get("mangrove_swamp")
    @JvmField
    public val FOREST: Biome = get("forest")
    @JvmField
    public val FLOWER_FOREST: Biome = get("flower_forest")
    @JvmField
    public val BIRCH_FOREST: Biome = get("birch_forest")
    @JvmField
    public val DARK_FOREST: Biome = get("dark_forest")
    @JvmField
    public val OLD_GROWTH_BIRCH_FOREST: Biome = get("old_growth_birch_forest")
    @JvmField
    public val OLD_GROWTH_PINE_TAIGA: Biome = get("old_growth_pine_taiga")
    @JvmField
    public val OLD_GROWTH_SPRUCE_TAIGA: Biome = get("old_growth_spruce_taiga")
    @JvmField
    public val TAIGA: Biome = get("taiga")
    @JvmField
    public val SNOWY_TAIGA: Biome = get("snowy_taiga")
    @JvmField
    public val SAVANNA: Biome = get("savanna")
    @JvmField
    public val SAVANNA_PLATEAU: Biome = get("savanna_plateau")
    @JvmField
    public val WINDSWEPT_HILLS: Biome = get("windswept_hills")
    @JvmField
    public val WINDSWEPT_GRAVELLY_HILLS: Biome = get("windswept_gravelly_hills")
    @JvmField
    public val WINDSWEPT_FOREST: Biome = get("windswept_forest")
    @JvmField
    public val WINDSWEPT_SAVANNA: Biome = get("windswept_savanna")
    @JvmField
    public val JUNGLE: Biome = get("jungle")
    @JvmField
    public val SPARSE_JUNGLE: Biome = get("sparse_jungle")
    @JvmField
    public val BAMBOO_JUNGLE: Biome = get("bamboo_jungle")
    @JvmField
    public val BADLANDS: Biome = get("badlands")
    @JvmField
    public val ERODED_BADLANDS: Biome = get("eroded_badlands")
    @JvmField
    public val WOODED_BADLANDS: Biome = get("wooded_badlands")
    @JvmField
    public val MEADOW: Biome = get("meadow")
    @JvmField
    public val GROVE: Biome = get("grove")
    @JvmField
    public val SNOWY_SLOPES: Biome = get("snowy_slopes")
    @JvmField
    public val FROZEN_PEAKS: Biome = get("frozen_peaks")
    @JvmField
    public val JAGGED_PEAKS: Biome = get("jagged_peaks")
    @JvmField
    public val STONY_PEAKS: Biome = get("stony_peaks")
    @JvmField
    public val RIVER: Biome = get("river")
    @JvmField
    public val FROZEN_RIVER: Biome = get("frozen_river")
    @JvmField
    public val BEACH: Biome = get("beach")
    @JvmField
    public val SNOWY_BEACH: Biome = get("snowy_beach")
    @JvmField
    public val STONY_SHORE: Biome = get("stony_shore")
    @JvmField
    public val WARM_OCEAN: Biome = get("warm_ocean")
    @JvmField
    public val LUKEWARM_OCEAN: Biome = get("lukewarm_ocean")
    @JvmField
    public val DEEP_LUKEWARM_OCEAN: Biome = get("deep_lukewarm_ocean")
    @JvmField
    public val OCEAN: Biome = get("ocean")
    @JvmField
    public val DEEP_OCEAN: Biome = get("deep_ocean")
    @JvmField
    public val COLD_OCEAN: Biome = get("cold_ocean")
    @JvmField
    public val DEEP_COLD_OCEAN: Biome = get("deep_cold_ocean")
    @JvmField
    public val FROZEN_OCEAN: Biome = get("frozen_ocean")
    @JvmField
    public val DEEP_FROZEN_OCEAN: Biome = get("deep_frozen_ocean")
    @JvmField
    public val MUSHROOM_FIELDS: Biome = get("mushroom_fields")
    @JvmField
    public val DRIPSTONE_CAVES: Biome = get("dripstone_caves")
    @JvmField
    public val LUSH_CAVES: Biome = get("lush_caves")
    @JvmField
    public val DEEP_DARK: Biome = get("deep_dark")
    @JvmField
    public val NETHER_WASTES: Biome = get("nether_wastes")
    @JvmField
    public val WARPED_FOREST: Biome = get("warped_forest")
    @JvmField
    public val CRIMSON_FOREST: Biome = get("crimson_forest")
    @JvmField
    public val SOUL_SAND_VALLEY: Biome = get("soul_sand_valley")
    @JvmField
    public val BASALT_DELTAS: Biome = get("basalt_deltas")
    @JvmField
    public val THE_END: Biome = get("the_end")
    @JvmField
    public val END_HIGHLANDS: Biome = get("end_highlands")
    @JvmField
    public val END_MIDLANDS: Biome = get("end_midlands")
    @JvmField
    public val SMALL_END_ISLANDS: Biome = get("small_end_islands")
    @JvmField
    public val END_BARRENS: Biome = get("end_barrens")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): Biome = Registries.BIOME.get(Key.key(key))!!
}

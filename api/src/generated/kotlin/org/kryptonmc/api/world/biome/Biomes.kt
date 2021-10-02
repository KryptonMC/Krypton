/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
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
@Suppress("UndocumentedPublicProperty", "LargeClass")
@Catalogue(Biome::class)
public object Biomes {

    // @formatter:off
    @JvmField public val OCEAN: Biome = get("ocean")
    @JvmField public val PLAINS: Biome = get("plains")
    @JvmField public val DESERT: Biome = get("desert")
    @JvmField public val MOUNTAINS: Biome = get("mountains")
    @JvmField public val FOREST: Biome = get("forest")
    @JvmField public val TAIGA: Biome = get("taiga")
    @JvmField public val SWAMP: Biome = get("swamp")
    @JvmField public val RIVER: Biome = get("river")
    @JvmField public val NETHER_WASTES: Biome = get("nether_wastes")
    @JvmField public val THE_END: Biome = get("the_end")
    @JvmField public val FROZEN_OCEAN: Biome = get("frozen_ocean")
    @JvmField public val FROZEN_RIVER: Biome = get("frozen_river")
    @JvmField public val SNOWY_TUNDRA: Biome = get("snowy_tundra")
    @JvmField public val SNOWY_MOUNTAINS: Biome = get("snowy_mountains")
    @JvmField public val MUSHROOM_FIELDS: Biome = get("mushroom_fields")
    @JvmField public val MUSHROOM_FIELD_SHORE: Biome = get("mushroom_field_shore")
    @JvmField public val BEACH: Biome = get("beach")
    @JvmField public val DESERT_HILLS: Biome = get("desert_hills")
    @JvmField public val WOODED_HILLS: Biome = get("wooded_hills")
    @JvmField public val TAIGA_HILLS: Biome = get("taiga_hills")
    @JvmField public val MOUNTAIN_EDGE: Biome = get("mountain_edge")
    @JvmField public val JUNGLE: Biome = get("jungle")
    @JvmField public val JUNGLE_HILLS: Biome = get("jungle_hills")
    @JvmField public val JUNGLE_EDGE: Biome = get("jungle_edge")
    @JvmField public val DEEP_OCEAN: Biome = get("deep_ocean")
    @JvmField public val STONE_SHORE: Biome = get("stone_shore")
    @JvmField public val SNOWY_BEACH: Biome = get("snowy_beach")
    @JvmField public val BIRCH_FOREST: Biome = get("birch_forest")
    @JvmField public val BIRCH_FOREST_HILLS: Biome = get("birch_forest_hills")
    @JvmField public val DARK_FOREST: Biome = get("dark_forest")
    @JvmField public val SNOWY_TAIGA: Biome = get("snowy_taiga")
    @JvmField public val SNOWY_TAIGA_HILLS: Biome = get("snowy_taiga_hills")
    @JvmField public val GIANT_TREE_TAIGA: Biome = get("giant_tree_taiga")
    @JvmField public val GIANT_TREE_TAIGA_HILLS: Biome = get("giant_tree_taiga_hills")
    @JvmField public val WOODED_MOUNTAINS: Biome = get("wooded_mountains")
    @JvmField public val SAVANNA: Biome = get("savanna")
    @JvmField public val SAVANNA_PLATEAU: Biome = get("savanna_plateau")
    @JvmField public val BADLANDS: Biome = get("badlands")
    @JvmField public val WOODED_BADLANDS_PLATEAU: Biome = get("wooded_badlands_plateau")
    @JvmField public val BADLANDS_PLATEAU: Biome = get("badlands_plateau")
    @JvmField public val SMALL_END_ISLANDS: Biome = get("small_end_islands")
    @JvmField public val END_MIDLANDS: Biome = get("end_midlands")
    @JvmField public val END_HIGHLANDS: Biome = get("end_highlands")
    @JvmField public val END_BARRENS: Biome = get("end_barrens")
    @JvmField public val WARM_OCEAN: Biome = get("warm_ocean")
    @JvmField public val LUKEWARM_OCEAN: Biome = get("lukewarm_ocean")
    @JvmField public val COLD_OCEAN: Biome = get("cold_ocean")
    @JvmField public val DEEP_WARM_OCEAN: Biome = get("deep_warm_ocean")
    @JvmField public val DEEP_LUKEWARM_OCEAN: Biome = get("deep_lukewarm_ocean")
    @JvmField public val DEEP_COLD_OCEAN: Biome = get("deep_cold_ocean")
    @JvmField public val DEEP_FROZEN_OCEAN: Biome = get("deep_frozen_ocean")
    @JvmField public val THE_VOID: Biome = get("the_void")
    @JvmField public val SUNFLOWER_PLAINS: Biome = get("sunflower_plains")
    @JvmField public val DESERT_LAKES: Biome = get("desert_lakes")
    @JvmField public val GRAVELLY_MOUNTAINS: Biome = get("gravelly_mountains")
    @JvmField public val FLOWER_FOREST: Biome = get("flower_forest")
    @JvmField public val TAIGA_MOUNTAINS: Biome = get("taiga_mountains")
    @JvmField public val SWAMP_HILLS: Biome = get("swamp_hills")
    @JvmField public val ICE_SPIKES: Biome = get("ice_spikes")
    @JvmField public val MODIFIED_JUNGLE: Biome = get("modified_jungle")
    @JvmField public val MODIFIED_JUNGLE_EDGE: Biome = get("modified_jungle_edge")
    @JvmField public val TALL_BIRCH_FOREST: Biome = get("tall_birch_forest")
    @JvmField public val TALL_BIRCH_HILLS: Biome = get("tall_birch_hills")
    @JvmField public val DARK_FOREST_HILLS: Biome = get("dark_forest_hills")
    @JvmField public val SNOWY_TAIGA_MOUNTAINS: Biome = get("snowy_taiga_mountains")
    @JvmField public val GIANT_SPRUCE_TAIGA: Biome = get("giant_spruce_taiga")
    @JvmField public val GIANT_SPRUCE_TAIGA_HILLS: Biome = get("giant_spruce_taiga_hills")
    @JvmField public val MODIFIED_GRAVELLY_MOUNTAINS: Biome = get("modified_gravelly_mountains")
    @JvmField public val SHATTERED_SAVANNA: Biome = get("shattered_savanna")
    @JvmField public val SHATTERED_SAVANNA_PLATEAU: Biome = get("shattered_savanna_plateau")
    @JvmField public val ERODED_BADLANDS: Biome = get("eroded_badlands")
    @JvmField public val MODIFIED_WOODED_BADLANDS_PLATEAU: Biome = get("modified_wooded_badlands_plateau")
    @JvmField public val MODIFIED_BADLANDS_PLATEAU: Biome = get("modified_badlands_plateau")
    @JvmField public val BAMBOO_JUNGLE: Biome = get("bamboo_jungle")
    @JvmField public val BAMBOO_JUNGLE_HILLS: Biome = get("bamboo_jungle_hills")
    @JvmField public val SOUL_SAND_VALLEY: Biome = get("soul_sand_valley")
    @JvmField public val CRIMSON_FOREST: Biome = get("crimson_forest")
    @JvmField public val WARPED_FOREST: Biome = get("warped_forest")
    @JvmField public val BASALT_DELTAS: Biome = get("basalt_deltas")
    @JvmField public val DRIPSTONE_CAVES: Biome = get("dripstone_caves")
    @JvmField public val LUSH_CAVES: Biome = get("lush_caves")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): Biome = Registries.BIOME[Key.key(key)]!!
}

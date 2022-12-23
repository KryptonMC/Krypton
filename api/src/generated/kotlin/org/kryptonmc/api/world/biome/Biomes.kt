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
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(Biome::class)
public object Biomes {

    // @formatter:off
    @JvmField
    public val THE_VOID: RegistryReference<Biome> = of("the_void")
    @JvmField
    public val PLAINS: RegistryReference<Biome> = of("plains")
    @JvmField
    public val SUNFLOWER_PLAINS: RegistryReference<Biome> = of("sunflower_plains")
    @JvmField
    public val SNOWY_PLAINS: RegistryReference<Biome> = of("snowy_plains")
    @JvmField
    public val ICE_SPIKES: RegistryReference<Biome> = of("ice_spikes")
    @JvmField
    public val DESERT: RegistryReference<Biome> = of("desert")
    @JvmField
    public val SWAMP: RegistryReference<Biome> = of("swamp")
    @JvmField
    public val MANGROVE_SWAMP: RegistryReference<Biome> = of("mangrove_swamp")
    @JvmField
    public val FOREST: RegistryReference<Biome> = of("forest")
    @JvmField
    public val FLOWER_FOREST: RegistryReference<Biome> = of("flower_forest")
    @JvmField
    public val BIRCH_FOREST: RegistryReference<Biome> = of("birch_forest")
    @JvmField
    public val DARK_FOREST: RegistryReference<Biome> = of("dark_forest")
    @JvmField
    public val OLD_GROWTH_BIRCH_FOREST: RegistryReference<Biome> = of("old_growth_birch_forest")
    @JvmField
    public val OLD_GROWTH_PINE_TAIGA: RegistryReference<Biome> = of("old_growth_pine_taiga")
    @JvmField
    public val OLD_GROWTH_SPRUCE_TAIGA: RegistryReference<Biome> = of("old_growth_spruce_taiga")
    @JvmField
    public val TAIGA: RegistryReference<Biome> = of("taiga")
    @JvmField
    public val SNOWY_TAIGA: RegistryReference<Biome> = of("snowy_taiga")
    @JvmField
    public val SAVANNA: RegistryReference<Biome> = of("savanna")
    @JvmField
    public val SAVANNA_PLATEAU: RegistryReference<Biome> = of("savanna_plateau")
    @JvmField
    public val WINDSWEPT_HILLS: RegistryReference<Biome> = of("windswept_hills")
    @JvmField
    public val WINDSWEPT_GRAVELLY_HILLS: RegistryReference<Biome> = of("windswept_gravelly_hills")
    @JvmField
    public val WINDSWEPT_FOREST: RegistryReference<Biome> = of("windswept_forest")
    @JvmField
    public val WINDSWEPT_SAVANNA: RegistryReference<Biome> = of("windswept_savanna")
    @JvmField
    public val JUNGLE: RegistryReference<Biome> = of("jungle")
    @JvmField
    public val SPARSE_JUNGLE: RegistryReference<Biome> = of("sparse_jungle")
    @JvmField
    public val BAMBOO_JUNGLE: RegistryReference<Biome> = of("bamboo_jungle")
    @JvmField
    public val BADLANDS: RegistryReference<Biome> = of("badlands")
    @JvmField
    public val ERODED_BADLANDS: RegistryReference<Biome> = of("eroded_badlands")
    @JvmField
    public val WOODED_BADLANDS: RegistryReference<Biome> = of("wooded_badlands")
    @JvmField
    public val MEADOW: RegistryReference<Biome> = of("meadow")
    @JvmField
    public val GROVE: RegistryReference<Biome> = of("grove")
    @JvmField
    public val SNOWY_SLOPES: RegistryReference<Biome> = of("snowy_slopes")
    @JvmField
    public val FROZEN_PEAKS: RegistryReference<Biome> = of("frozen_peaks")
    @JvmField
    public val JAGGED_PEAKS: RegistryReference<Biome> = of("jagged_peaks")
    @JvmField
    public val STONY_PEAKS: RegistryReference<Biome> = of("stony_peaks")
    @JvmField
    public val RIVER: RegistryReference<Biome> = of("river")
    @JvmField
    public val FROZEN_RIVER: RegistryReference<Biome> = of("frozen_river")
    @JvmField
    public val BEACH: RegistryReference<Biome> = of("beach")
    @JvmField
    public val SNOWY_BEACH: RegistryReference<Biome> = of("snowy_beach")
    @JvmField
    public val STONY_SHORE: RegistryReference<Biome> = of("stony_shore")
    @JvmField
    public val WARM_OCEAN: RegistryReference<Biome> = of("warm_ocean")
    @JvmField
    public val LUKEWARM_OCEAN: RegistryReference<Biome> = of("lukewarm_ocean")
    @JvmField
    public val DEEP_LUKEWARM_OCEAN: RegistryReference<Biome> = of("deep_lukewarm_ocean")
    @JvmField
    public val OCEAN: RegistryReference<Biome> = of("ocean")
    @JvmField
    public val DEEP_OCEAN: RegistryReference<Biome> = of("deep_ocean")
    @JvmField
    public val COLD_OCEAN: RegistryReference<Biome> = of("cold_ocean")
    @JvmField
    public val DEEP_COLD_OCEAN: RegistryReference<Biome> = of("deep_cold_ocean")
    @JvmField
    public val FROZEN_OCEAN: RegistryReference<Biome> = of("frozen_ocean")
    @JvmField
    public val DEEP_FROZEN_OCEAN: RegistryReference<Biome> = of("deep_frozen_ocean")
    @JvmField
    public val MUSHROOM_FIELDS: RegistryReference<Biome> = of("mushroom_fields")
    @JvmField
    public val DRIPSTONE_CAVES: RegistryReference<Biome> = of("dripstone_caves")
    @JvmField
    public val LUSH_CAVES: RegistryReference<Biome> = of("lush_caves")
    @JvmField
    public val DEEP_DARK: RegistryReference<Biome> = of("deep_dark")
    @JvmField
    public val NETHER_WASTES: RegistryReference<Biome> = of("nether_wastes")
    @JvmField
    public val WARPED_FOREST: RegistryReference<Biome> = of("warped_forest")
    @JvmField
    public val CRIMSON_FOREST: RegistryReference<Biome> = of("crimson_forest")
    @JvmField
    public val SOUL_SAND_VALLEY: RegistryReference<Biome> = of("soul_sand_valley")
    @JvmField
    public val BASALT_DELTAS: RegistryReference<Biome> = of("basalt_deltas")
    @JvmField
    public val THE_END: RegistryReference<Biome> = of("the_end")
    @JvmField
    public val END_HIGHLANDS: RegistryReference<Biome> = of("end_highlands")
    @JvmField
    public val END_MIDLANDS: RegistryReference<Biome> = of("end_midlands")
    @JvmField
    public val SMALL_END_ISLANDS: RegistryReference<Biome> = of("small_end_islands")
    @JvmField
    public val END_BARRENS: RegistryReference<Biome> = of("end_barrens")

    // @formatter:on
    @JvmStatic
    private fun of(name: String): RegistryReference<Biome> = RegistryReference.of(Registries.BIOME, Key.key(name))
}

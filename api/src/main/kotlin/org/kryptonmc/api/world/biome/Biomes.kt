/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.world.biome

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.DynamicRegistryReference
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All the built-in vanilla biomes.
 */
@Catalogue(Biome::class)
public object Biomes {

    // @formatter:off
    @JvmField
    public val THE_VOID: DynamicRegistryReference<Biome> = of("the_void")
    @JvmField
    public val PLAINS: DynamicRegistryReference<Biome> = of("plains")
    @JvmField
    public val SUNFLOWER_PLAINS: DynamicRegistryReference<Biome> = of("sunflower_plains")
    @JvmField
    public val SNOWY_PLAINS: DynamicRegistryReference<Biome> = of("snowy_plains")
    @JvmField
    public val ICE_SPIKES: DynamicRegistryReference<Biome> = of("ice_spikes")
    @JvmField
    public val DESERT: DynamicRegistryReference<Biome> = of("desert")
    @JvmField
    public val SWAMP: DynamicRegistryReference<Biome> = of("swamp")
    @JvmField
    public val MANGROVE_SWAMP: DynamicRegistryReference<Biome> = of("mangrove_swamp")
    @JvmField
    public val FOREST: DynamicRegistryReference<Biome> = of("forest")
    @JvmField
    public val FLOWER_FOREST: DynamicRegistryReference<Biome> = of("flower_forest")
    @JvmField
    public val BIRCH_FOREST: DynamicRegistryReference<Biome> = of("birch_forest")
    @JvmField
    public val DARK_FOREST: DynamicRegistryReference<Biome> = of("dark_forest")
    @JvmField
    public val OLD_GROWTH_BIRCH_FOREST: DynamicRegistryReference<Biome> = of("old_growth_birch_forest")
    @JvmField
    public val OLD_GROWTH_PINE_TAIGA: DynamicRegistryReference<Biome> = of("old_growth_pine_taiga")
    @JvmField
    public val OLD_GROWTH_SPRUCE_TAIGA: DynamicRegistryReference<Biome> = of("old_growth_spruce_taiga")
    @JvmField
    public val TAIGA: DynamicRegistryReference<Biome> = of("taiga")
    @JvmField
    public val SNOWY_TAIGA: DynamicRegistryReference<Biome> = of("snowy_taiga")
    @JvmField
    public val SAVANNA: DynamicRegistryReference<Biome> = of("savanna")
    @JvmField
    public val SAVANNA_PLATEAU: DynamicRegistryReference<Biome> = of("savanna_plateau")
    @JvmField
    public val WINDSWEPT_HILLS: DynamicRegistryReference<Biome> = of("windswept_hills")
    @JvmField
    public val WINDSWEPT_GRAVELLY_HILLS: DynamicRegistryReference<Biome> = of("windswept_gravelly_hills")
    @JvmField
    public val WINDSWEPT_FOREST: DynamicRegistryReference<Biome> = of("windswept_forest")
    @JvmField
    public val WINDSWEPT_SAVANNA: DynamicRegistryReference<Biome> = of("windswept_savanna")
    @JvmField
    public val JUNGLE: DynamicRegistryReference<Biome> = of("jungle")
    @JvmField
    public val SPARSE_JUNGLE: DynamicRegistryReference<Biome> = of("sparse_jungle")
    @JvmField
    public val BAMBOO_JUNGLE: DynamicRegistryReference<Biome> = of("bamboo_jungle")
    @JvmField
    public val BADLANDS: DynamicRegistryReference<Biome> = of("badlands")
    @JvmField
    public val ERODED_BADLANDS: DynamicRegistryReference<Biome> = of("eroded_badlands")
    @JvmField
    public val WOODED_BADLANDS: DynamicRegistryReference<Biome> = of("wooded_badlands")
    @JvmField
    public val MEADOW: DynamicRegistryReference<Biome> = of("meadow")
    @JvmField
    public val GROVE: DynamicRegistryReference<Biome> = of("grove")
    @JvmField
    public val SNOWY_SLOPES: DynamicRegistryReference<Biome> = of("snowy_slopes")
    @JvmField
    public val FROZEN_PEAKS: DynamicRegistryReference<Biome> = of("frozen_peaks")
    @JvmField
    public val JAGGED_PEAKS: DynamicRegistryReference<Biome> = of("jagged_peaks")
    @JvmField
    public val STONY_PEAKS: DynamicRegistryReference<Biome> = of("stony_peaks")
    @JvmField
    public val RIVER: DynamicRegistryReference<Biome> = of("river")
    @JvmField
    public val FROZEN_RIVER: DynamicRegistryReference<Biome> = of("frozen_river")
    @JvmField
    public val BEACH: DynamicRegistryReference<Biome> = of("beach")
    @JvmField
    public val SNOWY_BEACH: DynamicRegistryReference<Biome> = of("snowy_beach")
    @JvmField
    public val STONY_SHORE: DynamicRegistryReference<Biome> = of("stony_shore")
    @JvmField
    public val WARM_OCEAN: DynamicRegistryReference<Biome> = of("warm_ocean")
    @JvmField
    public val LUKEWARM_OCEAN: DynamicRegistryReference<Biome> = of("lukewarm_ocean")
    @JvmField
    public val DEEP_LUKEWARM_OCEAN: DynamicRegistryReference<Biome> = of("deep_lukewarm_ocean")
    @JvmField
    public val OCEAN: DynamicRegistryReference<Biome> = of("ocean")
    @JvmField
    public val DEEP_OCEAN: DynamicRegistryReference<Biome> = of("deep_ocean")
    @JvmField
    public val COLD_OCEAN: DynamicRegistryReference<Biome> = of("cold_ocean")
    @JvmField
    public val DEEP_COLD_OCEAN: DynamicRegistryReference<Biome> = of("deep_cold_ocean")
    @JvmField
    public val FROZEN_OCEAN: DynamicRegistryReference<Biome> = of("frozen_ocean")
    @JvmField
    public val DEEP_FROZEN_OCEAN: DynamicRegistryReference<Biome> = of("deep_frozen_ocean")
    @JvmField
    public val MUSHROOM_FIELDS: DynamicRegistryReference<Biome> = of("mushroom_fields")
    @JvmField
    public val DRIPSTONE_CAVES: DynamicRegistryReference<Biome> = of("dripstone_caves")
    @JvmField
    public val LUSH_CAVES: DynamicRegistryReference<Biome> = of("lush_caves")
    @JvmField
    public val DEEP_DARK: DynamicRegistryReference<Biome> = of("deep_dark")
    @JvmField
    public val NETHER_WASTES: DynamicRegistryReference<Biome> = of("nether_wastes")
    @JvmField
    public val WARPED_FOREST: DynamicRegistryReference<Biome> = of("warped_forest")
    @JvmField
    public val CRIMSON_FOREST: DynamicRegistryReference<Biome> = of("crimson_forest")
    @JvmField
    public val SOUL_SAND_VALLEY: DynamicRegistryReference<Biome> = of("soul_sand_valley")
    @JvmField
    public val BASALT_DELTAS: DynamicRegistryReference<Biome> = of("basalt_deltas")
    @JvmField
    public val THE_END: DynamicRegistryReference<Biome> = of("the_end")
    @JvmField
    public val END_HIGHLANDS: DynamicRegistryReference<Biome> = of("end_highlands")
    @JvmField
    public val END_MIDLANDS: DynamicRegistryReference<Biome> = of("end_midlands")
    @JvmField
    public val SMALL_END_ISLANDS: DynamicRegistryReference<Biome> = of("small_end_islands")
    @JvmField
    public val END_BARRENS: DynamicRegistryReference<Biome> = of("end_barrens")

    // @formatter:on
    @JvmStatic
    private fun of(name: String): DynamicRegistryReference<Biome> = DynamicRegistryReference.of(ResourceKeys.BIOME, Key.key(name))
}

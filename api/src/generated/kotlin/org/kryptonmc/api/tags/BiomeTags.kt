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
package org.kryptonmc.api.tags

import net.kyori.adventure.key.Key
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.biome.Biome

/**
 * This file is auto-generated. Do not edit this manually!
 */
public object BiomeTags {

    // @formatter:off
    @JvmField
    public val IS_DEEP_OCEAN: TagKey<Biome> = get("is_deep_ocean")
    @JvmField
    public val IS_OCEAN: TagKey<Biome> = get("is_ocean")
    @JvmField
    public val IS_BEACH: TagKey<Biome> = get("is_beach")
    @JvmField
    public val IS_RIVER: TagKey<Biome> = get("is_river")
    @JvmField
    public val IS_MOUNTAIN: TagKey<Biome> = get("is_mountain")
    @JvmField
    public val IS_BADLANDS: TagKey<Biome> = get("is_badlands")
    @JvmField
    public val IS_HILL: TagKey<Biome> = get("is_hill")
    @JvmField
    public val IS_TAIGA: TagKey<Biome> = get("is_taiga")
    @JvmField
    public val IS_JUNGLE: TagKey<Biome> = get("is_jungle")
    @JvmField
    public val IS_FOREST: TagKey<Biome> = get("is_forest")
    @JvmField
    public val IS_SAVANNA: TagKey<Biome> = get("is_savanna")
    @JvmField
    public val IS_OVERWORLD: TagKey<Biome> = get("is_overworld")
    @JvmField
    public val IS_NETHER: TagKey<Biome> = get("is_nether")
    @JvmField
    public val IS_END: TagKey<Biome> = get("is_end")
    @JvmField
    public val STRONGHOLD_BIASED_TO: TagKey<Biome> = get("stronghold_biased_to")
    @JvmField
    public val HAS_BURIED_TREASURE: TagKey<Biome> = get("has_structure/buried_treasure")
    @JvmField
    public val HAS_DESERT_PYRAMID: TagKey<Biome> = get("has_structure/desert_pyramid")
    @JvmField
    public val HAS_IGLOO: TagKey<Biome> = get("has_structure/igloo")
    @JvmField
    public val HAS_JUNGLE_TEMPLE: TagKey<Biome> = get("has_structure/jungle_temple")
    @JvmField
    public val HAS_MINESHAFT: TagKey<Biome> = get("has_structure/mineshaft")
    @JvmField
    public val HAS_MINESHAFT_MESA: TagKey<Biome> = get("has_structure/mineshaft_mesa")
    @JvmField
    public val HAS_OCEAN_MONUMENT: TagKey<Biome> = get("has_structure/ocean_monument")
    @JvmField
    public val HAS_OCEAN_RUIN_COLD: TagKey<Biome> = get("has_structure/ocean_ruin_cold")
    @JvmField
    public val HAS_OCEAN_RUIN_WARM: TagKey<Biome> = get("has_structure/ocean_ruin_warm")
    @JvmField
    public val HAS_PILLAGER_OUTPOST: TagKey<Biome> = get("has_structure/pillager_outpost")
    @JvmField
    public val HAS_RUINED_PORTAL_DESERT: TagKey<Biome> = get("has_structure/ruined_portal_desert")
    @JvmField
    public val HAS_RUINED_PORTAL_JUNGLE: TagKey<Biome> = get("has_structure/ruined_portal_jungle")
    @JvmField
    public val HAS_RUINED_PORTAL_OCEAN: TagKey<Biome> = get("has_structure/ruined_portal_ocean")
    @JvmField
    public val HAS_RUINED_PORTAL_SWAMP: TagKey<Biome> = get("has_structure/ruined_portal_swamp")
    @JvmField
    public val HAS_RUINED_PORTAL_MOUNTAIN: TagKey<Biome> = get("has_structure/ruined_portal_mountain")
    @JvmField
    public val HAS_RUINED_PORTAL_STANDARD: TagKey<Biome> = get("has_structure/ruined_portal_standard")
    @JvmField
    public val HAS_SHIPWRECK_BEACHED: TagKey<Biome> = get("has_structure/shipwreck_beached")
    @JvmField
    public val HAS_SHIPWRECK: TagKey<Biome> = get("has_structure/shipwreck")
    @JvmField
    public val HAS_STRONGHOLD: TagKey<Biome> = get("has_structure/stronghold")
    @JvmField
    public val HAS_SWAMP_HUT: TagKey<Biome> = get("has_structure/swamp_hut")
    @JvmField
    public val HAS_VILLAGE_DESERT: TagKey<Biome> = get("has_structure/village_desert")
    @JvmField
    public val HAS_VILLAGE_PLAINS: TagKey<Biome> = get("has_structure/village_plains")
    @JvmField
    public val HAS_VILLAGE_SAVANNA: TagKey<Biome> = get("has_structure/village_savanna")
    @JvmField
    public val HAS_VILLAGE_SNOWY: TagKey<Biome> = get("has_structure/village_snowy")
    @JvmField
    public val HAS_VILLAGE_TAIGA: TagKey<Biome> = get("has_structure/village_taiga")
    @JvmField
    public val HAS_WOODLAND_MANSION: TagKey<Biome> = get("has_structure/woodland_mansion")
    @JvmField
    public val HAS_NETHER_FORTRESS: TagKey<Biome> = get("has_structure/nether_fortress")
    @JvmField
    public val HAS_NETHER_FOSSIL: TagKey<Biome> = get("has_structure/nether_fossil")
    @JvmField
    public val HAS_BASTION_REMNANT: TagKey<Biome> = get("has_structure/bastion_remnant")
    @JvmField
    public val HAS_ANCIENT_CITY: TagKey<Biome> = get("has_structure/ancient_city")
    @JvmField
    public val HAS_RUINED_PORTAL_NETHER: TagKey<Biome> = get("has_structure/ruined_portal_nether")
    @JvmField
    public val HAS_END_CITY: TagKey<Biome> = get("has_structure/end_city")
    @JvmField
    public val REQUIRED_OCEAN_MONUMENT_SURROUNDING: TagKey<Biome> = get("required_ocean_monument_surrounding")
    @JvmField
    public val MINESHAFT_BLOCKING: TagKey<Biome> = get("mineshaft_blocking")
    @JvmField
    public val PLAYS_UNDERWATER_MUSIC: TagKey<Biome> = get("plays_underwater_music")
    @JvmField
    public val HAS_CLOSER_WATER_FOG: TagKey<Biome> = get("has_closer_water_fog")
    @JvmField
    public val WATER_ON_MAP_OUTLINES: TagKey<Biome> = get("water_on_map_outlines")
    @JvmField
    public val PRODUCES_CORALS_FROM_BONEMEAL: TagKey<Biome> = get("produces_corals_from_bonemeal")
    @JvmField
    public val WITHOUT_ZOMBIE_SIEGES: TagKey<Biome> = get("without_zombie_sieges")
    @JvmField
    public val WITHOUT_PATROL_SPAWNS: TagKey<Biome> = get("without_patrol_spawns")
    @JvmField
    public val WITHOUT_WANDERING_TRADER_SPAWNS: TagKey<Biome> = get("without_wandering_trader_spawns")
    @JvmField
    public val SPAWNS_COLD_VARIANT_FROGS: TagKey<Biome> = get("spawns_cold_variant_frogs")
    @JvmField
    public val SPAWNS_WARM_VARIANT_FROGS: TagKey<Biome> = get("spawns_warm_variant_frogs")
    @JvmField
    public val ONLY_ALLOWS_SNOW_AND_GOLD_RABBITS: TagKey<Biome> = get("only_allows_snow_and_gold_rabbits")
    @JvmField
    public val REDUCED_WATER_AMBIENT_SPAWNS: TagKey<Biome> = get("reduce_water_ambient_spawns")
    @JvmField
    public val ALLOWS_TROPICAL_FISH_SPAWNS_AT_ANY_HEIGHT: TagKey<Biome> = get("allows_tropical_fish_spawns_at_any_height")
    @JvmField
    public val POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS: TagKey<Biome> = get("polar_bears_spawn_on_alternate_blocks")
    @JvmField
    public val MORE_FREQUENT_DROWNED_SPAWNS: TagKey<Biome> = get("more_frequent_drowned_spawns")
    @JvmField
    public val ALLOWS_SURFACE_SLIME_SPAWNS: TagKey<Biome> = get("allows_surface_slime_spawns")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): TagKey<Biome> = TagKey.of(ResourceKeys.BIOME, Key.key(key))
}

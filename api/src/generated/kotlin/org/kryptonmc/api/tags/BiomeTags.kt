/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.api.world.biome.Biome

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(Tag::class)
public object BiomeTags {

    // @formatter:off
    @JvmField
    public val IS_DEEP_OCEAN: Tag<Biome> = get("is_deep_ocean")
    @JvmField
    public val IS_OCEAN: Tag<Biome> = get("is_ocean")
    @JvmField
    public val IS_BEACH: Tag<Biome> = get("is_beach")
    @JvmField
    public val IS_RIVER: Tag<Biome> = get("is_river")
    @JvmField
    public val IS_MOUNTAIN: Tag<Biome> = get("is_mountain")
    @JvmField
    public val IS_BADLANDS: Tag<Biome> = get("is_badlands")
    @JvmField
    public val IS_HILL: Tag<Biome> = get("is_hill")
    @JvmField
    public val IS_TAIGA: Tag<Biome> = get("is_taiga")
    @JvmField
    public val IS_JUNGLE: Tag<Biome> = get("is_jungle")
    @JvmField
    public val IS_FOREST: Tag<Biome> = get("is_forest")
    @JvmField
    public val IS_SAVANNA: Tag<Biome> = get("is_savanna")
    @JvmField
    public val IS_OVERWORLD: Tag<Biome> = get("is_overworld")
    @JvmField
    public val IS_NETHER: Tag<Biome> = get("is_nether")
    @JvmField
    public val IS_END: Tag<Biome> = get("is_end")
    @JvmField
    public val STRONGHOLD_BIASED_TO: Tag<Biome> = get("stronghold_biased_to")
    @JvmField
    public val HAS_BURIED_TREASURE: Tag<Biome> = get("has_structure/buried_treasure")
    @JvmField
    public val HAS_DESERT_PYRAMID: Tag<Biome> = get("has_structure/desert_pyramid")
    @JvmField
    public val HAS_IGLOO: Tag<Biome> = get("has_structure/igloo")
    @JvmField
    public val HAS_JUNGLE_TEMPLE: Tag<Biome> = get("has_structure/jungle_temple")
    @JvmField
    public val HAS_MINESHAFT: Tag<Biome> = get("has_structure/mineshaft")
    @JvmField
    public val HAS_MINESHAFT_MESA: Tag<Biome> = get("has_structure/mineshaft_mesa")
    @JvmField
    public val HAS_OCEAN_MONUMENT: Tag<Biome> = get("has_structure/ocean_monument")
    @JvmField
    public val HAS_OCEAN_RUIN_COLD: Tag<Biome> = get("has_structure/ocean_ruin_cold")
    @JvmField
    public val HAS_OCEAN_RUIN_WARM: Tag<Biome> = get("has_structure/ocean_ruin_warm")
    @JvmField
    public val HAS_PILLAGER_OUTPOST: Tag<Biome> = get("has_structure/pillager_outpost")
    @JvmField
    public val HAS_RUINED_PORTAL_DESERT: Tag<Biome> = get("has_structure/ruined_portal_desert")
    @JvmField
    public val HAS_RUINED_PORTAL_JUNGLE: Tag<Biome> = get("has_structure/ruined_portal_jungle")
    @JvmField
    public val HAS_RUINED_PORTAL_OCEAN: Tag<Biome> = get("has_structure/ruined_portal_ocean")
    @JvmField
    public val HAS_RUINED_PORTAL_SWAMP: Tag<Biome> = get("has_structure/ruined_portal_swamp")
    @JvmField
    public val HAS_RUINED_PORTAL_MOUNTAIN: Tag<Biome> = get("has_structure/ruined_portal_mountain")
    @JvmField
    public val HAS_RUINED_PORTAL_STANDARD: Tag<Biome> = get("has_structure/ruined_portal_standard")
    @JvmField
    public val HAS_SHIPWRECK_BEACHED: Tag<Biome> = get("has_structure/shipwreck_beached")
    @JvmField
    public val HAS_SHIPWRECK: Tag<Biome> = get("has_structure/shipwreck")
    @JvmField
    public val HAS_STRONGHOLD: Tag<Biome> = get("has_structure/stronghold")
    @JvmField
    public val HAS_SWAMP_HUT: Tag<Biome> = get("has_structure/swamp_hut")
    @JvmField
    public val HAS_VILLAGE_DESERT: Tag<Biome> = get("has_structure/village_desert")
    @JvmField
    public val HAS_VILLAGE_PLAINS: Tag<Biome> = get("has_structure/village_plains")
    @JvmField
    public val HAS_VILLAGE_SAVANNA: Tag<Biome> = get("has_structure/village_savanna")
    @JvmField
    public val HAS_VILLAGE_SNOWY: Tag<Biome> = get("has_structure/village_snowy")
    @JvmField
    public val HAS_VILLAGE_TAIGA: Tag<Biome> = get("has_structure/village_taiga")
    @JvmField
    public val HAS_WOODLAND_MANSION: Tag<Biome> = get("has_structure/woodland_mansion")
    @JvmField
    public val HAS_NETHER_FORTRESS: Tag<Biome> = get("has_structure/nether_fortress")
    @JvmField
    public val HAS_NETHER_FOSSIL: Tag<Biome> = get("has_structure/nether_fossil")
    @JvmField
    public val HAS_BASTION_REMNANT: Tag<Biome> = get("has_structure/bastion_remnant")
    @JvmField
    public val HAS_ANCIENT_CITY: Tag<Biome> = get("has_structure/ancient_city")
    @JvmField
    public val HAS_RUINED_PORTAL_NETHER: Tag<Biome> = get("has_structure/ruined_portal_nether")
    @JvmField
    public val HAS_END_CITY: Tag<Biome> = get("has_structure/end_city")
    @JvmField
    public val REQUIRED_OCEAN_MONUMENT_SURROUNDING: Tag<Biome> = get("required_ocean_monument_surrounding")
    @JvmField
    public val MINESHAFT_BLOCKING: Tag<Biome> = get("mineshaft_blocking")
    @JvmField
    public val PLAYS_UNDERWATER_MUSIC: Tag<Biome> = get("plays_underwater_music")
    @JvmField
    public val HAS_CLOSER_WATER_FOG: Tag<Biome> = get("has_closer_water_fog")
    @JvmField
    public val WATER_ON_MAP_OUTLINES: Tag<Biome> = get("water_on_map_outlines")
    @JvmField
    public val PRODUCES_CORALS_FROM_BONEMEAL: Tag<Biome> = get("produces_corals_from_bonemeal")
    @JvmField
    public val WITHOUT_ZOMBIE_SIEGES: Tag<Biome> = get("without_zombie_sieges")
    @JvmField
    public val WITHOUT_PATROL_SPAWNS: Tag<Biome> = get("without_patrol_spawns")
    @JvmField
    public val WITHOUT_WANDERING_TRADER_SPAWNS: Tag<Biome> = get("without_wandering_trader_spawns")
    @JvmField
    public val SPAWNS_COLD_VARIANT_FROGS: Tag<Biome> = get("spawns_cold_variant_frogs")
    @JvmField
    public val SPAWNS_WARM_VARIANT_FROGS: Tag<Biome> = get("spawns_warm_variant_frogs")
    @JvmField
    public val ONLY_ALLOWS_SNOW_AND_GOLD_RABBITS: Tag<Biome> = get("only_allows_snow_and_gold_rabbits")
    @JvmField
    public val REDUCED_WATER_AMBIENT_SPAWNS: Tag<Biome> = get("reduce_water_ambient_spawns")
    @JvmField
    public val ALLOWS_TROPICAL_FISH_SPAWNS_AT_ANY_HEIGHT: Tag<Biome> = get("allows_tropical_fish_spawns_at_any_height")
    @JvmField
    public val POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS: Tag<Biome> = get("polar_bears_spawn_on_alternate_blocks")
    @JvmField
    public val MORE_FREQUENT_DROWNED_SPAWNS: Tag<Biome> = get("more_frequent_drowned_spawns")
    @JvmField
    public val ALLOWS_SURFACE_SLIME_SPAWNS: Tag<Biome> = get("allows_surface_slime_spawns")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): Tag<Biome> = Krypton.tagManager[TagTypes.BIOMES, "minecraft:$key"]!!
}

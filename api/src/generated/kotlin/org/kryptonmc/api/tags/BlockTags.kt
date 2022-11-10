/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.resource.ResourceKeys

/**
 * This file is auto-generated. Do not edit this manually!
 */
public object BlockTags {

    // @formatter:off
    @JvmField
    public val WOOL: TagKey<Block> = get("wool")
    @JvmField
    public val PLANKS: TagKey<Block> = get("planks")
    @JvmField
    public val STONE_BRICKS: TagKey<Block> = get("stone_bricks")
    @JvmField
    public val WOODEN_BUTTONS: TagKey<Block> = get("wooden_buttons")
    @JvmField
    public val BUTTONS: TagKey<Block> = get("buttons")
    @JvmField
    public val WOOL_CARPETS: TagKey<Block> = get("wool_carpets")
    @JvmField
    public val WOODEN_DOORS: TagKey<Block> = get("wooden_doors")
    @JvmField
    public val WOODEN_STAIRS: TagKey<Block> = get("wooden_stairs")
    @JvmField
    public val WOODEN_SLABS: TagKey<Block> = get("wooden_slabs")
    @JvmField
    public val WOODEN_FENCES: TagKey<Block> = get("wooden_fences")
    @JvmField
    public val PRESSURE_PLATES: TagKey<Block> = get("pressure_plates")
    @JvmField
    public val WOODEN_PRESSURE_PLATES: TagKey<Block> = get("wooden_pressure_plates")
    @JvmField
    public val STONE_PRESSURE_PLATES: TagKey<Block> = get("stone_pressure_plates")
    @JvmField
    public val WOODEN_TRAPDOORS: TagKey<Block> = get("wooden_trapdoors")
    @JvmField
    public val DOORS: TagKey<Block> = get("doors")
    @JvmField
    public val SAPLINGS: TagKey<Block> = get("saplings")
    @JvmField
    public val LOGS_THAT_BURN: TagKey<Block> = get("logs_that_burn")
    @JvmField
    public val OVERWORLD_NATURAL_LOGS: TagKey<Block> = get("overworld_natural_logs")
    @JvmField
    public val LOGS: TagKey<Block> = get("logs")
    @JvmField
    public val DARK_OAK_LOGS: TagKey<Block> = get("dark_oak_logs")
    @JvmField
    public val OAK_LOGS: TagKey<Block> = get("oak_logs")
    @JvmField
    public val BIRCH_LOGS: TagKey<Block> = get("birch_logs")
    @JvmField
    public val ACACIA_LOGS: TagKey<Block> = get("acacia_logs")
    @JvmField
    public val JUNGLE_LOGS: TagKey<Block> = get("jungle_logs")
    @JvmField
    public val SPRUCE_LOGS: TagKey<Block> = get("spruce_logs")
    @JvmField
    public val MANGROVE_LOGS: TagKey<Block> = get("mangrove_logs")
    @JvmField
    public val CRIMSON_STEMS: TagKey<Block> = get("crimson_stems")
    @JvmField
    public val WARPED_STEMS: TagKey<Block> = get("warped_stems")
    @JvmField
    public val WART_BLOCKS: TagKey<Block> = get("wart_blocks")
    @JvmField
    public val BANNERS: TagKey<Block> = get("banners")
    @JvmField
    public val SAND: TagKey<Block> = get("sand")
    @JvmField
    public val STAIRS: TagKey<Block> = get("stairs")
    @JvmField
    public val SLABS: TagKey<Block> = get("slabs")
    @JvmField
    public val WALLS: TagKey<Block> = get("walls")
    @JvmField
    public val ANVIL: TagKey<Block> = get("anvil")
    @JvmField
    public val RAILS: TagKey<Block> = get("rails")
    @JvmField
    public val LEAVES: TagKey<Block> = get("leaves")
    @JvmField
    public val TRAPDOORS: TagKey<Block> = get("trapdoors")
    @JvmField
    public val SMALL_FLOWERS: TagKey<Block> = get("small_flowers")
    @JvmField
    public val BEDS: TagKey<Block> = get("beds")
    @JvmField
    public val FENCES: TagKey<Block> = get("fences")
    @JvmField
    public val TALL_FLOWERS: TagKey<Block> = get("tall_flowers")
    @JvmField
    public val FLOWERS: TagKey<Block> = get("flowers")
    @JvmField
    public val PIGLIN_REPELLENTS: TagKey<Block> = get("piglin_repellents")
    @JvmField
    public val GOLD_ORES: TagKey<Block> = get("gold_ores")
    @JvmField
    public val IRON_ORES: TagKey<Block> = get("iron_ores")
    @JvmField
    public val DIAMOND_ORES: TagKey<Block> = get("diamond_ores")
    @JvmField
    public val REDSTONE_ORES: TagKey<Block> = get("redstone_ores")
    @JvmField
    public val LAPIS_ORES: TagKey<Block> = get("lapis_ores")
    @JvmField
    public val COAL_ORES: TagKey<Block> = get("coal_ores")
    @JvmField
    public val EMERALD_ORES: TagKey<Block> = get("emerald_ores")
    @JvmField
    public val COPPER_ORES: TagKey<Block> = get("copper_ores")
    @JvmField
    public val NON_FLAMMABLE_WOOD: TagKey<Block> = get("non_flammable_wood")
    @JvmField
    public val CANDLES: TagKey<Block> = get("candles")
    @JvmField
    public val DIRT: TagKey<Block> = get("dirt")
    @JvmField
    public val TERRACOTTA: TagKey<Block> = get("terracotta")
    @JvmField
    public val COMPLETES_FIND_TREE_TUTORIAL: TagKey<Block> = get("completes_find_tree_tutorial")
    @JvmField
    public val FLOWER_POTS: TagKey<Block> = get("flower_pots")
    @JvmField
    public val ENDERMAN_HOLDABLE: TagKey<Block> = get("enderman_holdable")
    @JvmField
    public val ICE: TagKey<Block> = get("ice")
    @JvmField
    public val VALID_SPAWN: TagKey<Block> = get("valid_spawn")
    @JvmField
    public val IMPERMEABLE: TagKey<Block> = get("impermeable")
    @JvmField
    public val UNDERWATER_BONEMEALS: TagKey<Block> = get("underwater_bonemeals")
    @JvmField
    public val CORAL_BLOCKS: TagKey<Block> = get("coral_blocks")
    @JvmField
    public val WALL_CORALS: TagKey<Block> = get("wall_corals")
    @JvmField
    public val CORAL_PLANTS: TagKey<Block> = get("coral_plants")
    @JvmField
    public val CORALS: TagKey<Block> = get("corals")
    @JvmField
    public val BAMBOO_PLANTABLE_ON: TagKey<Block> = get("bamboo_plantable_on")
    @JvmField
    public val STANDING_SIGNS: TagKey<Block> = get("standing_signs")
    @JvmField
    public val WALL_SIGNS: TagKey<Block> = get("wall_signs")
    @JvmField
    public val SIGNS: TagKey<Block> = get("signs")
    @JvmField
    public val DRAGON_IMMUNE: TagKey<Block> = get("dragon_immune")
    @JvmField
    public val DRAGON_TRANSPARENT: TagKey<Block> = get("dragon_transparent")
    @JvmField
    public val WITHER_IMMUNE: TagKey<Block> = get("wither_immune")
    @JvmField
    public val WITHER_SUMMON_BASE_BLOCKS: TagKey<Block> = get("wither_summon_base_blocks")
    @JvmField
    public val BEEHIVES: TagKey<Block> = get("beehives")
    @JvmField
    public val CROPS: TagKey<Block> = get("crops")
    @JvmField
    public val BEE_GROWABLES: TagKey<Block> = get("bee_growables")
    @JvmField
    public val PORTALS: TagKey<Block> = get("portals")
    @JvmField
    public val FIRE: TagKey<Block> = get("fire")
    @JvmField
    public val NYLIUM: TagKey<Block> = get("nylium")
    @JvmField
    public val BEACON_BASE_BLOCKS: TagKey<Block> = get("beacon_base_blocks")
    @JvmField
    public val SOUL_SPEED_BLOCKS: TagKey<Block> = get("soul_speed_blocks")
    @JvmField
    public val WALL_POST_OVERRIDE: TagKey<Block> = get("wall_post_override")
    @JvmField
    public val CLIMBABLE: TagKey<Block> = get("climbable")
    @JvmField
    public val FALL_DAMAGE_RESETTING: TagKey<Block> = get("fall_damage_resetting")
    @JvmField
    public val SHULKER_BOXES: TagKey<Block> = get("shulker_boxes")
    @JvmField
    public val HOGLIN_REPELLENTS: TagKey<Block> = get("hoglin_repellents")
    @JvmField
    public val SOUL_FIRE_BASE_BLOCKS: TagKey<Block> = get("soul_fire_base_blocks")
    @JvmField
    public val STRIDER_WARM_BLOCKS: TagKey<Block> = get("strider_warm_blocks")
    @JvmField
    public val CAMPFIRES: TagKey<Block> = get("campfires")
    @JvmField
    public val GUARDED_BY_PIGLINS: TagKey<Block> = get("guarded_by_piglins")
    @JvmField
    public val PREVENT_MOB_SPAWNING_INSIDE: TagKey<Block> = get("prevent_mob_spawning_inside")
    @JvmField
    public val FENCE_GATES: TagKey<Block> = get("fence_gates")
    @JvmField
    public val UNSTABLE_BOTTOM_CENTER: TagKey<Block> = get("unstable_bottom_center")
    @JvmField
    public val MUSHROOM_GROW_BLOCK: TagKey<Block> = get("mushroom_grow_block")
    @JvmField
    public val INFINIBURN_OVERWORLD: TagKey<Block> = get("infiniburn_overworld")
    @JvmField
    public val INFINIBURN_NETHER: TagKey<Block> = get("infiniburn_nether")
    @JvmField
    public val INFINIBURN_END: TagKey<Block> = get("infiniburn_end")
    @JvmField
    public val BASE_STONE_OVERWORLD: TagKey<Block> = get("base_stone_overworld")
    @JvmField
    public val STONE_ORE_REPLACEABLES: TagKey<Block> = get("stone_ore_replaceables")
    @JvmField
    public val DEEPSLATE_ORE_REPLACEABLES: TagKey<Block> = get("deepslate_ore_replaceables")
    @JvmField
    public val BASE_STONE_NETHER: TagKey<Block> = get("base_stone_nether")
    @JvmField
    public val OVERWORLD_CARVER_REPLACEABLES: TagKey<Block> = get("overworld_carver_replaceables")
    @JvmField
    public val NETHER_CARVER_REPLACEABLES: TagKey<Block> = get("nether_carver_replaceables")
    @JvmField
    public val CANDLE_CAKES: TagKey<Block> = get("candle_cakes")
    @JvmField
    public val CAULDRONS: TagKey<Block> = get("cauldrons")
    @JvmField
    public val CRYSTAL_SOUND_BLOCKS: TagKey<Block> = get("crystal_sound_blocks")
    @JvmField
    public val INSIDE_STEP_SOUND_BLOCKS: TagKey<Block> = get("inside_step_sound_blocks")
    @JvmField
    public val OCCLUDES_VIBRATION_SIGNALS: TagKey<Block> = get("occludes_vibration_signals")
    @JvmField
    public val DAMPENS_VIBRATIONS: TagKey<Block> = get("dampens_vibrations")
    @JvmField
    public val DRIPSTONE_REPLACEABLE: TagKey<Block> = get("dripstone_replaceable_blocks")
    @JvmField
    public val CAVE_VINES: TagKey<Block> = get("cave_vines")
    @JvmField
    public val MOSS_REPLACEABLE: TagKey<Block> = get("moss_replaceable")
    @JvmField
    public val LUSH_GROUND_REPLACEABLE: TagKey<Block> = get("lush_ground_replaceable")
    @JvmField
    public val AZALEA_ROOT_REPLACEABLE: TagKey<Block> = get("azalea_root_replaceable")
    @JvmField
    public val SMALL_DRIPLEAF_PLACEABLE: TagKey<Block> = get("small_dripleaf_placeable")
    @JvmField
    public val BIG_DRIPLEAF_PLACEABLE: TagKey<Block> = get("big_dripleaf_placeable")
    @JvmField
    public val SNOW: TagKey<Block> = get("snow")
    @JvmField
    public val MINEABLE_WITH_AXE: TagKey<Block> = get("mineable/axe")
    @JvmField
    public val MINEABLE_WITH_HOE: TagKey<Block> = get("mineable/hoe")
    @JvmField
    public val MINEABLE_WITH_PICKAXE: TagKey<Block> = get("mineable/pickaxe")
    @JvmField
    public val MINEABLE_WITH_SHOVEL: TagKey<Block> = get("mineable/shovel")
    @JvmField
    public val NEEDS_DIAMOND_TOOL: TagKey<Block> = get("needs_diamond_tool")
    @JvmField
    public val NEEDS_IRON_TOOL: TagKey<Block> = get("needs_iron_tool")
    @JvmField
    public val NEEDS_STONE_TOOL: TagKey<Block> = get("needs_stone_tool")
    @JvmField
    public val FEATURES_CANNOT_REPLACE: TagKey<Block> = get("features_cannot_replace")
    @JvmField
    public val LAVA_POOL_STONE_CANNOT_REPLACE: TagKey<Block> = get("lava_pool_stone_cannot_replace")
    @JvmField
    public val GEODE_INVALID_BLOCKS: TagKey<Block> = get("geode_invalid_blocks")
    @JvmField
    public val FROG_PREFER_JUMP_TO: TagKey<Block> = get("frog_prefer_jump_to")
    @JvmField
    public val SCULK_REPLACEABLE: TagKey<Block> = get("sculk_replaceable")
    @JvmField
    public val SCULK_REPLACEABLE_WORLD_GEN: TagKey<Block> = get("sculk_replaceable_world_gen")
    @JvmField
    public val ANCIENT_CITY_REPLACEABLE: TagKey<Block> = get("ancient_city_replaceable")
    @JvmField
    public val ANIMALS_SPAWNABLE_ON: TagKey<Block> = get("animals_spawnable_on")
    @JvmField
    public val AXOLOTLS_SPAWNABLE_ON: TagKey<Block> = get("axolotls_spawnable_on")
    @JvmField
    public val GOATS_SPAWNABLE_ON: TagKey<Block> = get("goats_spawnable_on")
    @JvmField
    public val MOOSHROOMS_SPAWNABLE_ON: TagKey<Block> = get("mooshrooms_spawnable_on")
    @JvmField
    public val PARROTS_SPAWNABLE_ON: TagKey<Block> = get("parrots_spawnable_on")
    @JvmField
    public val POLAR_BEARS_SPAWNABLE_ON_ALTERNATE: TagKey<Block> = get("polar_bears_spawnable_on_alternate")
    @JvmField
    public val RABBITS_SPAWNABLE_ON: TagKey<Block> = get("rabbits_spawnable_on")
    @JvmField
    public val FOXES_SPAWNABLE_ON: TagKey<Block> = get("foxes_spawnable_on")
    @JvmField
    public val WOLVES_SPAWNABLE_ON: TagKey<Block> = get("wolves_spawnable_on")
    @JvmField
    public val FROGS_SPAWNABLE_ON: TagKey<Block> = get("frogs_spawnable_on")
    @JvmField
    public val AZALEA_GROWS_ON: TagKey<Block> = get("azalea_grows_on")
    @JvmField
    public val REPLACEABLE_PLANTS: TagKey<Block> = get("replaceable_plants")
    @JvmField
    public val CONVERTABLE_TO_MUD: TagKey<Block> = get("convertable_to_mud")
    @JvmField
    public val MANGROVE_LOGS_CAN_GROW_THROUGH: TagKey<Block> = get("mangrove_logs_can_grow_through")
    @JvmField
    public val MANGROVE_ROOTS_CAN_GROW_THROUGH: TagKey<Block> = get("mangrove_roots_can_grow_through")
    @JvmField
    public val DEAD_BUSH_MAY_PLACE_ON: TagKey<Block> = get("dead_bush_may_place_on")
    @JvmField
    public val SNAPS_GOAT_HORN: TagKey<Block> = get("snaps_goat_horn")
    @JvmField
    public val SNOW_LAYER_CANNOT_SURVIVE_ON: TagKey<Block> = get("snow_layer_cannot_survive_on")
    @JvmField
    public val SNOW_LAYER_CAN_SURVIVE_ON: TagKey<Block> = get("snow_layer_can_survive_on")

    // @formatter:on
    @JvmStatic
    public fun get(key: String): TagKey<Block> = TagKey.of(ResourceKeys.BLOCK, Key.key(key))
}

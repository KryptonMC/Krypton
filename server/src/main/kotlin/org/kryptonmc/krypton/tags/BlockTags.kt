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
package org.kryptonmc.krypton.tags

import net.kyori.adventure.key.Key

object BlockTags {

    val ACACIA_LOGS = get("acacia_logs")
    val ANVILS = get("anvil")
    val BAMBOO_PLANTABLE_ON = get("bamboo_plantable_on")
    val BANNERS = get("banners")
    val BASE_STONE_NETHER = get("base_stone_nether")
    val BASE_STONE_OVERWORLD = get("base_stone_overworld")
    val BEACON_BASE_BLOCKS = get("beacon_base_blocks")
    val BEDS = get("beds")
    val BEEHIVES = get("beehives")
    val BEE_GROWABLES = get("bee_growables")
    val BIRCH_LOGS = get("birch_logs")
    val BUTTONS = get("buttons")
    val CAMPFIRES = get("campfires")
    val CANDLE_CAKES = get("candle_cakes")
    val CANDLES = get("candles")
    val CARPETS = get("carpets")
    val CAULDRONS = get("cauldrons")
    val CAVE_VINES = get("cave_vines")
    val CLIMBABLE = get("climbable")
    val COAL_ORES = get("coal_ores")
    val COPPER_ORES = get("copper_ores")
    val CORALS = get("corals")
    val CORAL_BLOCKS = get("coral_blocks")
    val CORAL_PLANTS = get("coral_plants")
    val CRIMSON_STEMS = get("crimson_stems")
    val CROPS = get("crops")
    val CRYSTAL_SOUND_BLOCKS = get("crystal_sound_blocks")
    val DARK_OAK_LOGS = get("dark_oak_logs")
    val DEEPSLATE_ORE_REPLACEABLES = get("deepslate_ore_replaceables")
    val DIAMOND_ORES = get("diamond_ores")
    val DIRT = get("dirt")
    val DOORS = get("doors")
    val DRAGON_IMMUNE = get("dragon_immune")
    val DRIPSTONE_REPLACEABLE_BLOCKS = get("dripstone_replaceable_blocks")
    val EMERALD_ORES = get("emerald_ores")
    val ENDERMAN_HOLDABLE = get("enderman_holdable")
    val FEATURES_CANNOT_REPLACE = get("features_cannot_replace")
    val FENCE_GATES = get("fence_gates")
    val FENCES = get("fences")
    val FIRE = get("fire")
    val FLOWER_POTS = get("flower_pots")
    val FLOWERS = get("flowers")
    val GEODE_INVALID_BLOCKS = get("geode_invalid_blocks")
    val GOLD_ORES = get("gold_ores")
    val GUARDED_BY_PIGLINS = get("guarded_by_piglins")
    val HOGLIN_REPELLENTS = get("hoglin_repellents")
    val ICE = get("ice")
    val IMPERMEABLE = get("impermeable")
    val INFINIBURN_END = get("infiniburn_end")
    val INFINIBURN_NETHER = get("infiniburn_nether")
    val INFINIBURN_OVERWORLD = get("infiniburn_overworld")
    val INSIDE_STEP_SOUND_BLOCKS = get("inside_step_sound_blocks")
    val IRON_ORES = get("iron_ores")
    val JUNGLE_LOGS = get("jungle_logs")
    val LAPIS_ORES = get("lapis_ores")
    val LAVA_POOL_STONE_REPLACEABLES = get("lava_pool_stone_replaceables")
    val LEAVES = get("leaves")
    val LOGS = get("logs")
    val LOGS_THAT_BURN = get("logs_that_burn")
    val LUSH_GROUND_REPLACEABLE = get("lush_ground_replaceable")
    val MINEABLE_AXE = get("mineable/axe")
    val MINEABLE_HOE = get("mineable/hoe")
    val MINEABLE_PICKAXE = get("mineable/pickaxe")
    val MINEABLE_SHOVEL = get("mineable/shovel")
    val MOSS_REPLACEABLE = get("moss_replaceable")
    val MUSHROOM_GROW_BLOCK = get("mushroom_grow_block")
    val NEEDS_DIAMOND_TOOL = get("needs_diamond_tool")
    val NEEDS_IRON_TOOL = get("needs_iron_tool")
    val NEEDS_STONE_TOOL = get("needs_stone_tool")
    val NON_FLAMMABLE_WOOD = get("non_flammable_wood")
    val NYLIUM = get("nylium")
    val OAK_LOGS = get("oak_logs")
    val OCCLUDES_VIBRATION_SIGNALS = get("occludes_vibration_signals")
    val PIGLIN_REPELLENTS = get("piglin_repellents")
    val PLANKS = get("planks")
    val PORTALS = get("portals")
    val PRESSURE_PLATES = get("pressure_plates")
    val PREVENT_MOB_SPAWNING_INSIDE = get("prevent_mob_spawning_inside")
    val RAILS = get("rails")
    val REDSTONE_ORES = get("redstone_ores")
    val SAND = get("sand")
    val SAPLINGS = get("saplings")
    val SHULKER_BOXES = get("shulker_boxes")
    val SIGNS = get("signs")
    val SLABS = get("slabs")
    val SMALL_DRIPLEAF_PLACEABLE = get("small_dripleaf_placeable")
    val SMALL_FLOWERS = get("small_flowers")
    val SNOW = get("snow")
    val SOUL_FIRE_BASE_BLOCKS = get("soul_fire_base_blocks")
    val SOUL_SPEED_BLOCKS = get("soul_speed_blocks")
    val SPRUCE_LOGS = get("spruce_logs")
    val STAIRS = get("stairs")
    val STANDING_SIGNS = get("standing_signs")
    val STONE_BRICKS = get("stone_bricks")
    val STONE_ORE_REPLACEABLES = get("stone_ore_replaceables")
    val STONE_PRESSURE_PLATES = get("stone_pressure_plates")
    val STRIDER_WARM_BLOCKS = get("strider_warm_blocks")
    val TALL_FLOWERS = get("tall_flowers")
    val TRAPDOORS = get("trapdoors")
    val UNDERWATER_BONEMEALS = get("underwater_bonemeals")
    val UNSTABLE_BOTTOM_CENTER = get("unstable_bottom_center")
    val VALID_SPAWN = get("valid_spawn")
    val WALLS = get("walls")
    val WALL_CORALS = get("wall_corals")
    val WALL_POST_OVERRIDE = get("wall_post_override")
    val WALL_SIGNS = get("wall_signs")
    val WARPED_STEMS = get("warped_stems")
    val WART_BLOCKS = get("wart_blocks")
    val WITHER_IMMUNE = get("wither_immune")
    val WITHER_SUMMON_BASE_BLOCKS = get("wither_summon_base_blocks")
    val WOODEN_BUTTONS = get("wooden_buttons")
    val WOODEN_DOORS = get("wooden_doors")
    val WOODEN_FENCES = get("wooden_fences")
    val WOODEN_PRESSURE_PLATES = get("wooden_pressure_plates")
    val WOODEN_SLABS = get("wooden_slabs")
    val WOODEN_STAIRS = get("wooden_stairs")
    val WOODEN_TRAPDOORS = get("wooden_trapdoors")
    val WOOL = get("wool")

    private fun get(name: String) = KryptonTagManager.load(Key.key(name), TagTypes.BLOCK)
}
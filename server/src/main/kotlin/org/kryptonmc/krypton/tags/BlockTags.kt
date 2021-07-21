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

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.resource.ResourceKeys

object BlockTags {

    val HELPER = StaticTags.create(ResourceKeys.BLOCK, "tags/blocks")

    val ACACIA_LOGS = bind("acacia_logs")
    val ANVILS = bind("anvil")
    val BAMBOO_PLANTABLE_ON = bind("bamboo_plantable_on")
    val BANNERS = bind("banners")
    val BASE_STONE_NETHER = bind("base_stone_nether")
    val BASE_STONE_OVERWORLD = bind("base_stone_overworld")
    val BEACON_BASE_BLOCKS = bind("beacon_base_blocks")
    val BEDS = bind("beds")
    val BEEHIVES = bind("beehives")
    val BEE_GROWABLES = bind("bee_growables")
    val BIRCH_LOGS = bind("birch_logs")
    val BUTTONS = bind("buttons")
    val CAMPFIRES = bind("campfires")
    val CANDLE_CAKES = bind("candle_cakes")
    val CANDLES = bind("candles")
    val CARPETS = bind("carpets")
    val CAULDRONS = bind("cauldrons")
    val CAVE_VINES = bind("cave_vines")
    val CLIMBABLE = bind("climbable")
    val COAL_ORES = bind("coal_ores")
    val COPPER_ORES = bind("copper_ores")
    val CORALS = bind("corals")
    val CORAL_BLOCKS = bind("coral_blocks")
    val CORAL_PLANTS = bind("coral_plants")
    val CRIMSON_STEMS = bind("crimson_stems")
    val CROPS = bind("crops")
    val CRYSTAL_SOUND_BLOCKS = bind("crystal_sound_blocks")
    val DARK_OAK_LOGS = bind("dark_oak_logs")
    val DEEPSLATE_ORE_REPLACEABLES = bind("deepslate_ore_replaceables")
    val DIAMOND_ORES = bind("diamond_ores")
    val DIRT = bind("dirt")
    val DOORS = bind("doors")
    val DRAGON_IMMUNE = bind("dragon_immune")
    val DRIPSTONE_REPLACEABLE_BLOCKS = bind("dripstone_replaceable_blocks")
    val EMERALD_ORES = bind("emerald_ores")
    val ENDERMAN_HOLDABLE = bind("enderman_holdable")
    val FEATURES_CANNOT_REPLACE = bind("features_cannot_replace")
    val FENCE_GATES = bind("fence_gates")
    val FENCES = bind("fences")
    val FIRE = bind("fire")
    val FLOWER_POTS = bind("flower_pots")
    val FLOWERS = bind("flowers")
    val GEODE_INVALID_BLOCKS = bind("geode_invalid_blocks")
    val GOLD_ORES = bind("gold_ores")
    val GUARDED_BY_PIGLINS = bind("guarded_by_piglins")
    val HOGLIN_REPELLENTS = bind("hoglin_repellents")
    val ICE = bind("ice")
    val IMPERMEABLE = bind("impermeable")
    val INFINIBURN_END = bind("infiniburn_end")
    val INFINIBURN_NETHER = bind("infiniburn_nether")
    val INFINIBURN_OVERWORLD = bind("infiniburn_overworld")
    val INSIDE_STEP_SOUND_BLOCKS = bind("inside_step_sound_blocks")
    val IRON_ORES = bind("iron_ores")
    val JUNGLE_LOGS = bind("jungle_logs")
    val LAPIS_ORES = bind("lapis_ores")
    val LAVA_POOL_STONE_REPLACEABLES = bind("lava_pool_stone_replaceables")
    val LEAVES = bind("leaves")
    val LOGS = bind("logs")
    val LOGS_THAT_BURN = bind("logs_that_burn")
    val LUSH_GROUND_REPLACEABLE = bind("lush_ground_replaceable")
    val MINEABLE_AXE = bind("mineable/axe")
    val MINEABLE_HOE = bind("mineable/hoe")
    val MINEABLE_PICKAXE = bind("mineable/pickaxe")
    val MINEABLE_SHOVEL = bind("mineable/shovel")
    val MOSS_REPLACEABLE = bind("moss_replaceable")
    val MUSHROOM_GROW_BLOCK = bind("mushroom_grow_block")
    val NEEDS_DIAMOND_TOOL = bind("needs_diamond_tool")
    val NEEDS_IRON_TOOL = bind("needs_iron_tool")
    val NEEDS_STONE_TOOL = bind("needs_stone_tool")
    val NON_FLAMMABLE_WOOD = bind("non_flammable_wood")
    val NYLIUM = bind("nylium")
    val OAK_LOGS = bind("oak_logs")
    val OCCLUDES_VIBRATION_SIGNALS = bind("occludes_vibration_signals")
    val PIGLIN_REPELLENTS = bind("piglin_repellents")
    val PLANKS = bind("planks")
    val PORTALS = bind("portals")
    val PRESSURE_PLATES = bind("pressure_plates")
    val PREVENT_MOB_SPAWNING_INSIDE = bind("prevent_mob_spawning_inside")
    val RAILS = bind("rails")
    val REDSTONE_ORES = bind("redstone_ores")
    val SAND = bind("sand")
    val SAPLINGS = bind("saplings")
    val SHULKER_BOXES = bind("shulker_boxes")
    val SIGNS = bind("signs")
    val SLABS = bind("slabs")
    val SMALL_DRIPLEAF_PLACEABLE = bind("small_dripleaf_placeable")
    val SMALL_FLOWERS = bind("small_flowers")
    val SNOW = bind("snow")
    val SOUL_FIRE_BASE_BLOCKS = bind("soul_fire_base_blocks")
    val SOUL_SPEED_BLOCKS = bind("soul_speed_blocks")
    val SPRUCE_LOGS = bind("spruce_logs")
    val STAIRS = bind("stairs")
    val STANDING_SIGNS = bind("standing_signs")
    val STONE_BRICKS = bind("stone_bricks")
    val STONE_ORE_REPLACEABLES = bind("stone_ore_replaceables")
    val STONE_PRESSURE_PLATES = bind("stone_pressure_plates")
    val STRIDER_WARM_BLOCKS = bind("strider_warm_blocks")
    val TALL_FLOWERS = bind("tall_flowers")
    val TRAPDOORS = bind("trapdoors")
    val UNDERWATER_BONEMEALS = bind("underwater_bonemeals")
    val UNSTABLE_BOTTOM_CENTER = bind("unstable_bottom_center")
    val VALID_SPAWN = bind("valid_spawn")
    val WALLS = bind("walls")
    val WALL_CORALS = bind("wall_corals")
    val WALL_POST_OVERRIDE = bind("wall_post_override")
    val WALL_SIGNS = bind("wall_signs")
    val WARPED_STEMS = bind("warped_stems")
    val WART_BLOCKS = bind("wart_blocks")
    val WITHER_IMMUNE = bind("wither_immune")
    val WITHER_SUMMON_BASE_BLOCKS = bind("wither_summon_base_blocks")
    val WOODEN_BUTTONS = bind("wooden_buttons")
    val WOODEN_DOORS = bind("wooden_doors")
    val WOODEN_FENCES = bind("wooden_fences")
    val WOODEN_PRESSURE_PLATES = bind("wooden_pressure_plates")
    val WOODEN_SLABS = bind("wooden_slabs")
    val WOODEN_STAIRS = bind("wooden_stairs")
    val WOODEN_TRAPDOORS = bind("wooden_trapdoors")
    val WOOL = bind("wool")

    private fun bind(name: String) = HELPER.bind(name)

    val tags: TagCollection<Block>
        get() = HELPER.tags
}

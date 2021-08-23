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

    @JvmField val ACACIA_LOGS = get("acacia_logs")
    @JvmField val ANVILS = get("anvil")
    @JvmField val BAMBOO_PLANTABLE_ON = get("bamboo_plantable_on")
    @JvmField val BANNERS = get("banners")
    @JvmField val BASE_STONE_NETHER = get("base_stone_nether")
    @JvmField val BASE_STONE_OVERWORLD = get("base_stone_overworld")
    @JvmField val BEACON_BASE_BLOCKS = get("beacon_base_blocks")
    @JvmField val BEDS = get("beds")
    @JvmField val BEEHIVES = get("beehives")
    @JvmField val BEE_GROWABLES = get("bee_growables")
    @JvmField val BIRCH_LOGS = get("birch_logs")
    @JvmField val BUTTONS = get("buttons")
    @JvmField val CAMPFIRES = get("campfires")
    @JvmField val CANDLE_CAKES = get("candle_cakes")
    @JvmField val CANDLES = get("candles")
    @JvmField val CARPETS = get("carpets")
    @JvmField val CAULDRONS = get("cauldrons")
    @JvmField val CAVE_VINES = get("cave_vines")
    @JvmField val CLIMBABLE = get("climbable")
    @JvmField val COAL_ORES = get("coal_ores")
    @JvmField val COPPER_ORES = get("copper_ores")
    @JvmField val CORALS = get("corals")
    @JvmField val CORAL_BLOCKS = get("coral_blocks")
    @JvmField val CORAL_PLANTS = get("coral_plants")
    @JvmField val CRIMSON_STEMS = get("crimson_stems")
    @JvmField val CROPS = get("crops")
    @JvmField val CRYSTAL_SOUND_BLOCKS = get("crystal_sound_blocks")
    @JvmField val DARK_OAK_LOGS = get("dark_oak_logs")
    @JvmField val DEEPSLATE_ORE_REPLACEABLES = get("deepslate_ore_replaceables")
    @JvmField val DIAMOND_ORES = get("diamond_ores")
    @JvmField val DIRT = get("dirt")
    @JvmField val DOORS = get("doors")
    @JvmField val DRAGON_IMMUNE = get("dragon_immune")
    @JvmField val DRIPSTONE_REPLACEABLE_BLOCKS = get("dripstone_replaceable_blocks")
    @JvmField val EMERALD_ORES = get("emerald_ores")
    @JvmField val ENDERMAN_HOLDABLE = get("enderman_holdable")
    @JvmField val FEATURES_CANNOT_REPLACE = get("features_cannot_replace")
    @JvmField val FENCE_GATES = get("fence_gates")
    @JvmField val FENCES = get("fences")
    @JvmField val FIRE = get("fire")
    @JvmField val FLOWER_POTS = get("flower_pots")
    @JvmField val FLOWERS = get("flowers")
    @JvmField val GEODE_INVALID_BLOCKS = get("geode_invalid_blocks")
    @JvmField val GOLD_ORES = get("gold_ores")
    @JvmField val GUARDED_BY_PIGLINS = get("guarded_by_piglins")
    @JvmField val HOGLIN_REPELLENTS = get("hoglin_repellents")
    @JvmField val ICE = get("ice")
    @JvmField val IMPERMEABLE = get("impermeable")
    @JvmField val INFINIBURN_END = get("infiniburn_end")
    @JvmField val INFINIBURN_NETHER = get("infiniburn_nether")
    @JvmField val INFINIBURN_OVERWORLD = get("infiniburn_overworld")
    @JvmField val INSIDE_STEP_SOUND_BLOCKS = get("inside_step_sound_blocks")
    @JvmField val IRON_ORES = get("iron_ores")
    @JvmField val JUNGLE_LOGS = get("jungle_logs")
    @JvmField val LAPIS_ORES = get("lapis_ores")
    @JvmField val LAVA_POOL_STONE_REPLACEABLES = get("lava_pool_stone_replaceables")
    @JvmField val LEAVES = get("leaves")
    @JvmField val LOGS = get("logs")
    @JvmField val LOGS_THAT_BURN = get("logs_that_burn")
    @JvmField val LUSH_GROUND_REPLACEABLE = get("lush_ground_replaceable")
    @JvmField val MINEABLE_AXE = get("mineable/axe")
    @JvmField val MINEABLE_HOE = get("mineable/hoe")
    @JvmField val MINEABLE_PICKAXE = get("mineable/pickaxe")
    @JvmField val MINEABLE_SHOVEL = get("mineable/shovel")
    @JvmField val MOSS_REPLACEABLE = get("moss_replaceable")
    @JvmField val MUSHROOM_GROW_BLOCK = get("mushroom_grow_block")
    @JvmField val NEEDS_DIAMOND_TOOL = get("needs_diamond_tool")
    @JvmField val NEEDS_IRON_TOOL = get("needs_iron_tool")
    @JvmField val NEEDS_STONE_TOOL = get("needs_stone_tool")
    @JvmField val NON_FLAMMABLE_WOOD = get("non_flammable_wood")
    @JvmField val NYLIUM = get("nylium")
    @JvmField val OAK_LOGS = get("oak_logs")
    @JvmField val OCCLUDES_VIBRATION_SIGNALS = get("occludes_vibration_signals")
    @JvmField val PIGLIN_REPELLENTS = get("piglin_repellents")
    @JvmField val PLANKS = get("planks")
    @JvmField val PORTALS = get("portals")
    @JvmField val PRESSURE_PLATES = get("pressure_plates")
    @JvmField val PREVENT_MOB_SPAWNING_INSIDE = get("prevent_mob_spawning_inside")
    @JvmField val RAILS = get("rails")
    @JvmField val REDSTONE_ORES = get("redstone_ores")
    @JvmField val SAND = get("sand")
    @JvmField val SAPLINGS = get("saplings")
    @JvmField val SHULKER_BOXES = get("shulker_boxes")
    @JvmField val SIGNS = get("signs")
    @JvmField val SLABS = get("slabs")
    @JvmField val SMALL_DRIPLEAF_PLACEABLE = get("small_dripleaf_placeable")
    @JvmField val SMALL_FLOWERS = get("small_flowers")
    @JvmField val SNOW = get("snow")
    @JvmField val SOUL_FIRE_BASE_BLOCKS = get("soul_fire_base_blocks")
    @JvmField val SOUL_SPEED_BLOCKS = get("soul_speed_blocks")
    @JvmField val SPRUCE_LOGS = get("spruce_logs")
    @JvmField val STAIRS = get("stairs")
    @JvmField val STANDING_SIGNS = get("standing_signs")
    @JvmField val STONE_BRICKS = get("stone_bricks")
    @JvmField val STONE_ORE_REPLACEABLES = get("stone_ore_replaceables")
    @JvmField val STONE_PRESSURE_PLATES = get("stone_pressure_plates")
    @JvmField val STRIDER_WARM_BLOCKS = get("strider_warm_blocks")
    @JvmField val TALL_FLOWERS = get("tall_flowers")
    @JvmField val TRAPDOORS = get("trapdoors")
    @JvmField val UNDERWATER_BONEMEALS = get("underwater_bonemeals")
    @JvmField val UNSTABLE_BOTTOM_CENTER = get("unstable_bottom_center")
    @JvmField val VALID_SPAWN = get("valid_spawn")
    @JvmField val WALLS = get("walls")
    @JvmField val WALL_CORALS = get("wall_corals")
    @JvmField val WALL_POST_OVERRIDE = get("wall_post_override")
    @JvmField val WALL_SIGNS = get("wall_signs")
    @JvmField val WARPED_STEMS = get("warped_stems")
    @JvmField val WART_BLOCKS = get("wart_blocks")
    @JvmField val WITHER_IMMUNE = get("wither_immune")
    @JvmField val WITHER_SUMMON_BASE_BLOCKS = get("wither_summon_base_blocks")
    @JvmField val WOODEN_BUTTONS = get("wooden_buttons")
    @JvmField val WOODEN_DOORS = get("wooden_doors")
    @JvmField val WOODEN_FENCES = get("wooden_fences")
    @JvmField val WOODEN_PRESSURE_PLATES = get("wooden_pressure_plates")
    @JvmField val WOODEN_SLABS = get("wooden_slabs")
    @JvmField val WOODEN_STAIRS = get("wooden_stairs")
    @JvmField val WOODEN_TRAPDOORS = get("wooden_trapdoors")
    @JvmField val WOOL = get("wool")

    @JvmStatic
    operator fun get(key: Key) = TagManager[TagTypes.BLOCKS, key.asString()]

    @JvmStatic
    private fun get(name: String) = TagManager[TagTypes.BLOCKS, "minecraft:$name"]!!
}

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

object ItemTags {

    @JvmField val WOOL = get("wool")
    @JvmField val PLANKS = get("planks")
    @JvmField val STONE_BRICKS = get("stone_bricks")
    @JvmField val WOODEN_BUTTONS = get("wooden_buttons")
    @JvmField val BUTTONS = get("buttons")
    @JvmField val CARPETS = get("carpets")
    @JvmField val WOODEN_DOORS = get("wooden_doors")
    @JvmField val WOODEN_STAIRS = get("wooden_stairs")
    @JvmField val WOODEN_SLABS = get("wooden_slabs")
    @JvmField val WOODEN_FENCES = get("wooden_fences")
    @JvmField val WOODEN_PRESSURE_PLATES = get("wooden_pressure_plates")
    @JvmField val WOODEN_TRAPDOORS = get("wooden_trapdoors")
    @JvmField val DOORS = get("doors")
    @JvmField val SAPLINGS = get("saplings")
    @JvmField val LOGS_THAT_BURN = get("logs_that_burn")
    @JvmField val LOGS = get("logs")
    @JvmField val DARK_OAK_LOGS = get("dark_oak_logs")
    @JvmField val OAK_LOGS = get("oak_logs")
    @JvmField val BIRCH_LOGS = get("birch_logs")
    @JvmField val ACACIA_LOGS = get("acacia_logs")
    @JvmField val JUNGLE_LOGS = get("jungle_logs")
    @JvmField val SPRUCE_LOGS = get("spruce_logs")
    @JvmField val CRIMSON_STEMS = get("crimson_stems")
    @JvmField val WARPED_STEMS = get("warped_stems")
    @JvmField val BANNERS = get("banners")
    @JvmField val SAND = get("sand")
    @JvmField val STAIRS = get("stairs")
    @JvmField val SLABS = get("slabs")
    @JvmField val WALLS = get("walls")
    @JvmField val ANVIL = get("anvil")
    @JvmField val RAILS = get("rails")
    @JvmField val LEAVES = get("leaves")
    @JvmField val TRAPDOORS = get("trapdoors")
    @JvmField val SMALL_FLOWERS = get("small_flowers")
    @JvmField val BEDS = get("beds")
    @JvmField val FENCES = get("fences")
    @JvmField val TALL_FLOWERS = get("tall_flowers")
    @JvmField val FLOWERS = get("flowers")
    @JvmField val PIGLIN_REPELLENTS = get("piglin_repellents")
    @JvmField val PIGLIN_LOVED = get("piglin_loved")
    @JvmField val IGNORED_BY_PIGLIN_BABIES = get("ignored_by_piglin_babies")
    @JvmField val PIGLIN_FOOD = get("piglin_food")
    @JvmField val FOX_FOOD = get("fox_food")
    @JvmField val GOLD_ORES = get("gold_ores")
    @JvmField val IRON_ORES = get("iron_ores")
    @JvmField val DIAMOND_ORES = get("diamond_ores")
    @JvmField val REDSTONE_ORES = get("redstone_ores")
    @JvmField val LAPIS_ORES = get("lapis_ores")
    @JvmField val COAL_ORES = get("coal_ores")
    @JvmField val EMERALD_ORES = get("emerald_ores")
    @JvmField val COPPER_ORES = get("copper_ores")
    @JvmField val NON_FLAMMABLE_WOOD = get("non_flammable_wood")
    @JvmField val SOUL_FIRE_BASE_BLOCKS = get("soul_fire_base_blocks")
    @JvmField val CANDLES = get("candles")
    @JvmField val BOATS = get("boats")
    @JvmField val FISHES = get("fishes")
    @JvmField val SIGNS = get("signs")
    @JvmField val MUSIC_DISCS = get("music_discs")
    @JvmField val CREEPER_DROP_MUSIC_DISCS = get("creeper_drop_music_discs")
    @JvmField val COALS = get("coals")
    @JvmField val ARROWS = get("arrows")
    @JvmField val LECTERN_BOOKS = get("lectern_books")
    @JvmField val BEACON_PAYMENT_ITEMS = get("beacon_payment_items")
    @JvmField val STONE_TOOL_MATERIALS = get("stone_tool_materials")
    @JvmField val STONE_CRAFTING_MATERIALS = get("stone_crafting_materials")
    @JvmField val FREEZE_IMMUNE_WEARABLES = get("freeze_immune_wearables")
    @JvmField val AXOLOTL_TEMPT_ITEMS = get("axolotl_tempt_items")
    @JvmField val OCCLUDES_VIBRATION_SIGNALS = get("occludes_vibration_signals")
    @JvmField val CLUSTER_MAX_HARVESTABLES = get("cluster_max_harvestables")

    @JvmStatic
    operator fun get(key: Key) = TagManager[TagTypes.ITEMS, key.asString()]

    @JvmStatic
    private fun get(name: String) = TagManager[TagTypes.ITEMS, "minecraft:$name"]!!
}

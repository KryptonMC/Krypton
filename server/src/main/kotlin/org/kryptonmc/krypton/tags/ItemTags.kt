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

import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.resource.ResourceKeys

object ItemTags {

    val HELPER = StaticTags.create(ResourceKeys.ITEM, "tags/items")

    val WOOL = bind("wool")
    val PLANKS = bind("planks")
    val STONE_BRICKS = bind("stone_bricks")
    val WOODEN_BUTTONS = bind("wooden_buttons")
    val BUTTONS = bind("buttons")
    val CARPETS = bind("carpets")
    val WOODEN_DOORS = bind("wooden_doors")
    val WOODEN_STAIRS = bind("wooden_stairs")
    val WOODEN_SLABS = bind("wooden_slabs")
    val WOODEN_FENCES = bind("wooden_fences")
    val WOODEN_PRESSURE_PLATES = bind("wooden_pressure_plates")
    val WOODEN_TRAPDOORS = bind("wooden_trapdoors")
    val DOORS = bind("doors")
    val SAPLINGS = bind("saplings")
    val LOGS_THAT_BURN = bind("logs_that_burn")
    val LOGS = bind("logs")
    val DARK_OAK_LOGS = bind("dark_oak_logs")
    val OAK_LOGS = bind("oak_logs")
    val BIRCH_LOGS = bind("birch_logs")
    val ACACIA_LOGS = bind("acacia_logs")
    val JUNGLE_LOGS = bind("jungle_logs")
    val SPRUCE_LOGS = bind("spruce_logs")
    val CRIMSON_STEMS = bind("crimson_stems")
    val WARPED_STEMS = bind("warped_stems")
    val BANNERS = bind("banners")
    val SAND = bind("sand")
    val STAIRS = bind("stairs")
    val SLABS = bind("slabs")
    val WALLS = bind("walls")
    val ANVIL = bind("anvil")
    val RAILS = bind("rails")
    val LEAVES = bind("leaves")
    val TRAPDOORS = bind("trapdoors")
    val SMALL_FLOWERS = bind("small_flowers")
    val BEDS = bind("beds")
    val FENCES = bind("fences")
    val TALL_FLOWERS = bind("tall_flowers")
    val FLOWERS = bind("flowers")
    val PIGLIN_REPELLENTS = bind("piglin_repellents")
    val PIGLIN_LOVED = bind("piglin_loved")
    val IGNORED_BY_PIGLIN_BABIES = bind("ignored_by_piglin_babies")
    val PIGLIN_FOOD = bind("piglin_food")
    val FOX_FOOD = bind("fox_food")
    val GOLD_ORES = bind("gold_ores")
    val IRON_ORES = bind("iron_ores")
    val DIAMOND_ORES = bind("diamond_ores")
    val REDSTONE_ORES = bind("redstone_ores")
    val LAPIS_ORES = bind("lapis_ores")
    val COAL_ORES = bind("coal_ores")
    val EMERALD_ORES = bind("emerald_ores")
    val COPPER_ORES = bind("copper_ores")
    val NON_FLAMMABLE_WOOD = bind("non_flammable_wood")
    val SOUL_FIRE_BASE_BLOCKS = bind("soul_fire_base_blocks")
    val CANDLES = bind("candles")
    val BOATS = bind("boats")
    val FISHES = bind("fishes")
    val SIGNS = bind("signs")
    val MUSIC_DISCS = bind("music_discs")
    val CREEPER_DROP_MUSIC_DISCS = bind("creeper_drop_music_discs")
    val COALS = bind("coals")
    val ARROWS = bind("arrows")
    val LECTERN_BOOKS = bind("lectern_books")
    val BEACON_PAYMENT_ITEMS = bind("beacon_payment_items")
    val STONE_TOOL_MATERIALS = bind("stone_tool_materials")
    val STONE_CRAFTING_MATERIALS = bind("stone_crafting_materials")
    val FREEZE_IMMUNE_WEARABLES = bind("freeze_immune_wearables")
    val AXOLOTL_TEMPT_ITEMS = bind("axolotl_tempt_items")
    val OCCLUDES_VIBRATION_SIGNALS = bind("occludes_vibration_signals")
    val CLUSTER_MAX_HARVESTABLES = bind("cluster_max_harvestables")

    private fun bind(name: String) = HELPER.bind(name)

    val tags: TagCollection<ItemType>
        get() = HELPER.tags
}

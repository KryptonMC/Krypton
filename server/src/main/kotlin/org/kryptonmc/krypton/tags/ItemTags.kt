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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryKeys

object ItemTags {

    val WOOL = get("wool")
    val PLANKS = get("planks")
    val STONE_BRICKS = get("stone_bricks")
    val WOODEN_BUTTONS = get("wooden_buttons")
    val BUTTONS = get("buttons")
    val CARPETS = get("carpets")
    val WOODEN_DOORS = get("wooden_doors")
    val WOODEN_STAIRS = get("wooden_stairs")
    val WOODEN_SLABS = get("wooden_slabs")
    val WOODEN_FENCES = get("wooden_fences")
    val WOODEN_PRESSURE_PLATES = get("wooden_pressure_plates")
    val WOODEN_TRAPDOORS = get("wooden_trapdoors")
    val DOORS = get("doors")
    val SAPLINGS = get("saplings")
    val LOGS_THAT_BURN = get("logs_that_burn")
    val LOGS = get("logs")
    val DARK_OAK_LOGS = get("dark_oak_logs")
    val OAK_LOGS = get("oak_logs")
    val BIRCH_LOGS = get("birch_logs")
    val ACACIA_LOGS = get("acacia_logs")
    val JUNGLE_LOGS = get("jungle_logs")
    val SPRUCE_LOGS = get("spruce_logs")
    val CRIMSON_STEMS = get("crimson_stems")
    val WARPED_STEMS = get("warped_stems")
    val BANNERS = get("banners")
    val SAND = get("sand")
    val STAIRS = get("stairs")
    val SLABS = get("slabs")
    val WALLS = get("walls")
    val ANVIL = get("anvil")
    val RAILS = get("rails")
    val LEAVES = get("leaves")
    val TRAPDOORS = get("trapdoors")
    val SMALL_FLOWERS = get("small_flowers")
    val BEDS = get("beds")
    val FENCES = get("fences")
    val TALL_FLOWERS = get("tall_flowers")
    val FLOWERS = get("flowers")
    val PIGLIN_REPELLENTS = get("piglin_repellents")
    val PIGLIN_LOVED = get("piglin_loved")
    val IGNORED_BY_PIGLIN_BABIES = get("ignored_by_piglin_babies")
    val PIGLIN_FOOD = get("piglin_food")
    val FOX_FOOD = get("fox_food")
    val GOLD_ORES = get("gold_ores")
    val IRON_ORES = get("iron_ores")
    val DIAMOND_ORES = get("diamond_ores")
    val REDSTONE_ORES = get("redstone_ores")
    val LAPIS_ORES = get("lapis_ores")
    val COAL_ORES = get("coal_ores")
    val EMERALD_ORES = get("emerald_ores")
    val COPPER_ORES = get("copper_ores")
    val NON_FLAMMABLE_WOOD = get("non_flammable_wood")
    val SOUL_FIRE_BASE_BLOCKS = get("soul_fire_base_blocks")
    val CANDLES = get("candles")
    val BOATS = get("boats")
    val FISHES = get("fishes")
    val SIGNS = get("signs")
    val MUSIC_DISCS = get("music_discs")
    val CREEPER_DROP_MUSIC_DISCS = get("creeper_drop_music_discs")
    val COALS = get("coals")
    val ARROWS = get("arrows")
    val LECTERN_BOOKS = get("lectern_books")
    val BEACON_PAYMENT_ITEMS = get("beacon_payment_items")
    val STONE_TOOL_MATERIALS = get("stone_tool_materials")
    val STONE_CRAFTING_MATERIALS = get("stone_crafting_materials")
    val FREEZE_IMMUNE_WEARABLES = get("freeze_immune_wearables")
    val AXOLOTL_TEMPT_ITEMS = get("axolotl_tempt_items")
    val OCCLUDES_VIBRATION_SIGNALS = get("occludes_vibration_signals")
    val CLUSTER_MAX_HARVESTABLES = get("cluster_max_harvestables")

    private fun get(name: String) = KryptonTagManager.load(Key.key(name), RegistryKeys.ITEM.location, "items", Registries.ITEM)
}

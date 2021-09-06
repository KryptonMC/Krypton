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
package org.kryptonmc.krypton.util.converter.versions

import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converters.RenameAdvancementsConverter

object V1501 {

    private const val VERSION = MCVersions.V1_13_PRE1
    private val RENAMES = mapOf(
        "minecraft:recipes/brewing/speckled_melon" to "minecraft:recipes/brewing/glistering_melon_slice",
        "minecraft:recipes/building_blocks/black_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/black_terracotta",
        "minecraft:recipes/building_blocks/blue_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/blue_terracotta",
        "minecraft:recipes/building_blocks/brown_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/brown_terracotta",
        "minecraft:recipes/building_blocks/cyan_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/cyan_terracotta",
        "minecraft:recipes/building_blocks/gray_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/gray_terracotta",
        "minecraft:recipes/building_blocks/green_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/green_terracotta",
        "minecraft:recipes/building_blocks/light_blue_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/light_blue_terracotta",
        "minecraft:recipes/building_blocks/light_gray_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/light_gray_terracotta",
        "minecraft:recipes/building_blocks/lime_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/lime_terracotta",
        "minecraft:recipes/building_blocks/magenta_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/magenta_terracotta",
        "minecraft:recipes/building_blocks/orange_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/orange_terracotta",
        "minecraft:recipes/building_blocks/pink_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/pink_terracotta",
        "minecraft:recipes/building_blocks/purple_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/purple_terracotta",
        "minecraft:recipes/building_blocks/red_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/red_terracotta",
        "minecraft:recipes/building_blocks/white_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/white_terracotta",
        "minecraft:recipes/building_blocks/yellow_stained_hardened_clay" to
                "minecraft:recipes/building_blocks/yellow_terracotta",
        "minecraft:recipes/building_blocks/acacia_wooden_slab" to "minecraft:recipes/building_blocks/acacia_slab",
        "minecraft:recipes/building_blocks/birch_wooden_slab" to "minecraft:recipes/building_blocks/birch_slab",
        "minecraft:recipes/building_blocks/dark_oak_wooden_slab" to "minecraft:recipes/building_blocks/dark_oak_slab",
        "minecraft:recipes/building_blocks/jungle_wooden_slab" to "minecraft:recipes/building_blocks/jungle_slab",
        "minecraft:recipes/building_blocks/oak_wooden_slab" to "minecraft:recipes/building_blocks/oak_slab",
        "minecraft:recipes/building_blocks/spruce_wooden_slab" to "minecraft:recipes/building_blocks/spruce_slab",
        "minecraft:recipes/building_blocks/brick_block" to "minecraft:recipes/building_blocks/bricks",
        "minecraft:recipes/building_blocks/chiseled_stonebrick" to
                "minecraft:recipes/building_blocks/chiseled_stone_bricks",
        "minecraft:recipes/building_blocks/end_bricks" to "minecraft:recipes/building_blocks/end_stone_bricks",
        "minecraft:recipes/building_blocks/lit_pumpkin" to "minecraft:recipes/building_blocks/jack_o_lantern",
        "minecraft:recipes/building_blocks/magma" to "minecraft:recipes/building_blocks/magma_block",
        "minecraft:recipes/building_blocks/melon_block" to "minecraft:recipes/building_blocks/melon",
        "minecraft:recipes/building_blocks/mossy_stonebrick" to "minecraft:recipes/building_blocks/mossy_stone_bricks",
        "minecraft:recipes/building_blocks/nether_brick" to "minecraft:recipes/building_blocks/nether_bricks",
        "minecraft:recipes/building_blocks/pillar_quartz_block" to "minecraft:recipes/building_blocks/quartz_pillar",
        "minecraft:recipes/building_blocks/red_nether_brick" to "minecraft:recipes/building_blocks/red_nether_bricks",
        "minecraft:recipes/building_blocks/snow" to "minecraft:recipes/building_blocks/snow_block",
        "minecraft:recipes/building_blocks/smooth_red_sandstone" to "minecraft:recipes/building_blocks/cut_red_sandstone",
        "minecraft:recipes/building_blocks/smooth_sandstone" to "minecraft:recipes/building_blocks/cut_sandstone",
        "minecraft:recipes/building_blocks/stonebrick" to "minecraft:recipes/building_blocks/stone_bricks",
        "minecraft:recipes/building_blocks/stone_stairs" to "minecraft:recipes/building_blocks/cobblestone_stairs",
        "minecraft:recipes/building_blocks/string_to_wool" to "minecraft:recipes/building_blocks/white_wool_from_string",
        "minecraft:recipes/decorations/fence" to "minecraft:recipes/decorations/oak_fence",
        "minecraft:recipes/decorations/purple_shulker_box" to "minecraft:recipes/decorations/shulker_box",
        "minecraft:recipes/decorations/slime" to "minecraft:recipes/decorations/slime_block",
        "minecraft:recipes/decorations/snow_layer" to "minecraft:recipes/decorations/snow",
        "minecraft:recipes/misc/bone_meal_from_block" to "minecraft:recipes/misc/bone_meal_from_bone_block",
        "minecraft:recipes/misc/bone_meal_from_bone" to "minecraft:recipes/misc/bone_meal",
        "minecraft:recipes/misc/gold_ingot_from_block" to "minecraft:recipes/misc/gold_ingot_from_gold_block",
        "minecraft:recipes/misc/iron_ingot_from_block" to "minecraft:recipes/misc/iron_ingot_from_iron_block",
        "minecraft:recipes/redstone/fence_gate" to "minecraft:recipes/redstone/oak_fence_gate",
        "minecraft:recipes/redstone/noteblock" to "minecraft:recipes/redstone/note_block",
        "minecraft:recipes/redstone/trapdoor" to "minecraft:recipes/redstone/oak_trapdoor",
        "minecraft:recipes/redstone/wooden_button" to "minecraft:recipes/redstone/oak_button",
        "minecraft:recipes/redstone/wooden_door" to "minecraft:recipes/redstone/oak_door",
        "minecraft:recipes/redstone/wooden_pressure_plate" to "minecraft:recipes/redstone/oak_pressure_plate",
        "minecraft:recipes/transportation/boat" to "minecraft:recipes/transportation/oak_boat",
        "minecraft:recipes/transportation/golden_rail" to "minecraft:recipes/transportation/powered_rail"
    )

    fun register() = RenameAdvancementsConverter.register(VERSION, RENAMES::get)
}

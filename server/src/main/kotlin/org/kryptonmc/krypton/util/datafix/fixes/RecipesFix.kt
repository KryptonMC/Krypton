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
package org.kryptonmc.krypton.util.datafix.fixes

import com.mojang.datafixers.schemas.Schema
import org.kryptonmc.krypton.util.datafix.fixes.name.RenameRecipesFix

class RecipesFix(outputSchema: Schema, changesType: Boolean) : RenameRecipesFix(outputSchema, changesType, "Recipes fix", { RECIPES.getOrDefault(it, it) }) {

    companion object {

        private val RECIPES = mapOf(
            "minecraft:acacia_wooden_slab" to "minecraft:acacia_slab",
            "minecraft:birch_wooden_slab" to "minecraft:birch_slab",
            "minecraft:black_stained_hardened_clay" to "minecraft:black_terracotta",
            "minecraft:blue_stained_hardened_clay" to "minecraft:blue_terracotta",
            "minecraft:boat" to "minecraft:oak_boat",
            "minecraft:bone_meal_from_block" to "minecraft:bone_meal_from_bone_block",
            "minecraft:bone_meal_from_bone" to "minecraft:bone_meal",
            "minecraft:brick_block" to "minecraft:bricks",
            "minecraft:brown_stained_hardened_clay" to "minecraft:brown_terracotta",
            "minecraft:chiseled_stonebrick" to "minecraft:chiseled_stone_bricks",
            "minecraft:cyan_stained_hardened_clay" to "minecraft:cyan_terracotta",
            "minecraft:dark_oak_wooden_slab" to "minecraft:dark_oak_slab",
            "minecraft:end_bricks" to "minecraft:end_stone_bricks",
            "minecraft:fence_gate" to "minecraft:oak_fence_gate",
            "minecraft:fence" to "minecraft:oak_fence",
            "minecraft:golden_rail" to "minecraft:powered_rail",
            "minecraft:gold_ingot_from_block" to "minecraft:gold_ingot_from_gold_block",
            "minecraft:gray_stained_hardened_clay" to "minecraft:gray_terracotta",
            "minecraft:green_stained_hardened_clay" to "minecraft:green_terracotta",
            "minecraft:iron_ingot_from_block" to "minecraft:iron_ingot_from_iron_block",
            "minecraft:jungle_wooden_slab" to "minecraft:jungle_slab",
            "minecraft:light_blue_stained_hardened_clay" to "minecraft:light_blue_terracotta",
            "minecraft:light_gray_stained_hardened_clay" to "minecraft:light_gray_terracotta",
            "minecraft:lime_stained_hardened_clay" to "minecraft:lime_terracotta",
            "minecraft:lit_pumpkin" to "minecraft:jack_o_lantern",
            "minecraft:magenta_stained_hardened_clay" to "minecraft:magenta_terracotta",
            "minecraft:magma" to "minecraft:magma_block",
            "minecraft:melon_block" to "minecraft:melon",
            "minecraft:mossy_stonebrick" to "minecraft:mossy_stone_bricks",
            "minecraft:noteblock" to "minecraft:note_block",
            "minecraft:oak_wooden_slab" to "minecraft:oak_slab",
            "minecraft:orange_stained_hardened_clay" to "minecraft:orange_terracotta",
            "minecraft:pillar_quartz_block" to "minecraft:quartz_pillar",
            "minecraft:pink_stained_hardened_clay" to "minecraft:pink_terracotta",
            "minecraft:purple_shulker_box" to "minecraft:shulker_box",
            "minecraft:purple_stained_hardened_clay" to "minecraft:purple_terracotta",
            "minecraft:red_nether_brick" to "minecraft:red_nether_bricks",
            "minecraft:red_stained_hardened_clay" to "minecraft:red_terracotta",
            "minecraft:slime" to "minecraft:slime_block",
            "minecraft:smooth_red_sandstone" to "minecraft:cut_red_sandstone",
            "minecraft:smooth_sandstone" to "minecraft:cut_sandstone",
            "minecraft:snow_layer" to "minecraft:snow",
            "minecraft:snow" to "minecraft:snow_block",
            "minecraft:speckled_melon" to "minecraft:glistering_melon_slice",
            "minecraft:spruce_wooden_slab" to "minecraft:spruce_slab",
            "minecraft:stonebrick" to "minecraft:stone_bricks",
            "minecraft:stone_stairs" to "minecraft:cobblestone_stairs",
            "minecraft:string_to_wool" to "minecraft:white_wool_from_string",
            "minecraft:trapdoor" to "minecraft:oak_trapdoor",
            "minecraft:white_stained_hardened_clay" to "minecraft:white_terracotta",
            "minecraft:wooden_button" to "minecraft:oak_button",
            "minecraft:wooden_door" to "minecraft:oak_door",
            "minecraft:wooden_pressure_plate" to "minecraft:oak_pressure_plate",
            "minecraft:yellow_stained_hardened_clay" to "minecraft:yellow_terracotta"
        )
    }
}

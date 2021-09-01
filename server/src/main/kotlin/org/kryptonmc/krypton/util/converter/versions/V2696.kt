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
import org.kryptonmc.krypton.util.converters.RenameBlocksConverter
import org.kryptonmc.krypton.util.converters.RenameItemsConverter

object V2696 {

    private const val VERSION = MCVersions.V21W07A + 1
    private val RENAMES = mapOf(
        "minecraft:grimstone" to "minecraft:deepslate",
        "minecraft:grimstone_slab" to "minecraft:cobbled_deepslate_slab",
        "minecraft:grimstone_stairs" to "minecraft:cobbled_deepslate_stairs",
        "minecraft:grimstone_wall" to "minecraft:cobbled_deepslate_wall",
        "minecraft:polished_grimstone" to "minecraft:polished_deepslate",
        "minecraft:polished_grimstone_slab" to "minecraft:polished_deepslate_slab",
        "minecraft:polished_grimstone_stairs" to "minecraft:polished_deepslate_stairs",
        "minecraft:polished_grimstone_wall" to "minecraft:polished_deepslate_wall",
        "minecraft:grimstone_tiles" to "minecraft:deepslate_tiles",
        "minecraft:grimstone_tile_slab" to "minecraft:deepslate_tile_slab",
        "minecraft:grimstone_tile_stairs" to "minecraft:deepslate_tile_stairs",
        "minecraft:grimstone_tile_wall" to "minecraft:deepslate_tile_wall",
        "minecraft:grimstone_bricks" to "minecraft:deepslate_bricks",
        "minecraft:grimstone_brick_slab" to "minecraft:deepslate_brick_slab",
        "minecraft:grimstone_brick_stairs" to "minecraft:deepslate_brick_stairs",
        "minecraft:grimstone_brick_wall" to "minecraft:deepslate_brick_wall",
        "minecraft:chiseled_grimstone" to "minecraft:chiseled_deepslate",
    )

    fun register() {
        RenameItemsConverter.register(VERSION, RENAMES::get)
        RenameBlocksConverter.register(VERSION, RENAMES::get)
    }
}

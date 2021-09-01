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

object V2690 {

    private const val VERSION = MCVersions.V21W05A
    private val RENAMES = mapOf(
        "minecraft:weathered_copper_block" to "minecraft:oxidized_copper_block",
        "minecraft:semi_weathered_copper_block" to "minecraft:weathered_copper_block",
        "minecraft:lightly_weathered_copper_block" to "minecraft:exposed_copper_block",
        "minecraft:weathered_cut_copper" to "minecraft:oxidized_cut_copper",
        "minecraft:semi_weathered_cut_copper" to "minecraft:weathered_cut_copper",
        "minecraft:lightly_weathered_cut_copper" to "minecraft:exposed_cut_copper",
        "minecraft:weathered_cut_copper_stairs" to "minecraft:oxidized_cut_copper_stairs",
        "minecraft:semi_weathered_cut_copper_stairs" to "minecraft:weathered_cut_copper_stairs",
        "minecraft:lightly_weathered_cut_copper_stairs" to "minecraft:exposed_cut_copper_stairs",
        "minecraft:weathered_cut_copper_slab" to "minecraft:oxidized_cut_copper_slab",
        "minecraft:semi_weathered_cut_copper_slab" to "minecraft:weathered_cut_copper_slab",
        "minecraft:lightly_weathered_cut_copper_slab" to "minecraft:exposed_cut_copper_slab",
        "minecraft:waxed_semi_weathered_copper" to "minecraft:waxed_weathered_copper",
        "minecraft:waxed_lightly_weathered_copper" to "minecraft:waxed_exposed_copper",
        "minecraft:waxed_semi_weathered_cut_copper" to "minecraft:waxed_weathered_cut_copper",
        "minecraft:waxed_lightly_weathered_cut_copper" to "minecraft:waxed_exposed_cut_copper",
        "minecraft:waxed_semi_weathered_cut_copper_stairs" to "minecraft:waxed_weathered_cut_copper_stairs",
        "minecraft:waxed_lightly_weathered_cut_copper_stairs" to "minecraft:waxed_exposed_cut_copper_stairs",
        "minecraft:waxed_semi_weathered_cut_copper_slab" to "minecraft:waxed_weathered_cut_copper_slab",
        "minecraft:waxed_lightly_weathered_cut_copper_slab" to "minecraft:waxed_exposed_cut_copper_slab",
    )

    fun register() {
        RenameItemsConverter.register(VERSION, RENAMES::get)
        RenameBlocksConverter.register(VERSION, RENAMES::get)
    }
}

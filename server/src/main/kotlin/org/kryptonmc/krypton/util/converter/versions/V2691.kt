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

object V2691 {

    private const val VERSION = MCVersions.V21W05A + 1
    private val RENAMES = mapOf(
        "minecraft:waxed_copper" to "minecraft:waxed_copper_block",
        "minecraft:oxidized_copper_block" to "minecraft:oxidized_copper",
        "minecraft:weathered_copper_block" to "minecraft:weathered_copper",
        "minecraft:exposed_copper_block" to "minecraft:exposed_copper",
    )

    fun register() {
        RenameItemsConverter.register(VERSION, RENAMES::get)
        RenameBlocksConverter.register(VERSION, RENAMES::get)
    }
}

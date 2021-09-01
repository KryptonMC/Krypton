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

import ca.spottedleaf.dataconverter.types.MapType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converters.RenameAdvancementsConverter

object V2503 {

    private const val VERSION = MCVersions.V1_15_2 + 273
    private val WALL_BLOCKS = setOf(
        "minecraft:andesite_wall",
        "minecraft:brick_wall",
        "minecraft:cobblestone_wall",
        "minecraft:diorite_wall",
        "minecraft:end_stone_brick_wall",
        "minecraft:granite_wall",
        "minecraft:mossy_cobblestone_wall",
        "minecraft:mossy_stone_brick_wall",
        "minecraft:nether_brick_wall",
        "minecraft:prismarine_wall",
        "minecraft:red_nether_brick_wall",
        "minecraft:red_sandstone_wall",
        "minecraft:sandstone_wall",
        "minecraft:stone_brick_wall"
    )

    fun register() {
        MCTypeRegistry.BLOCK_STATE.addStructureConverter(VERSION) { data, _, _ ->
            if (!WALL_BLOCKS.contains(data.getString("Name"))) return@addStructureConverter null
            val properties = data.getMap<String>("Properties") ?: return@addStructureConverter null

            properties.changeWallProperty("east")
            properties.changeWallProperty("west")
            properties.changeWallProperty("north")
            properties.changeWallProperty("south")
            null
        }
        RenameAdvancementsConverter.register(VERSION, mapOf("minecraft:recipes/misc/composter" to "minecraft:recipes/decorations/composter")::get)
    }

    private fun MapType<String>.changeWallProperty(path: String) {
        getString(path)?.let { setString(path, if (it == "true") "low" else "none") }
    }
}

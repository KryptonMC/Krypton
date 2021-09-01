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
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V2518 {

    private const val VERSION = MCVersions.V20W12A + 3
    private val FACING_RENAMES = mapOf(
        "down" to "down_south",
        "up" to "up_north",
        "north" to "north_up",
        "south" to "south_up",
        "west" to "west_up",
        "east" to "east_up"
    )

    fun register() {
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:jigsaw", VERSION) { data, _, _ ->
            val type = data.getString("attachment_type", "minecraft:empty")!!
            val pool = data.getString("target_pool", "minecraft:empty")!!
            data.remove("attachment_type")
            data.remove("target_pool")

            data.setString("name", type)
            data.setString("target", type)
            data.setString("pool", pool)
            null
        }

        MCTypeRegistry.BLOCK_STATE.addStructureConverter(VERSION) { data, _, _ ->
            if (data.getString("Name") != "minecraft:jigsaw") return@addStructureConverter null
            val properties = data.getMap<String>("Properties") ?: return@addStructureConverter null

            val facing = properties.getString("facing", "north")!!
            properties.remove("facing")
            properties.setString("orientation", FACING_RENAMES.getOrDefault(facing, facing))
            null
        }
    }
}

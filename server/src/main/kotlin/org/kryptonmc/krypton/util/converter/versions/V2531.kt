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

object V2531 {

    private const val VERSION = MCVersions.V20W17A + 2

    fun register() = MCTypeRegistry.BLOCK_STATE.addStructureConverter(VERSION) { data, _, _ ->
        if (data.getString("Name") != "redstone_wire") return@addStructureConverter null
        val properties = data.getMap<String>("Properties") ?: return@addStructureConverter null

        val east = properties.getString("east", "none")!!
        val west = properties.getString("west", "none")!!
        val north = properties.getString("north", "none")!!
        val south = properties.getString("south", "none")!!

        val connectedX = isConnected(east) || isConnected(west)
        val connectedZ = isConnected(north) || isConnected(south)

        val newEast = if (!isConnected(east) && !connectedZ) "side" else east
        val newWest = if (!isConnected(west) && !connectedZ) "side" else west
        val newNorth = if (!isConnected(north) && !connectedX) "side" else north
        val newSouth = if (!isConnected(south) && !connectedX) "side" else south

        if (properties.hasKey("east")) properties.setString("east", newEast)
        if (properties.hasKey("west")) properties.setString("west", newWest)
        if (properties.hasKey("north")) properties.setString("north", newNorth)
        if (properties.hasKey("south")) properties.setString("south", newSouth)
        null
    }

    private fun isConnected(facing: String) = facing != "none"
}

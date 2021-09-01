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
import org.kryptonmc.krypton.util.converters.RenameBlocksConverter
import org.kryptonmc.krypton.util.converters.RenameItemsConverter

object V1484 {

    private const val VERSION = MCVersions.V18W19A

    fun register() {
        val renamed = mapOf(
            "minecraft:sea_grass" to "minecraft:seagrass",
            "minecraft:tall_sea_grass" to "minecraft:tall_seagrass"
        )

        RenameItemsConverter.register(VERSION, renamed::get)
        RenameBlocksConverter.register(VERSION, renamed::get)

        MCTypeRegistry.CHUNK.addStructureConverter(VERSION) { data, _, _ ->
            val level = data.getMap<String>("Level") ?: return@addStructureConverter null
            val heightmaps = level.getMap<String>("Heightmaps") ?: return@addStructureConverter null

            heightmaps.getGeneric("LIQUID")?.let {
                heightmaps.remove("LIQUID")
                heightmaps.setGeneric("WORLD_SURFACE_WG", it)
            }
            heightmaps.getGeneric("SOLID")?.let {
                heightmaps.remove("SOLID")
                heightmaps.setGeneric("OCEAN_FLOOR_WG", it)
                heightmaps.setGeneric("OCEAN_FLOOR", it)
            }
            heightmaps.getGeneric("LIGHT")?.let {
                heightmaps.remove("LIGHT")
                heightmaps.setGeneric("LIGHT_BLOCKING", it)
            }
            heightmaps.getGeneric("RAIN")?.let {
                heightmaps.remove("RAIN")
                heightmaps.setGeneric("MOTION_BLOCKING", it)
                heightmaps.setGeneric("MOTION_BLOCKING_NO_LEAVES", it)
            }
            null
        }
    }
}

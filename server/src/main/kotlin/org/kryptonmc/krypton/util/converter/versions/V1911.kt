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

object V1911 {

    private const val VERSION = MCVersions.V18W46A + 1
    private val CHUNK_STATUS_REMAP = mapOf(
        "structure_references" to "empty",
        "biomes" to "empty",
        "base" to "surface",
        "carved" to "carvers",
        "liquid_carved" to "liquid_carvers",
        "decorated" to "features",
        "lighted" to "light",
        "mobs_spawned" to "spawn",
        "finalized" to "heightmaps",
        "fullchunk" to "full"
    )

    fun register() = MCTypeRegistry.CHUNK.addStructureConverter(VERSION) { data, _, _ ->
        val level = data.getMap<String>("Level") ?: return@addStructureConverter null
        val status = level.getString("Status", "empty")!!
        level.setString("Status", CHUNK_STATUS_REMAP.getOrDefault(status, "empty"))
        null
    }
}

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
package org.kryptonmc.krypton.util.converters

import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object RenameStatsConverter {

    fun register(version: Int, renamer: (String) -> String?) = register(version, 0, renamer)

    fun register(version: Int, subVersion: Int, renamer: (String) -> String?) {
        MCTypeRegistry.OBJECTIVE.addStructureConverter(version, subVersion) { data, _, _ ->
            val criteriaType = data.getMap<String>("CriteriaType") ?: return@addStructureConverter null
            val type = criteriaType.getString("type")
            if (type != "minecraft:custom") return@addStructureConverter null
            val id = criteriaType.getString("id") ?: return@addStructureConverter null

            val rename = renamer(id)
            if (rename != null) criteriaType.setString("id", rename)
            null
        }
        MCTypeRegistry.STATS.addStructureConverter(version, subVersion) { data, _, _ ->
            val stats = data.getMap<String>("stats") ?: return@addStructureConverter null
            val custom = stats.getMap<String>("minecraft:custom") ?: return@addStructureConverter null

            custom.keys().toList().forEach {
                val rename = renamer(it) ?: return@forEach
                val value = custom.getGeneric(it)!!
                custom.remove(it)
                custom.setGeneric(rename, value)
            }
            null
        }
    }
}

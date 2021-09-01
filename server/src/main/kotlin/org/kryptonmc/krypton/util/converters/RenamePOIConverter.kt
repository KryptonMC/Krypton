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

import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object RenamePOIConverter {

    fun register(version: Int, renamer: (String) -> String?) = register(version, 0, renamer)

    fun register(version: Int, subVersion: Int, renamer: (String) -> String?) {
        MCTypeRegistry.POI_CHUNK.addStructureConverter(version, subVersion) { data, _, _ ->
            val sections = data.getMap<String>("Sections") ?: return@addStructureConverter null
            sections.keys().forEach {
                val section = sections.getMap<String>(it)!!
                val records = section.getList("Records", ObjectType.MAP) ?: return@forEach

                for (i in 0 until records.size()) {
                    val record = records.getMap<String>(i)
                    val type = record.getString("type")
                    if (type != null) {
                        val converted = renamer(type)
                        if (converted != null) record.setString("type", converted)
                    }
                }
            }
            null
        }
    }
}

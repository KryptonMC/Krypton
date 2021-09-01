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
import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V113 {

    private const val VERSION = MCVersions.V15W33C + 1

    // Removes "HandDropChances" and "ArmorDropChances" if they're empty.
    fun register() = MCTypeRegistry.ENTITY.addStructureConverter(VERSION) { data, _, _ ->
        data.checkList("HandDropChances", 2)
        data.checkList("ArmorDropChances", 4)
        null
    }

    private fun MapType<String>.checkList(id: String, requiredLength: Int) {
        val list = getList(id, ObjectType.FLOAT)
        if (list != null && list.size() == requiredLength) {
            for (i in 0 until requiredLength) {
                if (list.getFloat(i) != 0F) return
            }
        }
        remove(id)
    }
}

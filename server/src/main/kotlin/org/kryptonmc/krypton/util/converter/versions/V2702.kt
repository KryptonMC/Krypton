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
import org.kryptonmc.krypton.util.converter.StringDataConverter
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V2702 {

    private const val VERSION = MCVersions.V21W10A + 3

    fun register() {
        val arrowConverter = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                if (data.hasKey("pickup")) return null
                val player = data.getBoolean("player", true)
                data.remove("player")
                data.setByte("pickup", if (player) 1 else 0)
                return null
            }
        }

        MCTypeRegistry.ENTITY.addConverterForId("minecraft:arrow", arrowConverter)
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:spectral_arrow", arrowConverter)
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:trident", arrowConverter)
    }
}

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

object V111 {

    private const val VERSION = MCVersions.V15W33B

    fun register() {
        MCTypeRegistry.ENTITY.addConverterForId("Painting", EntityRotationConverter)
        MCTypeRegistry.ENTITY.addConverterForId("ItemFrame", EntityRotationConverter)
    }

    private object EntityRotationConverter : StringDataConverter(VERSION) {

        private val DIRECTIONS = arrayOf(
            intArrayOf(0, 0, 1),
            intArrayOf(-1, 0, 0),
            intArrayOf(0, 0, -1),
            intArrayOf(1, 0, 0)
        )

        override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
            if (data.getNumber("Facing") != null) return null

            val direction = data.getNumber("Direction")
            val facing: Int
            if (direction != null) {
                data.remove("Direction")
                facing = direction.toInt() % DIRECTIONS.size
                val offsets = DIRECTIONS[facing]
                data.setInt("TileX", data.getInt("TileX") + offsets[0])
                data.setInt("TileY", data.getInt("TileY") + offsets[1])
                data.setInt("TileZ", data.getInt("TileZ") + offsets[2])
                if (data.getString("id") == "ItemFrame") {
                    val rotation = data.getNumber("ItemRotation")
                    if (rotation != null) data.setByte("ItemRotation", (rotation.toByte() * 2).toByte())
                }
            } else {
                facing = data.getByte("Dir") % DIRECTIONS.size
                data.remove("Dir")
            }

            data.setByte("Facing", facing.toByte())
            return null
        }
    }
}

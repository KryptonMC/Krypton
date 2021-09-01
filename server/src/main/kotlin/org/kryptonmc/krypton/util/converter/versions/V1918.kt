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
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil

object V1918 {

    private const val VERSION = MCVersions.V18W49A + 2

    fun register() {
        val converter = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                val profession = data.getInt("Profession")
                val career = data.getInt("Career")
                val careerLevel = data.getInt("CareerLevel", 1)
                data.remove("Profession")
                data.remove("Career")
                data.remove("CareerLevel")

                val villagerData = NBTTypeUtil.createEmptyMap<String>()
                data.setMap("VillagerData", villagerData)
                villagerData.setString("type", "minecraft:plains")
                villagerData.setString("profession", getProfessionString(profession, career))
                villagerData.setInt("level", careerLevel)
                return null
            }
        }

        MCTypeRegistry.ENTITY.addConverterForId("minecraft:villager", converter)
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:zombie_villager", converter)
    }

    private fun getProfessionString(professionId: Int, careerId: Int): String = when (professionId) {
        0 -> when (careerId) {
            2 -> "minecraft:fisherman"
            3 -> "minecraft:shepherd"
            4 -> "minecraft:fletcher"
            else -> "minecraft:farmer"
        }
        1 -> if (careerId == 2) "minecraft:cartographer" else "minecraft:librarian"
        2 -> "minecraft:cleric"
        3 -> when (careerId) {
            2 -> "minecraft:weaponsmith"
            3 -> "minecraft:toolsmith"
            else -> "minecraft:armorer"
        }
        4 -> if (careerId == 2) "minecraft:leatherworker" else "minecraft:butcher"
        5 -> "minecraft:nitwit"
        else -> "minecraft:none"
    }
}

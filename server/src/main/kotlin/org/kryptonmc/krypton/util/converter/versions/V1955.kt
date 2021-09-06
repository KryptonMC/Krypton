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
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil

object V1955 {

    private const val VERSION = MCVersions.V1_14_1_PRE1
    private val LEVEL_XP_THRESHOLDS = intArrayOf(0, 10, 50, 100, 150)

    fun register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:villager", VERSION) { data, _, _ ->
            val villagerData = data.getMap<String>("VillagerData")
            var level = villagerData?.getInt("level") ?: 0
            if (level == 0 || level == 1) {
                val offers = data.getMap<String>("Offers")
                val recipes = offers?.getList("Recipes", ObjectType.MAP)
                val recipeCount = recipes?.size() ?: 0
                level = (recipeCount / 2).clamp(1, 5)
                if (level > 1) data.addLevel(level)
            }
            if (!data.hasKey("Xp", ObjectType.NUMBER)) data.addXpFromLevel(level)
            null
        }

        MCTypeRegistry.ENTITY.addConverterForId("minecraft:zombie_villager", VERSION) { data, _, _ ->
            val xp = data.getNumber("Xp")
            if (xp == null) {
                val villagerData = data.getMap<String>("VillagerData")
                val level = villagerData?.getInt("level", 1) ?: 1
                data.setInt("Xp", getMinXpPerLevel(level))
            }
            null
        }
    }

    private fun getMinXpPerLevel(level: Int) = LEVEL_XP_THRESHOLDS[(level - 1).clamp(
        0,
        LEVEL_XP_THRESHOLDS.size - 1
    )]

    private fun MapType<String>.addXpFromLevel(level: Int) = setInt("Xp", getMinXpPerLevel(level))

    private fun MapType<String>.addLevel(level: Int) {
        val villagerData = getMap("VillagerData")
            ?: NBTTypeUtil.createEmptyMap<String>().apply { setMap("VillagerData", this) }
        villagerData.setInt("level", level)
    }
}

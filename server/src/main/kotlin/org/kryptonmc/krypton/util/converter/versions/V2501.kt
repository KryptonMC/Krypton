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
import org.kryptonmc.krypton.util.converter.walkers.convertKeys
import org.kryptonmc.krypton.util.converter.walkers.convertList

object V2501 {

    private const val VERSION = MCVersions.V1_15_2 + 271

    fun register() {
        val converter = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                val recipesUsedSize = data.getInt("RecipesUsedSize")
                data.remove("RecipesUsedSize")
                if (recipesUsedSize <= 0) return null

                val newRecipes = NBTTypeUtil.createEmptyMap<String>()
                data.setMap("RecipesUsed", newRecipes)

                for (i in 0 until recipesUsedSize) {
                    val recipeKey = data.getString("RecipeLocation$i")
                    data.remove("RecipeLocation$i")
                    val recipeAmount = data.getInt("RecipeAmount$i")
                    data.remove("RecipeAmount$i")
                    if (i <= 0 || recipeKey == null) continue
                    newRecipes.setInt(recipeKey, recipeAmount)
                }
                return null
            }
        }

        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:furnace", converter)
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:blast_furnace", converter)
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:smoker", converter)

        registerFurnace("minecraft:furnace")
        registerFurnace("minecraft:smoker")
        registerFurnace("minecraft:blast_furnace")
    }

    private fun registerFurnace(id: String) = MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, id) { data, fromVersion, toVersion ->
        data.convertList(MCTypeRegistry.ITEM_STACK, "Items", fromVersion, toVersion)
        data.convertKeys(MCTypeRegistry.RECIPE, "RecipesUsed", fromVersion, toVersion)
        null
    }
}

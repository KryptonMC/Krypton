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

import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.walkers.ItemListsDataWalker
import org.kryptonmc.krypton.util.converter.walkers.convert
import org.kryptonmc.krypton.util.converters.RenameAdvancementsConverter
import org.kryptonmc.krypton.util.converters.RenameRecipesConverter

object V2100 {

    private const val VERSION = MCVersions.V1_14_4 + 124
    private val RECIPE_RENAMES = mapOf("minecraft:sugar" to "sugar_from_sugar_cane")

    fun register() {
        RenameRecipesConverter.register(VERSION, RECIPE_RENAMES::get)
        RenameAdvancementsConverter.register(VERSION, mapOf("minecraft:recipes/misc/sugar" to "minecraft:recipes/misc/sugar_from_sugar_cane")::get)

        registerMob("minecraft:bee")
        registerMob("minecraft:bee_stinger")
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "minecraft:beehive") { data, fromVersion, toVersion ->
            data.getList("Bees", ObjectType.MAP)?.let {
                for (i in 0 until it.size()) {
                    it.getMap<String>(i).convert(MCTypeRegistry.ENTITY, "EntityData", fromVersion, toVersion)
                }
            }
            null
        }
    }

    private fun registerMob(id: String) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, ItemListsDataWalker("ArmorItems", "HandItems"))
    }
}

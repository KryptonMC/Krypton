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
import org.kryptonmc.krypton.util.converter.walkers.convert
import org.kryptonmc.krypton.util.converter.walkers.convertList

object V1929 {

    private const val VERSION = MCVersions.V19W04B + 2

    fun register() {
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:wandering_trader") { data, fromVersion, toVersion ->
            data.convertList(MCTypeRegistry.ITEM_STACK, "Inventory", fromVersion, toVersion)
            data.getMap<String>("Offers")?.let { offers ->
                offers.getList("Recipes", ObjectType.MAP)?.let {
                    for (i in 0 until it.size()) {
                        val recipe = it.getMap<String>(i)
                        recipe.convert(MCTypeRegistry.ITEM_STACK, "buy", fromVersion, toVersion)
                        recipe.convert(MCTypeRegistry.ITEM_STACK, "buyB", fromVersion, toVersion)
                        recipe.convert(MCTypeRegistry.ITEM_STACK, "sell", fromVersion, toVersion)
                    }
                }
            }

            data.convertList(MCTypeRegistry.ITEM_STACK, "ArmorItems", fromVersion, toVersion)
            data.convertList(MCTypeRegistry.ITEM_STACK, "HandItems", fromVersion, toVersion)
            null
        }
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:trader_llama") { data, fromVersion, toVersion ->
            data.convert(MCTypeRegistry.ITEM_STACK, "SaddleItem", fromVersion, toVersion)
            data.convert(MCTypeRegistry.ITEM_STACK, "DecorItem", fromVersion, toVersion)

            data.convertList(MCTypeRegistry.ITEM_STACK, "Items", fromVersion, toVersion)
            data.convertList(MCTypeRegistry.ITEM_STACK, "ArmorItems", fromVersion, toVersion)
            data.convertList(MCTypeRegistry.ITEM_STACK, "HandItems", fromVersion, toVersion)
            null
        }
    }
}

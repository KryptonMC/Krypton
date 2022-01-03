/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.item

import org.kryptonmc.krypton.item.handler.ItemHandler
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.item.handler.DebugStickHandler
import org.kryptonmc.krypton.item.handler.FoodHandler
import org.kryptonmc.krypton.item.handler.SwordHandler
import org.kryptonmc.krypton.item.handler.TridentHandler

object ItemManager {

    private val handlers = mutableMapOf<String, ItemHandler>()

    init {
        register(ItemTypes.WOODEN_SWORD, SwordHandler)
        register(ItemTypes.STONE_SWORD, SwordHandler)
        register(ItemTypes.GOLDEN_SWORD, SwordHandler)
        register(ItemTypes.IRON_SWORD, SwordHandler)
        register(ItemTypes.DIAMOND_SWORD, SwordHandler)
        register(ItemTypes.NETHERITE_SWORD, SwordHandler)
        register(ItemTypes.TRIDENT, TridentHandler)
        register(ItemTypes.DEBUG_STICK, DebugStickHandler)
        register(ItemTypes.COOKED_BEEF, FoodHandler)
    }

    @JvmStatic
    fun handler(type: ItemType): ItemHandler? = handlers[type.key().asString()]

    @JvmStatic
    fun register(type: ItemType, handler: ItemHandler) {
        handlers[type.key().asString()] = handler
    }
}

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

    private val handlers = HashMap<String, ItemHandler>()

    @JvmStatic
    fun bootstrap() {
        register(ItemTypes.WOODEN_SWORD.get(), SwordHandler)
        register(ItemTypes.STONE_SWORD.get(), SwordHandler)
        register(ItemTypes.GOLDEN_SWORD.get(), SwordHandler)
        register(ItemTypes.IRON_SWORD.get(), SwordHandler)
        register(ItemTypes.DIAMOND_SWORD.get(), SwordHandler)
        register(ItemTypes.NETHERITE_SWORD.get(), SwordHandler)
        register(ItemTypes.TRIDENT.get(), TridentHandler)
        register(ItemTypes.DEBUG_STICK.get(), DebugStickHandler)
        register(ItemTypes.COOKED_BEEF.get(), FoodHandler)
    }

    @JvmStatic
    fun handler(type: ItemType): ItemHandler? = handlers.get(type.key().asString())

    @JvmStatic
    fun register(type: ItemType, handler: ItemHandler) {
        handlers.put(type.key().asString(), handler)
    }
}

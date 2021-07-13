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
package org.kryptonmc.krypton.item

import org.kryptonmc.api.item.ItemHandler
import org.kryptonmc.api.item.ItemManager
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.item.handler.DiamondSwordHandler
import org.kryptonmc.krypton.item.handler.GoldenSwordHandler
import org.kryptonmc.krypton.item.handler.IronSwordHandler
import org.kryptonmc.krypton.item.handler.NetheriteSwordHandler
import org.kryptonmc.krypton.item.handler.StoneSwordHandler
import org.kryptonmc.krypton.item.handler.TridentHandler
import org.kryptonmc.krypton.item.handler.WoodenSwordHandler

object KryptonItemManager : ItemManager {

    override val handlers = mutableMapOf<String, ItemHandler>()

    init {
        register(ItemTypes.WOODEN_SWORD.key.asString(), WoodenSwordHandler)
        register(ItemTypes.STONE_SWORD.key.asString(), StoneSwordHandler)
        register(ItemTypes.GOLDEN_SWORD.key.asString(), GoldenSwordHandler)
        register(ItemTypes.IRON_SWORD.key.asString(), IronSwordHandler)
        register(ItemTypes.DIAMOND_SWORD.key.asString(), DiamondSwordHandler)
        register(ItemTypes.NETHERITE_SWORD.key.asString(), NetheriteSwordHandler)
        register(ItemTypes.TRIDENT.key.asString(), TridentHandler)
    }

    override fun handler(key: String) = handlers[key]

    override fun register(key: String, handler: ItemHandler) {
        handlers[key] = handler
    }
}

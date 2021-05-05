/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.inventory

import org.kryptonmc.krypton.api.inventory.Inventory
import org.kryptonmc.krypton.api.inventory.InventoryHolder
import org.kryptonmc.krypton.api.inventory.InventoryType
import org.kryptonmc.krypton.api.inventory.item.ItemStack

abstract class KryptonInventory(
    override val type: InventoryType,
    override val owner: InventoryHolder,
    final override val size: Int
) : Inventory {

    override val items = arrayOfNulls<ItemStack>(size)

    override fun get(index: Int) = items[index]

    override fun set(index: Int, item: ItemStack) {
        items[index] = item
    }

    override fun contains(item: ItemStack) = item in items

    override fun plusAssign(item: ItemStack) = items.forEachIndexed { index, element ->
        if (element == null) {
            items[index] = item
            return
        }
    }

    override fun minusAssign(item: ItemStack) = items.forEachIndexed { index, element ->
        if (element == item) {
            items[index] = null
            return
        }
    }

    override fun clear() = items.forEachIndexed { index, _ -> items[index] = null }

    override fun iterator() = items.iterator()
}

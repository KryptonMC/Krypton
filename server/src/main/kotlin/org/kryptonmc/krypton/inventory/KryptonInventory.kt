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
package org.kryptonmc.krypton.inventory

import org.kryptonmc.api.inventory.Inventory
import org.kryptonmc.api.inventory.InventoryHolder
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.krypton.item.EmptyItemStack
import org.kryptonmc.krypton.item.KryptonItemStack

abstract class KryptonInventory(
    val id: Int,
    override val type: InventoryType,
    override val owner: InventoryHolder,
    final override val size: Int,
    totalItems: Int = size
) : Inventory {

    abstract val networkItems: List<KryptonItemStack>

    override val items = Array<KryptonItemStack>(totalItems) { EmptyItemStack }

    override fun get(index: Int) = items[index]

    override fun set(index: Int, item: ItemStack) {
        if (item !is KryptonItemStack) return
        items[index] = item
    }

    override fun contains(item: ItemStack) = item in items

    override fun plusAssign(item: ItemStack) {
        if (item !is KryptonItemStack) return
        items.forEachIndexed { index, element ->
            if (element !== EmptyItemStack) return@forEachIndexed
            items[index] = item
            return
        }
    }

    override fun minusAssign(item: ItemStack) {
        items.forEachIndexed { index, element ->
            if (element != item) return@forEachIndexed
            items[index] = EmptyItemStack
            return
        }
    }

    override fun clear() = items.forEachIndexed { index, _ -> items[index] = EmptyItemStack }

    override fun iterator() = items.iterator()
}

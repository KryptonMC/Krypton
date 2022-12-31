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
package org.kryptonmc.krypton.inventory

import org.kryptonmc.api.inventory.Inventory
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.collection.FixedList

abstract class KryptonInventory(
    val id: Int,
    final override val type: InventoryType,
    final override val size: Int,
    totalItems: Int = size
) : Inventory, Writable {

    private var stateId = 0
    final override val items = FixedList(totalItems, KryptonItemStack.EMPTY)

    fun stateId(): Int = stateId

    fun incrementStateId(): Int {
        stateId = stateId + 1 and Short.MAX_VALUE.toInt()
        return stateId
    }

    final override fun getItem(index: Int): KryptonItemStack = items.get(index)

    final override fun hasItem(item: ItemStack): Boolean = items.contains(item)

    final override fun addItem(item: ItemStack) {
        if (item !is KryptonItemStack) return
        items.forEachIndexed { index, element ->
            if (element.type == item.type) {
                val initialAmount = element.amount
                val maxAmount = element.type.maximumStackSize
                if (initialAmount + item.amount <= maxAmount) {
                    items.set(index, element.withAmount(initialAmount + item.amount))
                    return
                } else if (element.amount != maxAmount) {
                    items.set(index, element.withAmount(maxAmount))
                    if (item.amount == 0) return
                }
            } else if (element.isEmpty()) {
                items.set(index, item)
                return
            }
        }
    }

    final override fun removeItem(item: ItemStack) {
        items.forEachIndexed { index, element ->
            if (element != item) return@forEachIndexed
            items.set(index, KryptonItemStack.EMPTY)
            return
        }
    }

    final override fun clear() {
        items.forEachIndexed { index, _ -> items.set(index, KryptonItemStack.EMPTY) }
    }

    final override fun iterator(): Iterator<KryptonItemStack> = items.iterator()
}

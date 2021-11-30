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

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.inventory.Inventory
import org.kryptonmc.api.inventory.InventoryHolder
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.krypton.item.EmptyItemStack
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.FixedList

abstract class KryptonInventory(
    val id: Int,
    override val type: InventoryType,
    override val owner: InventoryHolder,
    final override val size: Int,
    totalItems: Int = size
) : Inventory {

    abstract val networkWriter: (ByteBuf) -> Unit

    var stateId = 0
        private set
    override val items = FixedList<KryptonItemStack>(totalItems, EmptyItemStack)

    fun incrementStateId(): Int {
        stateId = stateId + 1 and Short.MAX_VALUE.toInt()
        return stateId
    }

    override fun get(index: Int): KryptonItemStack = items[index]

    override fun contains(item: ItemStack): Boolean = items.contains(item)

    override fun add(item: ItemStack) {
        if (item !is KryptonItemStack) return
        items.forEachIndexed { index, element ->
            if (element.type == item.type) {
                val initialAmount = element.amount
                val maxAmount = element.type.maximumStackSize
                if (initialAmount + item.amount <= maxAmount) {
                    element.amount = initialAmount + item.amount
                    return
                } else if (element.amount != maxAmount) {
                    element.amount = maxAmount
                    item.amount = maxAmount - item.amount
                    if (item.amount == 0) return
                }
            } else if (element == EmptyItemStack) {
                items[index] = item
                return
            }
        }
    }

    override fun remove(item: ItemStack) {
        items.forEachIndexed { index, element ->
            if (element != item) return@forEachIndexed
            items[index] = EmptyItemStack
            return
        }
    }

    override fun clear() {
        items.forEachIndexed { index, _ -> items[index] = EmptyItemStack }
    }

    override fun iterator(): Iterator<KryptonItemStack> = items.iterator()
}

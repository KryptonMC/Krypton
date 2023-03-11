/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

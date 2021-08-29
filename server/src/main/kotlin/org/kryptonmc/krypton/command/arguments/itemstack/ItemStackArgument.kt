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
package org.kryptonmc.krypton.command.arguments.itemstack

import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.meta.KryptonMetaHolder
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.MutableCompoundTag

data class ItemStackArgument(val item: ItemType, val tag: CompoundTag? = null) {

    fun createItemStacks(amount: Int): List<KryptonItemStack> {
        val items = mutableListOf<KryptonItemStack>()
        if (amount <= item.maximumAmount) {
            items.add(createStack(amount))
        } else {
            var size = amount
            while (size > item.maximumAmount) {
                items.add(createStack(item.maximumAmount))
                size -= item.maximumAmount
            }
            items.add(createStack(size))
        }
        return items
    }

    private fun createStack(amount: Int) = KryptonItemStack(item, amount, KryptonMetaHolder(tag?.mutable() ?: MutableCompoundTag()))
}

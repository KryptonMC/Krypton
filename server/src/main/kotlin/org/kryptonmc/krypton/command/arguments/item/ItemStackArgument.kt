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
package org.kryptonmc.krypton.command.arguments.item

import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.item.ItemFactory
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.nbt.CompoundTag

/**
 * An argument that represents an item, optionally with some NBT data.
 */
@JvmRecord
data class ItemStackArgument(val type: ItemType, val data: CompoundTag? = null) {

    /**
     * Creates the item stacks from the data stored by this argument.
     */
    fun createItemStacks(amount: Int): List<KryptonItemStack> {
        if (amount <= type.maximumStackSize) return listOf(createStack(amount))
        val items = persistentListOf<KryptonItemStack>().builder()
        var size = amount
        while (size > type.maximumStackSize) {
            items.add(createStack(type.maximumStackSize))
            size -= type.maximumStackSize
        }
        items.add(createStack(size))
        return items.build()
    }

    private fun createStack(amount: Int): KryptonItemStack = KryptonItemStack(type, amount, ItemFactory.create(type, data ?: CompoundTag.EMPTY))
}

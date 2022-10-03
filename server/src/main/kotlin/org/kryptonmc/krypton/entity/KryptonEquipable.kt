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
package org.kryptonmc.krypton.entity

import org.kryptonmc.api.entity.ArmorSlot
import org.kryptonmc.api.entity.Equipable
import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.list

interface KryptonEquipable : Equipable {

    fun setHeldItem(hand: Hand, item: KryptonItemStack)

    fun setArmor(slot: ArmorSlot, item: KryptonItemStack)

    fun setEquipment(slot: EquipmentSlot, item: KryptonItemStack)

    override fun setHeldItem(hand: Hand, item: ItemStack) {
        if (item !is KryptonItemStack) return
        setHeldItem(hand, item)
    }

    override fun setArmor(slot: ArmorSlot, item: ItemStack) {
        if (item !is KryptonItemStack) return
        setArmor(slot, item)
    }

    override fun setEquipment(slot: EquipmentSlot, item: ItemStack) {
        if (item !is KryptonItemStack) return
        setEquipment(slot, item)
    }

    companion object {

        @JvmStatic
        fun loadItems(tag: CompoundTag, name: String, output: MutableList<KryptonItemStack>) {
            if (!tag.contains(name, ListTag.ID)) return
            val items = tag.getList(name, ListTag.ID)
            for (i in 0 until items.size) {
                output.set(i, KryptonItemStack.from(items.getCompound(i)))
            }
        }

        @JvmStatic
        fun saveItems(items: Iterable<KryptonItemStack>): ListTag = list {
            items.forEach {
                if (it.isEmpty()) return@forEach
                add(it.save())
            }
        }
    }
}

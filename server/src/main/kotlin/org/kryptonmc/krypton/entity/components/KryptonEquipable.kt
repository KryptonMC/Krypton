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
package org.kryptonmc.krypton.entity.components

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
            for (i in 0 until items.size()) {
                output.set(i, KryptonItemStack.from(items.getCompound(i)))
            }
        }

        @JvmStatic
        fun saveItems(items: Iterable<KryptonItemStack>): ListTag = list { items.forEach { if (!it.isEmpty()) add(it.save()) } }
    }
}

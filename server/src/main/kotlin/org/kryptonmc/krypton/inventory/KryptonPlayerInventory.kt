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

import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import org.kryptonmc.api.entity.ArmorSlot
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.inventory.PlayerInventory
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.EmptyItemStack
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.packet.out.play.PacketOutHeldItemChange
import org.kryptonmc.krypton.util.nbt.Serializable

class KryptonPlayerInventory(override val owner: KryptonPlayer) : KryptonInventory(0, TYPE, owner, SIZE, 36), PlayerInventory, Serializable<NBTList<NBTCompound>> {

    override val crafting = Array<KryptonItemStack>(5) { EmptyItemStack }
    override val armor = Array<KryptonItemStack>(4) { EmptyItemStack }

    override val helmet: ItemStack
        get() = armor[ArmorSlot.HELMET.ordinal]
    override val chestplate: ItemStack
        get() = armor[ArmorSlot.CHESTPLATE.ordinal]
    override val leggings: ItemStack
        get() = armor[ArmorSlot.LEGGINGS.ordinal]
    override val boots: ItemStack
        get() = armor[ArmorSlot.BOOTS.ordinal]

    override var heldSlot = 0

    override val mainHand: KryptonItemStack
        get() = items[heldSlot]
    override var offHand: KryptonItemStack = EmptyItemStack

    override fun armor(slot: ArmorSlot) = armor[slot.ordinal]

    override fun set(index: Int, item: ItemStack) {
        if (item !is KryptonItemStack) return
        when (index) {
            0 -> crafting[4] = item
            in 1..4 -> crafting[index - 1] = item
            in 5..8 -> armor[index - 5] = item
            in 9..35 -> items[index] = item
            in 36..44 -> items[index - 36] = item
            45 -> offHand = item
        }
        if (index == heldSlot) return
        owner.session.sendPacket(PacketOutHeldItemChange(index))
    }

    override fun load(tag: NBTList<NBTCompound>) {
        clear()
        tag.forEach {
            val slot = it.getByte("Slot").toInt() and 255
            val stack = KryptonItemStack(it)
            if (stack.type === ItemTypes.AIR) return@forEach
            when (slot) {
                in items.indices -> items[slot] = stack
                in 100..103 -> armor[slot - 100] = stack
                -106 -> offHand = stack
            }
        }
    }

    override fun save(): NBTList<NBTCompound> {
        val list = NBTList<NBTCompound>(NBTTypes.TAG_Compound)
        items.forEachIndexed { index, item ->
            if (item.type === ItemTypes.AIR) return@forEachIndexed
            list.add(NBTCompound().setByte("Slot", index.toByte()).apply { item.save(this) })
        }
        armor.forEachIndexed { index, item ->
            if (item.type === ItemTypes.AIR) return@forEachIndexed
            list.add(NBTCompound().setByte("Slot", (index + 100).toByte()).apply { item.save(this) })
        }
        list.add(NBTCompound().setByte("Slot", -106).apply { offHand.save(this) })
        return list
    }

    override val networkItems: List<KryptonItemStack>
        get() {
            val list = mutableListOf<KryptonItemStack>()
            crafting.forEach { list.add(it) }
            armor.forEach { list.add(it) }
            items.forEach { list.add(it) }
            list.add(offHand)
            return list
        }

    companion object {

        private val TYPE = InventoryType.PLAYER
        private const val SIZE = 46
    }
}

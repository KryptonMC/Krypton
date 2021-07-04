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
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.inventory.PlayerInventory
import org.kryptonmc.api.inventory.item.ItemStack
import org.kryptonmc.api.inventory.item.Material
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.inventory.item.ItemFactory
import org.kryptonmc.krypton.inventory.item.save
import org.kryptonmc.krypton.packet.out.play.PacketOutHeldItemChange
import org.kryptonmc.krypton.util.nbt.Serializable
import org.kryptonmc.krypton.util.nbt.getByte

class KryptonPlayerInventory(override val owner: KryptonPlayer) : KryptonInventory(0, TYPE, owner, SIZE, 36), PlayerInventory, Serializable<NBTList<NBTCompound>> {

    override val armor = Array(4) { ItemStack.EMPTY }

    override val helmet: ItemStack
        get() = armor[0]
    override val chestplate: ItemStack
        get() = armor[1]
    override val leggings: ItemStack
        get() = armor[2]
    override val boots: ItemStack
        get() = armor[3]

    override var heldSlot = 0

    override val mainHand: ItemStack
        get() = items[heldSlot]
    override var offHand = ItemStack.EMPTY

    override fun set(index: Int, item: ItemStack) {
        super.set(index, item)

        if (index == heldSlot) return
        owner.session.sendPacket(PacketOutHeldItemChange(index))
    }

    override fun load(tag: NBTList<NBTCompound>) {
        clear()
        tag.forEach {
            val slot = it.getByte("Slot", 0).toInt() and 255
            val stack = ItemFactory.create(it)
            if (stack.type == Material.AIR) return@forEach
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
            if (item == ItemStack.EMPTY) return@forEachIndexed
            list.add(NBTCompound().setByte("Slot", index.toByte()).apply { item.save(this) })
        }
        armor.forEachIndexed { index, item ->
            if (item == ItemStack.EMPTY) return@forEachIndexed
            list.add(NBTCompound().setByte("Slot", (index + 100).toByte()).apply { item.save(this) })
        }
        list.add(NBTCompound().setByte("Slot", -106).apply { offHand.save(this) })
        return list
    }

    companion object {

        private val TYPE = InventoryType.PLAYER
        private const val SIZE = 46
    }
}

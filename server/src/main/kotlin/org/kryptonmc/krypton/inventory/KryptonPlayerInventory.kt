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
import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.ArmorSlot
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.inventory.PlayerInventory
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeHeldItem
import org.kryptonmc.krypton.packet.out.play.PacketOutSetSlot
import org.kryptonmc.krypton.util.FixedList
import org.kryptonmc.krypton.util.writeItem
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.MutableListTag
import org.kryptonmc.nbt.compound

class KryptonPlayerInventory(override val owner: KryptonPlayer) : KryptonInventory(0, TYPE, owner, SIZE, 36), PlayerInventory {

    override val crafting = FixedList(5, KryptonItemStack.EMPTY)
    override val armor = FixedList(4, KryptonItemStack.EMPTY)

    override val main: List<ItemStack> = items.subList(9, 35)
    override val hotbar: List<ItemStack> = items.subList(0, 8)

    override var helmet: ItemStack
        get() = armor(ArmorSlot.HELMET)
        set(value) = armor(ArmorSlot.HELMET, value)
    override var chestplate: ItemStack
        get() = armor(ArmorSlot.CHESTPLATE)
        set(value) = armor(ArmorSlot.CHESTPLATE, value)
    override var leggings: ItemStack
        get() = armor(ArmorSlot.LEGGINGS)
        set(value) = armor(ArmorSlot.LEGGINGS, value)
    override var boots: ItemStack
        get() = armor(ArmorSlot.BOOTS)
        set(value) = armor(ArmorSlot.BOOTS, value)

    override var heldSlot = 0

    override val mainHand: KryptonItemStack
        get() = items[heldSlot]
    override var offHand: KryptonItemStack = KryptonItemStack.EMPTY

    override fun armor(slot: ArmorSlot) = armor[slot.ordinal]

    override fun armor(slot: ArmorSlot, item: ItemStack) {
        if (item !is KryptonItemStack) return
        armor[slot.ordinal] = item
    }

    override fun heldItem(hand: Hand): KryptonItemStack = when (hand) {
        Hand.MAIN -> mainHand
        Hand.OFF -> offHand
    }

    override fun setHeldItem(hand: Hand, item: ItemStack) {
        if (item !is KryptonItemStack) return
        when (hand) {
            Hand.MAIN -> set(heldSlot + 36, item)
            Hand.OFF -> set(45, item)
        }
    }

    override fun set(index: Int, item: ItemStack) {
        if (item !is KryptonItemStack) return
        set(index, item)
    }

    operator fun set(index: Int, item: KryptonItemStack) {
        when (index) {
            0 -> crafting[4] = item
            in 1..4 -> crafting[index - 1] = item
            in 5..8 -> armor[index - 5] = item
            in 9..35 -> items[index] = item
            in 36..44 -> items[index - 36] = item
            45 -> offHand = item
        }
        owner.session.send(PacketOutSetSlot(id, incrementStateId(), index, item))
        if (index == heldSlot) return
        owner.session.send(PacketOutChangeHeldItem(index))
    }

    fun load(tag: ListTag) {
        clear()
        for (i in tag.indices) {
            val compound = tag.getCompound(i)
            val slot = compound.getByte("Slot").toInt() and 255
            val stack = KryptonItemStack(compound)
            if (stack.type === ItemTypes.AIR) continue
            when (slot) {
                in items.indices -> items[slot] = stack
                100 -> armor[3] = stack
                101 -> armor[2] = stack
                102 -> armor[1] = stack
                103 -> armor[0] = stack
                -106 -> offHand = stack
            }
        }
    }

    fun save(): ListTag {
        val list = MutableListTag(elementType = CompoundTag.ID)
        items.forEachIndexed { index, item ->
            if (item.type === ItemTypes.AIR) return@forEachIndexed
            list.add(compound {
                byte("Slot", index.toByte())
                item.save(this)
            })
        }
        var i: Byte = 103
        armor.forEach {
            if (it.type === ItemTypes.AIR) return@forEach
            list.add(compound {
                byte("Slot", i--)
                it.save(this)
            })
        }
        if (offHand.type !== ItemTypes.AIR) list.add(compound {
            byte("Slot", -106)
            offHand.save(this)
        })
        return list
    }

    override val networkWriter: (ByteBuf) -> Unit = { buf ->
        buf.writeVarInt(SIZE)
        buf.writeItem(crafting[4])
        for (i in 0..3) buf.writeItem(crafting[i])
        armor.forEach { buf.writeItem(it) }
        for (i in 0..26) buf.writeItem(items[i + 9])
        for (i in 0..8) buf.writeItem(items[i])
        buf.writeItem(offHand)
    }

    companion object {

        const val SIZE = 46
        private val TYPE = KryptonInventoryType(Key.key("krypton", "inventory/player"), SIZE)
    }
}

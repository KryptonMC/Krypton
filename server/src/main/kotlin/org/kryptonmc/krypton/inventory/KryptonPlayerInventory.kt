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

import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.entity.ArmorSlot
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.inventory.PlayerInventory
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.packet.out.play.PacketOutSetContainerSlot
import org.kryptonmc.krypton.util.FixedList
import org.kryptonmc.krypton.util.writeItem
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.compound
import org.kryptonmc.nbt.list

class KryptonPlayerInventory(override val owner: KryptonPlayer) : KryptonInventory(0, TYPE, SIZE, INVENTORY_SIZE), PlayerInventory {

    override val crafting: MutableList<KryptonItemStack> = FixedList(CRAFTING_SIZE, KryptonItemStack.EMPTY)
    override val armor: MutableList<KryptonItemStack> = FixedList(ARMOR_SIZE, KryptonItemStack.EMPTY)

    override val main: List<ItemStack> = items.subList(HOTBAR_SIZE, INVENTORY_SIZE - 1)
    override val hotbar: List<ItemStack> = items.subList(0, HOTBAR_SIZE - 1)

    override var helmet: ItemStack
        get() = armor(ArmorSlot.HELMET)
        set(value) = setArmor(ArmorSlot.HELMET, value)
    override var chestplate: ItemStack
        get() = armor(ArmorSlot.CHESTPLATE)
        set(value) = setArmor(ArmorSlot.CHESTPLATE, value)
    override var leggings: ItemStack
        get() = armor(ArmorSlot.LEGGINGS)
        set(value) = setArmor(ArmorSlot.LEGGINGS, value)
    override var boots: ItemStack
        get() = armor(ArmorSlot.BOOTS)
        set(value) = setArmor(ArmorSlot.BOOTS, value)

    override val mainHand: KryptonItemStack
        get() = items[heldSlot]
    override var offHand: KryptonItemStack = KryptonItemStack.EMPTY

    override var heldSlot: Int = 0

    override fun armor(slot: ArmorSlot): KryptonItemStack = armor[slot.ordinal]

    override fun setArmor(slot: ArmorSlot, item: ItemStack) {
        if (item !is KryptonItemStack) return
        armor[slot.ordinal] = item
    }

    override fun heldItem(hand: Hand): KryptonItemStack {
        if (hand == Hand.MAIN) return mainHand
        return offHand
    }

    override fun setHeldItem(hand: Hand, item: ItemStack) {
        if (item !is KryptonItemStack) return
        when (hand) {
            Hand.MAIN -> set(heldSlot + INVENTORY_SIZE, item)
            Hand.OFF -> set(OFFHAND_SLOT, item)
        }
    }

    override fun set(index: Int, item: ItemStack) {
        if (item !is KryptonItemStack) return
        set(index, item)
    }

    operator fun set(index: Int, item: KryptonItemStack) {
        when (index) {
            0 -> crafting[CRAFTING_SLOT] = item
            in 1..CRAFTING_GRID_SIZE -> crafting[index - 1] = item
            in CRAFTING_SIZE until HOTBAR_SIZE -> armor[index - CRAFTING_SIZE] = item
            in HOTBAR_SIZE until INVENTORY_SIZE -> items[index] = item
            in INVENTORY_SIZE until OFFHAND_SLOT -> items[index - INVENTORY_SIZE] = item
            OFFHAND_SLOT -> offHand = item
        }
        owner.session.send(PacketOutSetContainerSlot(id, incrementStateId(), index, item))
    }

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(SIZE)
        buf.writeItem(crafting[CRAFTING_SIZE - 1])
        for (i in 0 until CRAFTING_GRID_SIZE) {
            buf.writeItem(crafting[i])
        }
        armor.forEach { buf.writeItem(it) }
        for (i in 0 until MAIN_SIZE) {
            buf.writeItem(items[i + HOTBAR_SIZE])
        }
        for (i in 0 until HOTBAR_SIZE) {
            buf.writeItem(items[i])
        }
        buf.writeItem(offHand)
    }

    fun getDestroySpeed(state: KryptonBlockState): Float {
        val item = items[heldSlot]
        return item.type.handler().destroySpeed(item, state)
    }

    fun load(data: ListTag) {
        clear()
        data.forEachCompound {
            // No point creating the item stack just to throw it away if it's air
            if (it.getString("id") == ItemTypes.AIR.key().asString()) return@forEachCompound
            val slot = it.getByte("Slot").toInt()
            val stack = KryptonItemStack.from(it)
            when (slot) {
                in items.indices -> items[slot] = stack
                BOOTS_DATA_SLOT -> armor[BOOTS_SLOT] = stack
                LEGGINGS_DATA_SLOT -> armor[LEGGINGS_SLOT] = stack
                CHESTPLATE_DATA_SLOT -> armor[CHESTPLATE_SLOT] = stack
                HELMET_DATA_SLOT -> armor[HELMET_SLOT] = stack
                OFFHAND_DATA_SLOT -> offHand = stack
            }
        }
    }

    fun save(): ListTag = list {
        items.forEachIndexed { index, item ->
            if (item.type === ItemTypes.AIR) return@forEachIndexed
            add(compound {
                byte("Slot", index.toByte())
                item.save(this)
            })
        }
        var i: Byte = HELMET_DATA_SLOT.toByte()
        armor.forEach {
            if (it.type === ItemTypes.AIR) return@forEach
            add(compound {
                byte("Slot", i--)
                it.save(this)
            })
        }
        if (offHand.type !== ItemTypes.AIR) {
            add(compound {
                byte("Slot", OFFHAND_DATA_SLOT.toByte())
                offHand.save(this)
            })
        }
    }

    companion object {

        const val SIZE: Int = 46
        private const val MAIN_SIZE = 27
        private const val HOTBAR_SIZE = 9
        private const val INVENTORY_SIZE = MAIN_SIZE + HOTBAR_SIZE
        private const val CRAFTING_SIZE = 5
        private const val CRAFTING_GRID_SIZE = 4
        private const val CRAFTING_SLOT = 4
        private const val OFFHAND_SLOT = SIZE - 1
        private const val OFFHAND_DATA_SLOT = -106
        private const val ARMOR_SIZE = 4
        private const val HELMET_SLOT = 0
        private const val HELMET_DATA_SLOT = 103
        private const val CHESTPLATE_SLOT = 1
        private const val CHESTPLATE_DATA_SLOT = 102
        private const val LEGGINGS_SLOT = 2
        private const val LEGGINGS_DATA_SLOT = 101
        private const val BOOTS_SLOT = 3
        private const val BOOTS_DATA_SLOT = 100

        private val TYPE = KryptonInventoryType(Key.key("krypton", "inventory/player"), SIZE, Component.translatable("container.inventory"))
    }
}

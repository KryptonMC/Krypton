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
import org.kryptonmc.krypton.util.collection.FixedList
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
        get() = getArmor(ArmorSlot.HELMET)
        set(value) = setArmor(ArmorSlot.HELMET, value)
    override var chestplate: ItemStack
        get() = getArmor(ArmorSlot.CHESTPLATE)
        set(value) = setArmor(ArmorSlot.CHESTPLATE, value)
    override var leggings: ItemStack
        get() = getArmor(ArmorSlot.LEGGINGS)
        set(value) = setArmor(ArmorSlot.LEGGINGS, value)
    override var boots: ItemStack
        get() = getArmor(ArmorSlot.BOOTS)
        set(value) = setArmor(ArmorSlot.BOOTS, value)

    override val mainHand: KryptonItemStack
        get() = items.get(heldSlot)
    override var offHand: KryptonItemStack = KryptonItemStack.EMPTY

    override var heldSlot: Int = 0

    override fun getArmor(slot: ArmorSlot): KryptonItemStack = armor.get(slot.ordinal)

    override fun setArmor(slot: ArmorSlot, item: ItemStack) {
        if (item !is KryptonItemStack) return
        armor.set(slot.ordinal, item)
    }

    override fun getHeldItem(hand: Hand): KryptonItemStack {
        if (hand == Hand.MAIN) return mainHand
        return offHand
    }

    override fun setHeldItem(hand: Hand, item: ItemStack) {
        if (item !is KryptonItemStack) return
        when (hand) {
            Hand.MAIN -> setItem(heldSlot + INVENTORY_SIZE, item)
            Hand.OFF -> setItem(OFFHAND_SLOT, item)
        }
    }

    override fun setItem(index: Int, item: ItemStack) {
        if (item !is KryptonItemStack) return
        setItem(index, item)
    }

    fun setItem(index: Int, item: KryptonItemStack) {
        when (index) {
            0 -> crafting.set(CRAFTING_SLOT, item)
            in 1..CRAFTING_GRID_SIZE -> crafting.set(index - 1, item)
            in CRAFTING_SIZE until HOTBAR_SIZE -> armor.set(index - CRAFTING_SIZE, item)
            in HOTBAR_SIZE until INVENTORY_SIZE -> items.set(index, item)
            in INVENTORY_SIZE until OFFHAND_SLOT -> items.set(index - INVENTORY_SIZE, item)
            OFFHAND_SLOT -> offHand = item
        }
        owner.connection.send(PacketOutSetContainerSlot(id, incrementStateId(), index, item))
    }

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(SIZE)
        buf.writeItem(crafting.get(CRAFTING_SIZE - 1))
        for (i in 0 until CRAFTING_GRID_SIZE) {
            buf.writeItem(crafting.get(i))
        }
        armor.forEach(buf::writeItem)
        for (i in 0 until MAIN_SIZE) {
            buf.writeItem(items.get(i + HOTBAR_SIZE))
        }
        for (i in 0 until HOTBAR_SIZE) {
            buf.writeItem(items.get(i))
        }
        buf.writeItem(offHand)
    }

    fun getDestroySpeed(state: KryptonBlockState): Float {
        val item = items.get(heldSlot)
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
                in items.indices -> items.set(slot, stack)
                BOOTS_DATA_SLOT -> armor.set(BOOTS_SLOT, stack)
                LEGGINGS_DATA_SLOT -> armor.set(LEGGINGS_SLOT, stack)
                CHESTPLATE_DATA_SLOT -> armor.set(CHESTPLATE_SLOT, stack)
                HELMET_DATA_SLOT -> armor.set(HELMET_SLOT, stack)
                OFFHAND_DATA_SLOT -> offHand = stack
            }
        }
    }

    fun save(): ListTag = list {
        items.forEachIndexed { index, item ->
            if (item.type === ItemTypes.AIR) return@forEachIndexed
            add(compound {
                putByte("Slot", index.toByte())
                item.save(this)
            })
        }
        var i: Byte = HELMET_DATA_SLOT.toByte()
        armor.forEach {
            if (it.type === ItemTypes.AIR) return@forEach
            add(compound {
                putByte("Slot", i--)
                it.save(this)
            })
        }
        if (offHand.type !== ItemTypes.AIR) {
            add(compound {
                putByte("Slot", OFFHAND_DATA_SLOT.toByte())
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

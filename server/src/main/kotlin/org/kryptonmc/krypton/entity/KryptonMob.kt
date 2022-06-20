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

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.entity.Mob
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.util.FixedList
import org.kryptonmc.krypton.util.InteractionResult
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.ListTag

abstract class KryptonMob(
    world: KryptonWorld,
    type: EntityType<out Mob>,
    attributeSupplier: AttributeSupplier
) : KryptonLivingEntity(world, type, attributeSupplier), Mob, KryptonEquipable {

    private val handItems = FixedList(2, KryptonItemStack.EMPTY)
    private val armorItems = FixedList(4, KryptonItemStack.EMPTY)
    private val handDropChances = FloatArray(2)
    private val armorDropChances = FloatArray(4)

    override val handSlots: Iterable<KryptonItemStack>
        get() = handItems
    override val armorSlots: Iterable<KryptonItemStack>
        get() = armorItems

    final override var canPickUpLoot: Boolean = false
    final override var isPersistent: Boolean = false
    open var target: KryptonLivingEntity? = null

    final override var hasAI: Boolean
        get() = getFlag(0)
        set(value) = setFlag(0, value)
    final override var mainHand: MainHand
        get() = if (getFlag(1)) MainHand.LEFT else MainHand.RIGHT
        set(value) = setFlag(1, value == MainHand.LEFT)
    final override var isAggressive: Boolean
        get() = getFlag(2)
        set(value) = setFlag(2, value)

    init {
        data.add(MetadataKeys.MOB.FLAGS)
    }

    override fun equipment(slot: EquipmentSlot): KryptonItemStack = when (slot.type) {
        EquipmentSlot.Type.HAND -> handItems[slot.index]
        EquipmentSlot.Type.ARMOR -> armorItems[slot.index]
    }

    override fun setEquipment(slot: EquipmentSlot, item: KryptonItemStack) {
        when (slot.type) {
            EquipmentSlot.Type.HAND -> handItems[slot.index] = item
            EquipmentSlot.Type.ARMOR -> armorItems[slot.index] = item
        }
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        if (tag.contains("CanPickUpLoot", ByteTag.ID)) canPickUpLoot = tag.getBoolean("CanPickUpLoot")
        isPersistent = tag.getBoolean("PersistenceRequired")

        loadItems(tag, "ArmorItems", armorItems)
        loadItems(tag, "HandItems", handItems)
        if (tag.contains("ArmorDropChances", ListTag.ID)) {
            val chances = tag.getList("ArmorDropChances", FloatTag.ID)
            for (i in chances.indices) {
                armorDropChances[i] = chances.getFloat(i)
            }
        }
        if (tag.contains("HandDropChances", ListTag.ID)) {
            val chances = tag.getList("HandDropChances", FloatTag.ID)
            for (i in chances.indices) {
                handDropChances[i] = chances.getFloat(i)
            }
        }

        mainHand = if (tag.getBoolean("LeftHanded")) MainHand.LEFT else MainHand.RIGHT
        hasAI = !tag.getBoolean("NoAI")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        boolean("CanPickUpLoot", canPickUpLoot)
        boolean("PersistenceRequired", isPersistent)
        put("ArmorItems", saveItems(armorItems))
        put("HandItems", saveItems(handItems))
        list("ArmorDropChances") { armorDropChances.forEach(::addFloat) }
        list("HandDropChances") { handDropChances.forEach(::addFloat) }
        boolean("LeftHanded", mainHand == MainHand.LEFT)
        if (!hasAI) boolean("NoAI", true)
    }

    // TODO: Separate mob interactions
    //protected open fun mobInteract(player: KryptonPlayer, hand: Hand): InteractionResult = InteractionResult.PASS

    /*
    final override fun interact(player: KryptonPlayer, hand: Hand): InteractionResult {
        if (!isAlive) return InteractionResult.PASS
        var result = handleImportantInteractions(player, hand)
        if (result.consumesAction) return result
        result = mobInteract(player, hand)
        if (result.consumesAction) return result
        return super.interact(player, hand)
    }

    private fun handleImportantInteractions(player: KryptonPlayer, hand: Hand): InteractionResult {
        val heldItem = player.heldItem(hand)
        // TODO: Handle mob leashing
        if (heldItem.type === ItemTypes.NAME_TAG) {
            val result = heldItem.type.handler().interactEntity(heldItem, player, this, hand)
            if (result.consumesAction) return result
        }
        // TODO: Handle spawn egg
        return InteractionResult.PASS
    }
    */

    private fun getFlag(flag: Int): Boolean = getFlag(MetadataKeys.MOB.FLAGS, flag)

    private fun setFlag(flag: Int, state: Boolean) {
        setFlag(MetadataKeys.MOB.FLAGS, flag, state)
    }

    companion object {

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonLivingEntity.attributes()
            .add(AttributeTypes.FOLLOW_RANGE, 16.0)
            .add(AttributeTypes.ATTACK_KNOCKBACK)
    }
}

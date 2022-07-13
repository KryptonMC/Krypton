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
import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.entity.Mob
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.FixedList
import org.kryptonmc.krypton.world.KryptonWorld

abstract class KryptonMob(
    world: KryptonWorld,
    type: EntityType<out Mob>,
    attributeSupplier: AttributeSupplier
) : KryptonLivingEntity(world, type, attributeSupplier), Mob {

    internal val handItems = FixedList(2, KryptonItemStack.EMPTY)
    internal val armorItems = FixedList(4, KryptonItemStack.EMPTY)
    internal val handDropChances = FloatArray(2)
    internal val armorDropChances = FloatArray(4)

    override val handSlots: Iterable<KryptonItemStack>
        get() = handItems
    override val armorSlots: Iterable<KryptonItemStack>
        get() = armorItems

    var canPickUpLoot: Boolean = false
    final override var isPersistent: Boolean = false
    open var target: KryptonLivingEntity? = null

    final override var hasAI: Boolean
        get() = !getFlag(0)
        set(value) = setFlag(0, !value)
    final override var mainHand: MainHand
        get() = if (getFlag(1)) MainHand.LEFT else MainHand.RIGHT
        set(value) = setFlag(1, value == MainHand.LEFT)
    final override var isAggressive: Boolean
        get() = getFlag(2)
        set(value) = setFlag(2, value)

    init {
        data.add(MetadataKeys.MOB.FLAGS, 0)
    }

    override fun equipment(slot: EquipmentSlot): KryptonItemStack = when (slot.type) {
        EquipmentSlot.Type.HAND -> handItems[EquipmentSlots.index(slot)]
        EquipmentSlot.Type.ARMOR -> armorItems[EquipmentSlots.index(slot)]
    }

    override fun setEquipment(slot: EquipmentSlot, item: KryptonItemStack) {
        when (slot.type) {
            EquipmentSlot.Type.HAND -> handItems[EquipmentSlots.index(slot)] = item
            EquipmentSlot.Type.ARMOR -> armorItems[EquipmentSlots.index(slot)] = item
        }
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

    private fun getFlag(flag: Int): Boolean = data.getFlag(MetadataKeys.MOB.FLAGS, flag)

    private fun setFlag(flag: Int, state: Boolean) {
        data.setFlag(MetadataKeys.MOB.FLAGS, flag, state)
    }

    companion object {

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonLivingEntity.attributes()
            .add(AttributeTypes.FOLLOW_RANGE, 16.0)
            .add(AttributeTypes.ATTACK_KNOCKBACK)
    }
}

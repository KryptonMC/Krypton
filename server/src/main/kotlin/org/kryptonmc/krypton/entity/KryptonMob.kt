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

import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.entity.Mob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.MobSerializer
import org.kryptonmc.krypton.entity.util.EquipmentSlots
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.collection.FixedList
import org.kryptonmc.krypton.world.KryptonWorld

abstract class KryptonMob(world: KryptonWorld) : KryptonLivingEntity(world), Mob {

    override val serializer: EntitySerializer<out KryptonMob>
        get() = MobSerializer

    internal val handItems = FixedList(2, KryptonItemStack.EMPTY)
    internal val armorItems = FixedList(4, KryptonItemStack.EMPTY)
    internal val handDropChances = FloatArray(2) { DEFAULT_DROP_CHANCE }
    internal val armorDropChances = FloatArray(4) { DEFAULT_DROP_CHANCE }

    final override var canPickUpLoot: Boolean = false
    final override var isPersistent: Boolean = false
    private var target: KryptonLivingEntity? = null

    final override var hasAI: Boolean
        get() = !data.getFlag(MetadataKeys.Mob.FLAGS, FLAG_NO_AI)
        set(value) = data.setFlag(MetadataKeys.Mob.FLAGS, FLAG_NO_AI, !value)
    final override var mainHand: MainHand
        get() = if (data.getFlag(MetadataKeys.Mob.FLAGS, FLAG_LEFT_HANDED)) MainHand.LEFT else MainHand.RIGHT
        set(value) = data.setFlag(MetadataKeys.Mob.FLAGS, FLAG_LEFT_HANDED, value == MainHand.LEFT)
    final override var isAggressive: Boolean
        get() = data.getFlag(MetadataKeys.Mob.FLAGS, FLAG_AGGRESSIVE)
        set(value) = data.setFlag(MetadataKeys.Mob.FLAGS, FLAG_AGGRESSIVE, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Mob.FLAGS, 0)
    }

    override fun getEquipment(slot: EquipmentSlot): KryptonItemStack = when (slot.type) {
        EquipmentSlot.Type.HAND -> handItems.get(EquipmentSlots.index(slot))
        EquipmentSlot.Type.ARMOR -> armorItems.get(EquipmentSlots.index(slot))
    }

    override fun setEquipment(slot: EquipmentSlot, item: KryptonItemStack) {
        when (slot.type) {
            EquipmentSlot.Type.HAND -> handItems.set(EquipmentSlots.index(slot), item)
            EquipmentSlot.Type.ARMOR -> armorItems.set(EquipmentSlots.index(slot), item)
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

    protected fun target(): KryptonLivingEntity? = target

    open fun setTarget(target: KryptonLivingEntity?) {
        this.target = target
    }

    companion object {

        private const val FLAG_NO_AI = 0
        private const val FLAG_LEFT_HANDED = 1
        private const val FLAG_AGGRESSIVE = 2

        private const val DEFAULT_DROP_CHANCE = 0.085F

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonLivingEntity.attributes()
            .add(KryptonAttributeTypes.FOLLOW_RANGE, 16.0)
            .add(KryptonAttributeTypes.ATTACK_KNOCKBACK)
    }
}

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
package org.kryptonmc.krypton.entity

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.entity.Mob
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.EmptyItemStack
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.FixedList
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

    private val handItems = FixedList<KryptonItemStack>(2, EmptyItemStack)
    private val armorItems = FixedList<KryptonItemStack>(4, EmptyItemStack)
    private val handDropChances = FloatArray(2)
    private val armorDropChances = FloatArray(4)

    override val handSlots: Iterable<KryptonItemStack>
        get() = handItems
    override val armorSlots: Iterable<KryptonItemStack>
        get() = armorItems

    final override var canPickUpLoot = false
    final override var isPersistent = false
    open var target: KryptonLivingEntity? = null

    init {
        data.add(MetadataKeys.MOB.FLAGS)
    }

    override fun equipment(slot: EquipmentSlot): KryptonItemStack = when (slot.type) {
        EquipmentSlot.Type.HAND -> handItems[slot.index]
        EquipmentSlot.Type.ARMOR -> armorItems[slot.index]
    }

    override fun setEquipment(slot: EquipmentSlot, item: KryptonItemStack) = when (slot.type) {
        EquipmentSlot.Type.HAND -> handItems[slot.index] = item
        EquipmentSlot.Type.ARMOR -> armorItems[slot.index] = item
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
        list("ArmorDropChances") {
            armorDropChances.forEach(::addFloat)
        }
        list("HandDropChances") {
            handDropChances.forEach(::addFloat)
        }
        boolean("LeftHanded", mainHand == MainHand.LEFT)
        if (!hasAI) boolean("NoAI", true)
    }

    final override var hasAI: Boolean
        get() = data[MetadataKeys.MOB.FLAGS].toInt() and 1 == 0
        set(value) {
            val flags = data[MetadataKeys.MOB.FLAGS].toInt()
            data[MetadataKeys.MOB.FLAGS] = (if (value) flags or 1 else flags and -2).toByte()
        }
    final override var mainHand: MainHand
        get() = if (data[MetadataKeys.MOB.FLAGS].toInt() and 2 != 0) MainHand.LEFT else MainHand.RIGHT
        set(value) {
            val flags = data[MetadataKeys.MOB.FLAGS].toInt()
            data[MetadataKeys.MOB.FLAGS] = (if (value == MainHand.LEFT) flags or 1 else flags and -2).toByte()
        }
    final override var isAggressive: Boolean
        get() = data[MetadataKeys.MOB.FLAGS].toInt() and 4 != 0
        set(value) {
            val flags = data[MetadataKeys.MOB.FLAGS].toInt()
            data[MetadataKeys.MOB.FLAGS] = (if (value) flags or 4 else flags and -5).toByte()
        }

    companion object {

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonLivingEntity.attributes()
            .add(AttributeTypes.FOLLOW_RANGE, 16.0)
            .add(AttributeTypes.ATTACK_KNOCKBACK)
    }
}

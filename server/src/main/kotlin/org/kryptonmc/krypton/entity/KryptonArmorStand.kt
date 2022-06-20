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

import org.kryptonmc.api.entity.ArmorStand
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.FixedList
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3f

class KryptonArmorStand(world: KryptonWorld) : KryptonLivingEntity(world, EntityTypes.ARMOR_STAND, ATTRIBUTES), ArmorStand, KryptonEquipable {

    internal val armorItems = FixedList(4, KryptonItemStack.EMPTY)
    internal val handItems = FixedList(2, KryptonItemStack.EMPTY)
    internal var disabledSlots = 0

    override val handSlots: Iterable<KryptonItemStack>
        get() = handItems
    override val armorSlots: Iterable<KryptonItemStack>
        get() = armorItems
    internal val hasPhysics: Boolean
        get() = !isMarker && hasGravity

    override var isSmall: Boolean
        get() = getFlag(0)
        set(value) = setFlag(0, value)
    override var hasArms: Boolean
        get() = getFlag(2)
        set(value) = setFlag(2, value)
    override var hasBasePlate: Boolean
        get() = getFlag(3)
        set(value) = setFlag(3, value)
    override var isMarker: Boolean
        get() = getFlag(4)
        set(value) = setFlag(4, value)
    override var headPose: Vector3f
        get() = data[MetadataKeys.ARMOR_STAND.HEAD_ROTATION]
        set(value) = data.set(MetadataKeys.ARMOR_STAND.HEAD_ROTATION, value)
    override var bodyPose: Vector3f
        get() = data[MetadataKeys.ARMOR_STAND.BODY_ROTATION]
        set(value) = data.set(MetadataKeys.ARMOR_STAND.BODY_ROTATION, value)
    override var leftArmPose: Vector3f
        get() = data[MetadataKeys.ARMOR_STAND.LEFT_ARM_ROTATION]
        set(value) = data.set(MetadataKeys.ARMOR_STAND.LEFT_ARM_ROTATION, value)
    override var rightArmPose: Vector3f
        get() = data[MetadataKeys.ARMOR_STAND.RIGHT_ARM_ROTATION]
        set(value) = data.set(MetadataKeys.ARMOR_STAND.RIGHT_ARM_ROTATION, value)
    override var leftLegPose: Vector3f
        get() = data[MetadataKeys.ARMOR_STAND.LEFT_LEG_ROTATION]
        set(value) = data.set(MetadataKeys.ARMOR_STAND.LEFT_LEG_ROTATION, value)
    override var rightLegPose: Vector3f
        get() = data[MetadataKeys.ARMOR_STAND.RIGHT_LEG_ROTATION]
        set(value) = data.set(MetadataKeys.ARMOR_STAND.RIGHT_LEG_ROTATION, value)

    init {
        data.add(MetadataKeys.ARMOR_STAND.FLAGS)
        data.add(MetadataKeys.ARMOR_STAND.HEAD_ROTATION)
        data.add(MetadataKeys.ARMOR_STAND.BODY_ROTATION)
        data.add(MetadataKeys.ARMOR_STAND.LEFT_ARM_ROTATION)
        data.add(MetadataKeys.ARMOR_STAND.RIGHT_ARM_ROTATION)
        data.add(MetadataKeys.ARMOR_STAND.LEFT_LEG_ROTATION)
        data.add(MetadataKeys.ARMOR_STAND.RIGHT_LEG_ROTATION)
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

    private fun getFlag(flag: Int): Boolean = getFlag(MetadataKeys.ARMOR_STAND.FLAGS, flag)

    private fun setFlag(flag: Int, state: Boolean) {
        setFlag(MetadataKeys.ARMOR_STAND.FLAGS, flag, state)
    }
}

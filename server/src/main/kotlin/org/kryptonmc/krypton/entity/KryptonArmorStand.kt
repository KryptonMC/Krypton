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
import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.ArmorStandSerializer
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

    override var isSmall: Boolean
        get() = getFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_SMALL)
        set(value) = setFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_SMALL, value)
    override var hasArms: Boolean
        get() = getFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_ARMS)
        set(value) = setFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_ARMS, value)
    override var hasBasePlate: Boolean
        get() = getFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_BASE_PLATE)
        set(value) = setFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_BASE_PLATE, value)
    override var isMarker: Boolean
        get() = getFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_MARKER)
        set(value) = setFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_MARKER, value)
    override var headPose: Vector3f
        get() = data.get(MetadataKeys.ArmorStand.HEAD_ROTATION)
        set(value) = data.set(MetadataKeys.ArmorStand.HEAD_ROTATION, value)
    override var bodyPose: Vector3f
        get() = data.get(MetadataKeys.ArmorStand.BODY_ROTATION)
        set(value) = data.set(MetadataKeys.ArmorStand.BODY_ROTATION, value)
    override var leftArmPose: Vector3f
        get() = data.get(MetadataKeys.ArmorStand.LEFT_ARM_ROTATION)
        set(value) = data.set(MetadataKeys.ArmorStand.LEFT_ARM_ROTATION, value)
    override var rightArmPose: Vector3f
        get() = data.get(MetadataKeys.ArmorStand.RIGHT_ARM_ROTATION)
        set(value) = data.set(MetadataKeys.ArmorStand.RIGHT_ARM_ROTATION, value)
    override var leftLegPose: Vector3f
        get() = data.get(MetadataKeys.ArmorStand.LEFT_LEG_ROTATION)
        set(value) = data.set(MetadataKeys.ArmorStand.LEFT_LEG_ROTATION, value)
    override var rightLegPose: Vector3f
        get() = data.get(MetadataKeys.ArmorStand.RIGHT_LEG_ROTATION)
        set(value) = data.set(MetadataKeys.ArmorStand.RIGHT_LEG_ROTATION, value)

    init {
        data.add(MetadataKeys.ArmorStand.FLAGS, 0)
        data.add(MetadataKeys.ArmorStand.HEAD_ROTATION, ArmorStandSerializer.DEFAULT_HEAD_ROTATION)
        data.add(MetadataKeys.ArmorStand.BODY_ROTATION, ArmorStandSerializer.DEFAULT_BODY_ROTATION)
        data.add(MetadataKeys.ArmorStand.LEFT_ARM_ROTATION, ArmorStandSerializer.DEFAULT_LEFT_ARM_ROTATION)
        data.add(MetadataKeys.ArmorStand.RIGHT_ARM_ROTATION, ArmorStandSerializer.DEFAULT_RIGHT_ARM_ROTATION)
        data.add(MetadataKeys.ArmorStand.LEFT_LEG_ROTATION, ArmorStandSerializer.DEFAULT_LEFT_LEG_ROTATION)
        data.add(MetadataKeys.ArmorStand.RIGHT_LEG_ROTATION, ArmorStandSerializer.DEFAULT_RIGHT_LEG_ROTATION)
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

    companion object {

        private const val FLAG_SMALL = 0
        private const val FLAG_ARMS = 2
        private const val FLAG_BASE_PLATE = 3
        private const val FLAG_MARKER = 4
    }
}

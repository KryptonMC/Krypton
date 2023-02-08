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
import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.util.Rotation
import org.kryptonmc.krypton.entity.components.KryptonEquipable
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.ArmorStandSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.util.EquipmentSlots
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.collection.FixedList
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonArmorStand(world: KryptonWorld) : KryptonLivingEntity(world), ArmorStand, KryptonEquipable {

    override val type: KryptonEntityType<KryptonArmorStand>
        get() = KryptonEntityTypes.ARMOR_STAND
    override val serializer: EntitySerializer<KryptonArmorStand>
        get() = ArmorStandSerializer

    internal val armorItems = FixedList(4, KryptonItemStack.EMPTY)
    internal val handItems = FixedList(2, KryptonItemStack.EMPTY)
    internal var disabledSlots = 0

    override var isSmall: Boolean
        get() = data.getFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_SMALL)
        set(value) = data.setFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_SMALL, value)
    override var hasArms: Boolean
        get() = data.getFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_ARMS)
        set(value) = data.setFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_ARMS, value)
    override var hasBasePlate: Boolean
        get() = data.getFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_BASE_PLATE)
        set(value) = data.setFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_BASE_PLATE, value)
    override var isMarker: Boolean
        get() = data.getFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_MARKER)
        set(value) = data.setFlag(MetadataKeys.ArmorStand.FLAGS, FLAG_MARKER, value)
    override var headPose: Rotation
        get() = data.get(MetadataKeys.ArmorStand.HEAD_ROTATION)
        set(value) = data.set(MetadataKeys.ArmorStand.HEAD_ROTATION, value)
    override var bodyPose: Rotation
        get() = data.get(MetadataKeys.ArmorStand.BODY_ROTATION)
        set(value) = data.set(MetadataKeys.ArmorStand.BODY_ROTATION, value)
    override var leftArmPose: Rotation
        get() = data.get(MetadataKeys.ArmorStand.LEFT_ARM_ROTATION)
        set(value) = data.set(MetadataKeys.ArmorStand.LEFT_ARM_ROTATION, value)
    override var rightArmPose: Rotation
        get() = data.get(MetadataKeys.ArmorStand.RIGHT_ARM_ROTATION)
        set(value) = data.set(MetadataKeys.ArmorStand.RIGHT_ARM_ROTATION, value)
    override var leftLegPose: Rotation
        get() = data.get(MetadataKeys.ArmorStand.LEFT_LEG_ROTATION)
        set(value) = data.set(MetadataKeys.ArmorStand.LEFT_LEG_ROTATION, value)
    override var rightLegPose: Rotation
        get() = data.get(MetadataKeys.ArmorStand.RIGHT_LEG_ROTATION)
        set(value) = data.set(MetadataKeys.ArmorStand.RIGHT_LEG_ROTATION, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.ArmorStand.FLAGS, 0)
        data.define(MetadataKeys.ArmorStand.HEAD_ROTATION, ArmorStandSerializer.DEFAULT_HEAD_ROTATION)
        data.define(MetadataKeys.ArmorStand.BODY_ROTATION, ArmorStandSerializer.DEFAULT_BODY_ROTATION)
        data.define(MetadataKeys.ArmorStand.LEFT_ARM_ROTATION, ArmorStandSerializer.DEFAULT_LEFT_ARM_ROTATION)
        data.define(MetadataKeys.ArmorStand.RIGHT_ARM_ROTATION, ArmorStandSerializer.DEFAULT_RIGHT_ARM_ROTATION)
        data.define(MetadataKeys.ArmorStand.LEFT_LEG_ROTATION, ArmorStandSerializer.DEFAULT_LEFT_LEG_ROTATION)
        data.define(MetadataKeys.ArmorStand.RIGHT_LEG_ROTATION, ArmorStandSerializer.DEFAULT_RIGHT_LEG_ROTATION)
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

    companion object {

        private const val FLAG_SMALL = 0
        private const val FLAG_ARMS = 2
        private const val FLAG_BASE_PLATE = 3
        private const val FLAG_MARKER = 4
    }
}

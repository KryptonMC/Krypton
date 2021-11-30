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

import org.kryptonmc.api.entity.ArmorStand
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.EmptyItemStack
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.FixedList
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.FloatTag
import org.spongepowered.math.vector.Vector3f

class KryptonArmorStand(world: KryptonWorld) : KryptonLivingEntity(world, EntityTypes.ARMOR_STAND, ATTRIBUTES), ArmorStand, KryptonEquipable {

    private val handItems = FixedList<KryptonItemStack>(2, EmptyItemStack)
    private val armorItems = FixedList<KryptonItemStack>(4, EmptyItemStack)
    private var disabledSlots = 0

    override val handSlots: Iterable<KryptonItemStack>
        get() = handItems
    override val armorSlots: Iterable<KryptonItemStack>
        get() = armorItems
    private val hasPhysics: Boolean
        get() = !isMarker && hasGravity

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

    override fun setEquipment(slot: EquipmentSlot, item: KryptonItemStack) = when (slot.type) {
        EquipmentSlot.Type.HAND -> handItems[slot.index] = item
        EquipmentSlot.Type.ARMOR -> armorItems[slot.index] = item
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        loadItems(tag, "ArmorItems", armorItems)
        loadItems(tag, "HandItems", handItems)

        isInvisible = tag.getBoolean("Invisible")
        isSmall = tag.getBoolean("Small")
        hasArms = tag.getBoolean("ShowArms")
        disabledSlots = tag.getInt("DisabledSlots")
        hasBasePlate = !tag.getBoolean("NoBasePlate")
        isMarker = tag.getBoolean("Marker")
        noPhysics = !hasPhysics

        if (tag.contains("Pose", CompoundTag.ID)) {
            val headRotation = tag.getList("Head", FloatTag.ID)
            headPose = if (headRotation.isEmpty()) MetadataKeys.ARMOR_STAND.HEAD_ROTATION.default else headRotation.readRotation()
            val bodyRotation = tag.getList("Body", FloatTag.ID)
            bodyPose = if (bodyRotation.isEmpty()) MetadataKeys.ARMOR_STAND.BODY_ROTATION.default else bodyRotation.readRotation()
            val leftArmRotation = tag.getList("LeftArm", FloatTag.ID)
            leftArmPose = if (leftArmRotation.isEmpty()) MetadataKeys.ARMOR_STAND.LEFT_ARM_ROTATION.default else leftArmRotation.readRotation()
            val rightArmRotation = tag.getList("RightArm", FloatTag.ID)
            rightArmPose = if (rightArmRotation.isEmpty()) MetadataKeys.ARMOR_STAND.RIGHT_ARM_ROTATION.default else rightArmRotation.readRotation()
            val leftLegRotation = tag.getList("LeftLeg", FloatTag.ID)
            leftLegPose = if (leftLegRotation.isEmpty()) MetadataKeys.ARMOR_STAND.LEFT_LEG_ROTATION.default else leftLegRotation.readRotation()
            val rightLegRotation = tag.getList("RightLeg", FloatTag.ID)
            rightLegPose = if (rightLegRotation.isEmpty()) MetadataKeys.ARMOR_STAND.RIGHT_LEG_ROTATION.default else rightLegRotation.readRotation()
        }
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        put("ArmorItems", saveItems(armorItems))
        put("HandItems", saveItems(handItems))

        boolean("Invisible", isInvisible)
        boolean("Small", isSmall)
        boolean("ShowArms", hasArms)
        int("DisabledSlots", disabledSlots)
        boolean("NoBasePlate", !hasBasePlate)
        if (isMarker) boolean("Marker", isMarker)

        compound("Pose") {
            if (headPose != MetadataKeys.ARMOR_STAND.HEAD_ROTATION.default) rotation("Head", headPose)
            if (bodyPose != MetadataKeys.ARMOR_STAND.BODY_ROTATION.default) rotation("Body", bodyPose)
            if (leftArmPose != MetadataKeys.ARMOR_STAND.LEFT_ARM_ROTATION.default) rotation("LeftArm", leftArmPose)
            if (rightArmPose != MetadataKeys.ARMOR_STAND.RIGHT_ARM_ROTATION.default) rotation("RightArm", rightArmPose)
            if (leftLegPose != MetadataKeys.ARMOR_STAND.LEFT_LEG_ROTATION.default) rotation("LeftLeg", leftLegPose)
            if (rightLegPose != MetadataKeys.ARMOR_STAND.RIGHT_LEG_ROTATION.default) rotation("RightLeg", rightLegPose)
        }
    }

    override var isSmall: Boolean
        get() = data[MetadataKeys.ARMOR_STAND.FLAGS].toInt() and 1 > 0
        set(value) {
            val flags = data[MetadataKeys.ARMOR_STAND.FLAGS].toInt()
            data[MetadataKeys.ARMOR_STAND.FLAGS] = (if (value) flags or 1 else flags and -2).toByte()
        }
    override var hasArms: Boolean
        get() = data[MetadataKeys.ARMOR_STAND.FLAGS].toInt() and 4 > 0
        set(value) {
            val flags = data[MetadataKeys.ARMOR_STAND.FLAGS].toInt()
            data[MetadataKeys.ARMOR_STAND.FLAGS] = (if (value) flags or 4 else flags and -5).toByte()
        }
    override var hasBasePlate: Boolean
        get() = data[MetadataKeys.ARMOR_STAND.FLAGS].toInt() and 8 <= 0
        set(value) {
            val flags = data[MetadataKeys.ARMOR_STAND.FLAGS].toInt()
            data[MetadataKeys.ARMOR_STAND.FLAGS] = (if (value) flags and -9 else flags or 8).toByte()
        }
    override var isMarker: Boolean
        get() = data[MetadataKeys.ARMOR_STAND.FLAGS].toInt() and 16 > 0
        set(value) {
            val flags = data[MetadataKeys.ARMOR_STAND.FLAGS].toInt()
            data[MetadataKeys.ARMOR_STAND.FLAGS] = (if (value) flags or 16 else flags and -17).toByte()
        }
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
}

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
package org.kryptonmc.krypton.entity.serializer

import org.kryptonmc.krypton.entity.KryptonArmorStand
import org.kryptonmc.krypton.entity.KryptonEquipable
import org.kryptonmc.krypton.entity.readRotation
import org.kryptonmc.krypton.entity.rotation
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.FloatTag
import org.spongepowered.math.vector.Vector3f

object ArmorStandSerializer : EntitySerializer<KryptonArmorStand> {

    @JvmField
    val DEFAULT_HEAD_ROTATION: Vector3f = Vector3f.ZERO
    @JvmField
    val DEFAULT_BODY_ROTATION: Vector3f = Vector3f.ZERO
    @JvmField
    val DEFAULT_LEFT_ARM_ROTATION: Vector3f = Vector3f(-10F, 0F, -10F)
    @JvmField
    val DEFAULT_RIGHT_ARM_ROTATION: Vector3f = Vector3f(-15F, 0F, 10F)
    @JvmField
    val DEFAULT_LEFT_LEG_ROTATION: Vector3f = Vector3f(-1F, 0F, -1F)
    @JvmField
    val DEFAULT_RIGHT_LEG_ROTATION: Vector3f = Vector3f(1F, 0F, 1F)

    override fun load(entity: KryptonArmorStand, data: CompoundTag) {
        LivingEntitySerializer.load(entity, data)
        KryptonEquipable.loadItems(data, "ArmorItems", entity.armorItems)
        KryptonEquipable.loadItems(data, "HandItems", entity.handItems)

        entity.isInvisible = data.getBoolean("Invisible")
        entity.isSmall = data.getBoolean("Small")
        entity.hasArms = data.getBoolean("ShowArms")
        entity.disabledSlots = data.getInt("DisabledSlots")
        entity.hasBasePlate = !data.getBoolean("NoBasePlate")
        entity.isMarker = data.getBoolean("Marker")

        if (!data.contains("Pose", CompoundTag.ID)) return
        val headRotation = data.getList("Head", FloatTag.ID)
        entity.headPose = if (headRotation.isEmpty()) DEFAULT_HEAD_ROTATION else headRotation.readRotation()
        val bodyRotation = data.getList("Body", FloatTag.ID)
        entity.bodyPose = if (bodyRotation.isEmpty()) DEFAULT_BODY_ROTATION else bodyRotation.readRotation()
        val leftArmRotation = data.getList("LeftArm", FloatTag.ID)
        entity.leftArmPose = if (leftArmRotation.isEmpty()) DEFAULT_LEFT_ARM_ROTATION else leftArmRotation.readRotation()
        val rightArmRotation = data.getList("RightArm", FloatTag.ID)
        entity.rightArmPose = if (rightArmRotation.isEmpty()) DEFAULT_RIGHT_ARM_ROTATION else rightArmRotation.readRotation()
        val leftLegRotation = data.getList("LeftLeg", FloatTag.ID)
        entity.leftLegPose = if (leftLegRotation.isEmpty()) DEFAULT_LEFT_LEG_ROTATION else leftLegRotation.readRotation()
        val rightLegRotation = data.getList("RightLeg", FloatTag.ID)
        entity.rightLegPose = if (rightLegRotation.isEmpty()) DEFAULT_RIGHT_LEG_ROTATION else rightLegRotation.readRotation()
    }

    override fun save(entity: KryptonArmorStand): CompoundTag.Builder = LivingEntitySerializer.save(entity).apply {
        put("ArmorItems", KryptonEquipable.saveItems(entity.armorItems))
        put("HandItems", KryptonEquipable.saveItems(entity.handItems))

        boolean("Invisible", entity.isInvisible)
        boolean("Small", entity.isSmall)
        boolean("ShowArms", entity.hasArms)
        int("DisabledSlots", entity.disabledSlots)
        boolean("NoBasePlate", !entity.hasBasePlate)
        if (entity.isMarker) boolean("Marker", true)

        compound("Pose") {
            if (entity.headPose != DEFAULT_HEAD_ROTATION) rotation("Head", entity.headPose)
            if (entity.bodyPose != DEFAULT_BODY_ROTATION) rotation("Body", entity.bodyPose)
            if (entity.leftArmPose != DEFAULT_LEFT_ARM_ROTATION) rotation("LeftArm", entity.leftArmPose)
            if (entity.rightArmPose != DEFAULT_RIGHT_ARM_ROTATION) rotation("RightArm", entity.rightArmPose)
            if (entity.leftLegPose != DEFAULT_LEFT_LEG_ROTATION) rotation("LeftLeg", entity.leftLegPose)
            if (entity.rightLegPose != DEFAULT_RIGHT_LEG_ROTATION) rotation("RightLeg", entity.rightLegPose)
        }
    }
}

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

import org.kryptonmc.api.util.Rotations
import org.kryptonmc.krypton.entity.KryptonArmorStand
import org.kryptonmc.krypton.entity.KryptonEquipable
import org.kryptonmc.krypton.util.RotationsImpl
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.compound
import org.kryptonmc.nbt.list

object ArmorStandSerializer : EntitySerializer<KryptonArmorStand> {

    private const val ARMOR_ITEMS_TAG = "ArmorItems"
    private const val HAND_ITEMS_TAG = "HandItems"
    private const val INVISIBLE_TAG = "Invisible"
    private const val SMALL_TAG = "Small"
    private const val SHOW_ARMS_TAG = "ShowArms"
    private const val DISABLED_SLOTS_TAG = "DisabledSlots"
    private const val NO_BASE_PLATE_TAG = "NoBasePlate"
    private const val MARKER_TAG = "Marker"
    private const val POSE_TAG = "Pose"
    private const val HEAD_TAG = "Head"
    private const val BODY_TAG = "Body"
    private const val LEFT_ARM_TAG = "LeftArm"
    private const val RIGHT_ARM_TAG = "RightArm"
    private const val LEFT_LEG_TAG = "LeftLeg"
    private const val RIGHT_LEG_TAG = "RightLeg"

    @JvmField
    val DEFAULT_HEAD_ROTATION: Rotations = RotationsImpl.ZERO
    @JvmField
    val DEFAULT_BODY_ROTATION: Rotations = RotationsImpl.ZERO
    @JvmField
    val DEFAULT_LEFT_ARM_ROTATION: Rotations = RotationsImpl(-10F, 0F, -10F)
    @JvmField
    val DEFAULT_RIGHT_ARM_ROTATION: Rotations = RotationsImpl(-15F, 0F, 10F)
    @JvmField
    val DEFAULT_LEFT_LEG_ROTATION: Rotations = RotationsImpl(-1F, 0F, -1F)
    @JvmField
    val DEFAULT_RIGHT_LEG_ROTATION: Rotations = RotationsImpl(1F, 0F, 1F)

    override fun load(entity: KryptonArmorStand, data: CompoundTag) {
        LivingEntitySerializer.load(entity, data)
        KryptonEquipable.loadItems(data, ARMOR_ITEMS_TAG, entity.armorItems)
        KryptonEquipable.loadItems(data, HAND_ITEMS_TAG, entity.handItems)

        entity.isInvisible = data.getBoolean(INVISIBLE_TAG)
        entity.isSmall = data.getBoolean(SMALL_TAG)
        entity.hasArms = data.getBoolean(SHOW_ARMS_TAG)
        entity.disabledSlots = data.getInt(DISABLED_SLOTS_TAG)
        entity.hasBasePlate = !data.getBoolean(NO_BASE_PLATE_TAG)
        entity.isMarker = data.getBoolean(MARKER_TAG)

        if (data.contains(POSE_TAG, CompoundTag.ID)) {
            val pose = data.getCompound(POSE_TAG)
            entity.headPose = pose.getPose(HEAD_TAG, DEFAULT_HEAD_ROTATION)
            entity.bodyPose = pose.getPose(BODY_TAG, DEFAULT_BODY_ROTATION)
            entity.leftArmPose = pose.getPose(LEFT_ARM_TAG, DEFAULT_LEFT_ARM_ROTATION)
            entity.rightArmPose = pose.getPose(RIGHT_ARM_TAG, DEFAULT_RIGHT_ARM_ROTATION)
            entity.leftLegPose = pose.getPose(LEFT_LEG_TAG, DEFAULT_LEFT_LEG_ROTATION)
            entity.rightLegPose = pose.getPose(RIGHT_LEG_TAG, DEFAULT_RIGHT_LEG_ROTATION)
        }
    }

    override fun save(entity: KryptonArmorStand): CompoundTag.Builder = LivingEntitySerializer.save(entity).apply {
        put(ARMOR_ITEMS_TAG, KryptonEquipable.saveItems(entity.armorItems))
        put(HAND_ITEMS_TAG, KryptonEquipable.saveItems(entity.handItems))

        putBoolean(INVISIBLE_TAG, entity.isInvisible)
        putBoolean(SMALL_TAG, entity.isSmall)
        putBoolean(SHOW_ARMS_TAG, entity.hasArms)
        putInt(DISABLED_SLOTS_TAG, entity.disabledSlots)
        putBoolean(NO_BASE_PLATE_TAG, !entity.hasBasePlate)
        if (entity.isMarker) putBoolean(MARKER_TAG, true)

        compound(POSE_TAG) {
            putPose(HEAD_TAG, entity.headPose, DEFAULT_HEAD_ROTATION)
            putPose(BODY_TAG, entity.bodyPose, DEFAULT_BODY_ROTATION)
            putPose(LEFT_ARM_TAG, entity.leftArmPose, DEFAULT_LEFT_ARM_ROTATION)
            putPose(RIGHT_ARM_TAG, entity.rightArmPose, DEFAULT_RIGHT_ARM_ROTATION)
            putPose(LEFT_LEG_TAG, entity.leftLegPose, DEFAULT_LEFT_LEG_ROTATION)
            putPose(RIGHT_LEG_TAG, entity.rightLegPose, DEFAULT_RIGHT_LEG_ROTATION)
        }
    }
}

private fun CompoundTag.getPose(name: String, default: Rotations): Rotations {
    val tag = getList(name, FloatTag.ID)
    return if (tag.isEmpty) default else RotationsImpl(tag.getFloat(0), tag.getFloat(1), tag.getFloat(2))
}

private fun CompoundTag.Builder.putPose(key: String, pose: Rotations, default: Rotations): CompoundTag.Builder {
    if (pose == default) return this
    return list(key) {
        addFloat(pose.yaw)
        addFloat(pose.pitch)
        addFloat(pose.roll)
    }
}

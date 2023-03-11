/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.entity.serializer

import org.kryptonmc.api.util.Rotation
import org.kryptonmc.krypton.entity.KryptonArmorStand
import org.kryptonmc.krypton.entity.components.KryptonEquipable
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
    val DEFAULT_HEAD_ROTATION: Rotation = Rotation.ZERO
    @JvmField
    val DEFAULT_BODY_ROTATION: Rotation = Rotation.ZERO
    @JvmField
    val DEFAULT_LEFT_ARM_ROTATION: Rotation = Rotation(-10F, 0F, -10F)
    @JvmField
    val DEFAULT_RIGHT_ARM_ROTATION: Rotation = Rotation(-15F, 0F, 10F)
    @JvmField
    val DEFAULT_LEFT_LEG_ROTATION: Rotation = Rotation(-1F, 0F, -1F)
    @JvmField
    val DEFAULT_RIGHT_LEG_ROTATION: Rotation = Rotation(1F, 0F, 1F)

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
            entity.headPose = getPose(pose, HEAD_TAG, DEFAULT_HEAD_ROTATION)
            entity.bodyPose = getPose(pose, BODY_TAG, DEFAULT_BODY_ROTATION)
            entity.leftArmPose = getPose(pose, LEFT_ARM_TAG, DEFAULT_LEFT_ARM_ROTATION)
            entity.rightArmPose = getPose(pose, RIGHT_ARM_TAG, DEFAULT_RIGHT_ARM_ROTATION)
            entity.leftLegPose = getPose(pose, LEFT_LEG_TAG, DEFAULT_LEFT_LEG_ROTATION)
            entity.rightLegPose = getPose(pose, RIGHT_LEG_TAG, DEFAULT_RIGHT_LEG_ROTATION)
        }
    }

    @JvmStatic
    private fun getPose(data: CompoundTag, name: String, default: Rotation): Rotation {
        val tag = data.getList(name, FloatTag.ID)
        return if (tag.isEmpty) default else Rotation(tag.getFloat(0), tag.getFloat(1), tag.getFloat(2))
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
            putPose(this, HEAD_TAG, entity.headPose, DEFAULT_HEAD_ROTATION)
            putPose(this, BODY_TAG, entity.bodyPose, DEFAULT_BODY_ROTATION)
            putPose(this, LEFT_ARM_TAG, entity.leftArmPose, DEFAULT_LEFT_ARM_ROTATION)
            putPose(this, RIGHT_ARM_TAG, entity.rightArmPose, DEFAULT_RIGHT_ARM_ROTATION)
            putPose(this, LEFT_LEG_TAG, entity.leftLegPose, DEFAULT_LEFT_LEG_ROTATION)
            putPose(this, RIGHT_LEG_TAG, entity.rightLegPose, DEFAULT_RIGHT_LEG_ROTATION)
        }
    }

    @JvmStatic
    private fun putPose(data: CompoundTag.Builder, key: String, pose: Rotation, default: Rotation): CompoundTag.Builder {
        if (pose == default) return data
        return data.list(key) {
            addFloat(pose.x)
            addFloat(pose.y)
            addFloat(pose.z)
        }
    }
}

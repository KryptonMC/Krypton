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
import org.kryptonmc.api.space.Rotation
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonArmorStand(world: KryptonWorld) : KryptonLivingEntity(world, EntityTypes.ARMOR_STAND), ArmorStand {

    init {
        data += MetadataKeys.ARMOR_STAND.FLAGS
        data += MetadataKeys.ARMOR_STAND.HEAD_ROTATION
        data += MetadataKeys.ARMOR_STAND.BODY_ROTATION
        data += MetadataKeys.ARMOR_STAND.LEFT_ARM_ROTATION
        data += MetadataKeys.ARMOR_STAND.RIGHT_ARM_ROTATION
        data += MetadataKeys.ARMOR_STAND.LEFT_LEG_ROTATION
        data += MetadataKeys.ARMOR_STAND.RIGHT_LEG_ROTATION
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        isInvisible = tag.getBoolean("Invisible")
        isMarker = tag.getBoolean("Marker")
        hasBasePlate = !tag.getBoolean("NoBasePlate")
        if (tag.contains("Pose", CompoundTag.ID)) {
            tag.getRotation("Body")?.let { bodyPose = it }
            tag.getRotation("Head")?.let { headPose = it }
            tag.getRotation("LeftArm")?.let { leftArmPose = it }
            tag.getRotation("RightArm")?.let { rightArmPose = it }
            tag.getRotation("LeftLeg")?.let { leftLegPose = it }
            tag.getRotation("RightLeg")?.let { rightLegPose = it }
        }
        hasArms = tag.getBoolean("ShowArms")
        isSmall = tag.getBoolean("Small")
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

    override var headPose: Rotation
        get() = data[MetadataKeys.ARMOR_STAND.HEAD_ROTATION]
        set(value) = data.set(MetadataKeys.ARMOR_STAND.HEAD_ROTATION, value)

    override var bodyPose: Rotation
        get() = data[MetadataKeys.ARMOR_STAND.BODY_ROTATION]
        set(value) = data.set(MetadataKeys.ARMOR_STAND.BODY_ROTATION, value)

    override var leftArmPose: Rotation
        get() = data[MetadataKeys.ARMOR_STAND.LEFT_ARM_ROTATION]
        set(value) = data.set(MetadataKeys.ARMOR_STAND.LEFT_ARM_ROTATION, value)

    override var rightArmPose: Rotation
        get() = data[MetadataKeys.ARMOR_STAND.RIGHT_ARM_ROTATION]
        set(value) = data.set(MetadataKeys.ARMOR_STAND.RIGHT_ARM_ROTATION, value)

    override var leftLegPose: Rotation
        get() = data[MetadataKeys.ARMOR_STAND.LEFT_LEG_ROTATION]
        set(value) = data.set(MetadataKeys.ARMOR_STAND.LEFT_LEG_ROTATION, value)

    override var rightLegPose: Rotation
        get() = data[MetadataKeys.ARMOR_STAND.RIGHT_LEG_ROTATION]
        set(value) = data.set(MetadataKeys.ARMOR_STAND.RIGHT_LEG_ROTATION, value)
}

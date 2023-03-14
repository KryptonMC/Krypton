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

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.util.Position
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.krypton.util.nbt.hasUUID
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.buildCompound
import kotlin.math.abs

object BaseEntitySerializer : EntitySerializer<KryptonEntity> {

    private const val ID_TAG = "id"
    private const val AIR_TAG = "Air"
    private const val CUSTOM_NAME_TAG = "CustomName"
    private const val CUSTOM_NAME_VISIBLE_TAG = "CustomNameVisible"
    private const val FALL_DISTANCE_TAG = "FallDistance"
    private const val FIRE_TAG = "Fire"
    private const val GLOWING_TAG = "Glowing"
    private const val INVULNERABLE_TAG = "Invulnerable"
    private const val MOTION_TAG = "Motion"
    private const val NO_GRAVITY_TAG = "NoGravity"
    private const val ON_GROUND_TAG = "OnGround"
    private const val POSITION_TAG = "Pos"
    private const val ROTATION_TAG = "Rotation"
    private const val SILENT_TAG = "Silent"
    private const val FROZEN_TICKS_TAG = "TicksFrozen"
    private const val UUID_TAG = "UUID"

    private const val MAXIMUM_MOTION_VALUE = 10.0

    override fun load(entity: KryptonEntity, data: CompoundTag) {
        entity.airSupply = data.getShort(AIR_TAG).toInt()
        if (data.contains(CUSTOM_NAME_TAG, StringTag.ID)) {
            entity.customName = GsonComponentSerializer.gson().deserialize(data.getString(CUSTOM_NAME_TAG))
        }
        entity.isCustomNameVisible = data.getBoolean(CUSTOM_NAME_VISIBLE_TAG)
        entity.fallDistance = data.getFloat(FALL_DISTANCE_TAG)
        entity.remainingFireTicks = data.getShort(FIRE_TAG).toInt()
        entity.isGlowing = data.getBoolean(GLOWING_TAG)
        entity.isInvulnerable = data.getBoolean(INVULNERABLE_TAG)

        val motion = data.getList(MOTION_TAG, DoubleTag.ID)
        entity.velocity = Vec3d(getMotionValue(motion, 0), getMotionValue(motion, 1), getMotionValue(motion, 2))

        entity.hasGravity = !data.getBoolean(NO_GRAVITY_TAG)
        entity.isOnGround = data.getBoolean(ON_GROUND_TAG)

        val location = data.getList(POSITION_TAG, DoubleTag.ID)
        val rotation = data.getList(ROTATION_TAG, FloatTag.ID)
        entity.position = Position(location.getDouble(0), location.getDouble(1), location.getDouble(2), rotation.getFloat(0), rotation.getFloat(1))

        entity.isSilent = data.getBoolean(SILENT_TAG)
        entity.frozenTicks = data.getInt(FROZEN_TICKS_TAG)
        if (data.hasUUID(UUID_TAG)) entity.uuid = data.getUUID(UUID_TAG)
    }

    override fun save(entity: KryptonEntity): CompoundTag.Builder = buildCompound {
        // Display name
        if (entity.isCustomNameVisible) putBoolean(CUSTOM_NAME_VISIBLE_TAG, true)
        entity.customName?.let { putString(CUSTOM_NAME_TAG, GsonComponentSerializer.gson().serialize(it)) }

        // Flags
        if (entity.isGlowing) putBoolean(GLOWING_TAG, true)
        if (entity.isInvulnerable) putBoolean(INVULNERABLE_TAG, true)
        if (!entity.hasGravity) putBoolean(NO_GRAVITY_TAG, true)
        putBoolean(ON_GROUND_TAG, entity.isOnGround)
        if (entity.isSilent) putBoolean(SILENT_TAG, true)

        // Positioning
        putList(MOTION_TAG, DoubleTag.ID, DoubleTag.of(entity.velocity.x), DoubleTag.of(entity.velocity.y), DoubleTag.of(entity.velocity.z))
        putList(POSITION_TAG, DoubleTag.ID, DoubleTag.of(entity.position.x), DoubleTag.of(entity.position.y), DoubleTag.of(entity.position.z))
        putList(ROTATION_TAG, FloatTag.ID, FloatTag.of(entity.position.yaw), FloatTag.of(entity.position.pitch))

        // Identification
        if (entity !is KryptonPlayer) putString(ID_TAG, entity.type.key().asString())
        putUUID(UUID_TAG, entity.uuid)

        // Miscellaneous
        putShort(AIR_TAG, entity.airSupply.toShort())
        putShort(FIRE_TAG, entity.remainingFireTicks.toShort())
        putInt(FROZEN_TICKS_TAG, entity.frozenTicks)
        putFloat(FALL_DISTANCE_TAG, entity.fallDistance)
    }

    @JvmStatic
    private fun getMotionValue(motion: ListTag, index: Int): Double {
        val value = motion.getDouble(index)
        return if (abs(value) < MAXIMUM_MOTION_VALUE) value else 0.0
    }
}

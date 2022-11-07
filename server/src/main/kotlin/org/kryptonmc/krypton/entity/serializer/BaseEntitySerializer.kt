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

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.adventure.toJson
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
import org.spongepowered.math.vector.Vector2f
import org.spongepowered.math.vector.Vector3d
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

    override fun load(entity: KryptonEntity, data: CompoundTag) {
        entity.air = data.getShort(AIR_TAG).toInt()
        if (data.contains(CUSTOM_NAME_TAG, StringTag.ID)) {
            entity.customName = GsonComponentSerializer.gson().deserialize(data.getString(CUSTOM_NAME_TAG))
        }
        entity.isCustomNameVisible = data.getBoolean(CUSTOM_NAME_VISIBLE_TAG)
        entity.fallDistance = data.getFloat(FALL_DISTANCE_TAG)
        entity.fireTicks = data.getShort(FIRE_TAG).toInt()
        entity.isGlowing = data.getBoolean(GLOWING_TAG)
        entity.isInvulnerable = data.getBoolean(INVULNERABLE_TAG)

        val motion = data.getList(MOTION_TAG, DoubleTag.ID)
        entity.velocity = Vector3d(motion.getMotionValue(0), motion.getMotionValue(1), motion.getMotionValue(2))

        entity.hasGravity = !data.getBoolean(NO_GRAVITY_TAG)
        entity.isOnGround = data.getBoolean(ON_GROUND_TAG)

        val location = data.getList(POSITION_TAG, DoubleTag.ID)
        val rotation = data.getList(ROTATION_TAG, FloatTag.ID)
        entity.location = Vector3d(location.getDouble(0), location.getDouble(1), location.getDouble(2))
        entity.rotation = Vector2f(rotation.getFloat(0), rotation.getFloat(1))

        entity.isSilent = data.getBoolean(SILENT_TAG)
        entity.frozenTicks = data.getInt(FROZEN_TICKS_TAG)
        if (data.hasUUID(UUID_TAG)) entity.uuid = data.getUUID(UUID_TAG)!!
    }

    override fun save(entity: KryptonEntity): CompoundTag.Builder = buildCompound {
        // Display name
        if (entity.isCustomNameVisible) putBoolean(CUSTOM_NAME_VISIBLE_TAG, true)
        entity.customName?.let { putString(CUSTOM_NAME_TAG, it.toJson()) }

        // Flags
        if (entity.isGlowing) putBoolean(GLOWING_TAG, true)
        if (entity.isInvulnerable) putBoolean(INVULNERABLE_TAG, true)
        if (!entity.hasGravity) putBoolean(NO_GRAVITY_TAG, true)
        putBoolean(ON_GROUND_TAG, entity.isOnGround)
        if (entity.isSilent) putBoolean(SILENT_TAG, true)

        // Positioning
        putList(MOTION_TAG, DoubleTag.ID, DoubleTag.of(entity.velocity.x()), DoubleTag.of(entity.velocity.y()), DoubleTag.of(entity.velocity.z()))
        putList(POSITION_TAG, DoubleTag.ID, DoubleTag.of(entity.location.x()), DoubleTag.of(entity.location.y()), DoubleTag.of(entity.location.z()))
        putList(ROTATION_TAG, FloatTag.ID, FloatTag.of(entity.rotation.x()), FloatTag.of(entity.rotation.y()))

        // Identification
        if (entity !is KryptonPlayer) putString(ID_TAG, entity.type.key().asString())
        putUUID(UUID_TAG, entity.uuid)

        // Miscellaneous
        putShort(AIR_TAG, entity.air.toShort())
        putShort(FIRE_TAG, entity.fireTicks.toShort())
        putInt(FROZEN_TICKS_TAG, entity.frozenTicks)
        putFloat(FALL_DISTANCE_TAG, entity.fallDistance)
    }
}

private const val MAXIMUM_MOTION_VALUE = 10.0

private fun ListTag.getMotionValue(index: Int): Double {
    val value = getDouble(index)
    return if (abs(value) < MAXIMUM_MOTION_VALUE) value else 0.0
}

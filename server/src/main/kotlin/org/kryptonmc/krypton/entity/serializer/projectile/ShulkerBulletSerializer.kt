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
package org.kryptonmc.krypton.entity.serializer.projectile

import org.kryptonmc.krypton.entity.projectile.KryptonShulkerBullet
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.Directions
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.krypton.util.nbt.hasNumber
import org.kryptonmc.krypton.util.nbt.hasUUID
import org.kryptonmc.krypton.util.nbt.putNullable
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag

object ShulkerBulletSerializer : EntitySerializer<KryptonShulkerBullet> {

    private const val STEPS_TAG = "Steps"
    private const val DELTA_X_TAG = "TXD"
    private const val DELTA_Y_TAG = "TYD"
    private const val DELTA_Z_TAG = "TZD"
    private const val DIR_TAG = "Dir"
    private const val TARGET_TAG = "Target"

    override fun load(entity: KryptonShulkerBullet, data: CompoundTag) {
        ProjectileSerializer.load(entity, data)
        entity.steps = data.getInt(STEPS_TAG)
        entity.targetDeltaX = data.getDouble(DELTA_X_TAG)
        entity.targetDeltaY = data.getDouble(DELTA_Y_TAG)
        entity.targetDeltaZ = data.getDouble(DELTA_Z_TAG)
        if (data.hasNumber(DIR_TAG)) entity.movingDirection = Directions.of3D(data.getInt(DIR_TAG))
        if (data.hasUUID(TARGET_TAG)) entity.targetId = data.getUUID(TARGET_TAG)
    }

    override fun save(entity: KryptonShulkerBullet): CompoundTag.Builder = ProjectileSerializer.save(entity).apply {
        putInt(STEPS_TAG, entity.steps)
        putDouble(DELTA_X_TAG, entity.targetDeltaX)
        putDouble(DELTA_Y_TAG, entity.targetDeltaY)
        putDouble(DELTA_Z_TAG, entity.targetDeltaZ)
        putNullable(DIR_TAG, entity.movingDirection) { name, value -> putInt(name, value.ordinal) }
        putNullable(TARGET_TAG, entity.targetId, CompoundTag.Builder::putUUID)
    }
}

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
package org.kryptonmc.krypton.entity.serializer.projectile

import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.entity.projectile.KryptonShulkerBullet
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.enumhelper.Directions
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
        entity.targetDelta = Vec3d(data.getDouble(DELTA_X_TAG), data.getDouble(DELTA_Y_TAG), data.getDouble(DELTA_Z_TAG))
        if (data.hasNumber(DIR_TAG)) entity.movingDirection = Directions.of3D(data.getInt(DIR_TAG))
        if (data.hasUUID(TARGET_TAG)) entity.setTargetId(data.getUUID(TARGET_TAG))
    }

    override fun save(entity: KryptonShulkerBullet): CompoundTag.Builder = ProjectileSerializer.save(entity).apply {
        putInt(STEPS_TAG, entity.steps)
        putDouble(DELTA_X_TAG, entity.targetDelta.x)
        putDouble(DELTA_Y_TAG, entity.targetDelta.y)
        putDouble(DELTA_Z_TAG, entity.targetDelta.z)
        putNullable(DIR_TAG, entity.movingDirection) { name, value -> putInt(name, value.ordinal) }
        putNullable(TARGET_TAG, entity.targetId(), CompoundTag.Builder::putUUID)
    }
}

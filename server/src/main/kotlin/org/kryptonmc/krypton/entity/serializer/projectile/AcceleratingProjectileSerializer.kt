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

import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.entity.projectile.KryptonAcceleratingProjectile
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.list

object AcceleratingProjectileSerializer : EntitySerializer<KryptonAcceleratingProjectile> {

    private val LOGGER = LogManager.getLogger()
    private const val POWER_TAG = "power"
    private const val POWER_SIZE = 3

    override fun load(entity: KryptonAcceleratingProjectile, data: CompoundTag) {
        ProjectileSerializer.load(entity, data)
        if (!data.contains(POWER_TAG, ListTag.ID)) return
        val power = data.getList(POWER_TAG, DoubleTag.ID)
        if (power.size() != POWER_SIZE) {
            LOGGER.warn("Found invalid accelerating projectile power array of length ${power.size()}! Expected length of $POWER_SIZE! Skipping...")
            return
        }
        entity.acceleration = Vec3d(power.getDouble(0), power.getDouble(1), power.getDouble(2))
    }

    override fun save(entity: KryptonAcceleratingProjectile): CompoundTag.Builder = ProjectileSerializer.save(entity).apply {
        list(POWER_TAG) {
            addDouble(entity.acceleration.x)
            addDouble(entity.acceleration.y)
            addDouble(entity.acceleration.z)
        }
    }
}

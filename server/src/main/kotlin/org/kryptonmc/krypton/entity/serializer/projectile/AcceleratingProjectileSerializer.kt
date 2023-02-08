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

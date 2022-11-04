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
import org.kryptonmc.krypton.util.nbt.hasUUID
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag

object ShulkerBulletSerializer : EntitySerializer<KryptonShulkerBullet> {

    override fun load(entity: KryptonShulkerBullet, data: CompoundTag) {
        ProjectileSerializer.load(entity, data)
        entity.steps = data.getInt("Steps")
        entity.targetDeltaX = data.getDouble("TXD")
        entity.targetDeltaY = data.getDouble("TYD")
        entity.targetDeltaZ = data.getDouble("TZD")
        if (data.contains("Dir", 99)) entity.movingDirection = Directions.of3D(data.getInt("Dir"))
        if (data.hasUUID("Target")) entity.targetId = data.getUUID("Target")
    }

    override fun save(entity: KryptonShulkerBullet): CompoundTag.Builder = ProjectileSerializer.save(entity).apply {
        putInt("Steps", entity.steps)
        putDouble("TXD", entity.targetDeltaX)
        putDouble("TYD", entity.targetDeltaY)
        putDouble("TZD", entity.targetDeltaZ)
        if (entity.target != null) putUUID("Target", entity.target!!.uuid)
        if (entity.movingDirection != null) putInt("Dir", entity.movingDirection!!.ordinal)
    }
}

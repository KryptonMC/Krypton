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
package org.kryptonmc.krypton.entity.projectile

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.projectile.ShulkerBullet
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.util.Directions
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import java.util.UUID

class KryptonShulkerBullet(world: KryptonWorld) : KryptonProjectile(world, EntityTypes.SHULKER_BULLET), ShulkerBullet {

    private var targetId: UUID? = null
    override var steps: Int = 0
    override var movingDirection: Direction? = null
    override var target: Entity? = null
    override var targetDeltaX: Double = 0.0
    override var targetDeltaY: Double = 0.0
    override var targetDeltaZ: Double = 0.0

    override fun load(tag: CompoundTag) {
        super.load(tag)
        steps = tag.getInt("Steps")
        targetDeltaX = tag.getDouble("TXD")
        targetDeltaY = tag.getDouble("TYD")
        targetDeltaZ = tag.getDouble("TZD")
        if (tag.contains("Dir", 99)) movingDirection = Directions.of3D(tag.getInt("Dir"))
        if (tag.hasUUID("Target")) targetId = tag.getUUID("Target")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        int("Steps", steps)
        double("TXD", targetDeltaX)
        double("TYD", targetDeltaY)
        double("TZD", targetDeltaZ)
        if (target != null) uuid("Target", target!!.uuid)
        if (movingDirection != null) int("Dir", movingDirection!!.ordinal)
    }
}

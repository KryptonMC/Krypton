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
package org.kryptonmc.krypton.entity.projectile

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.projectile.AcceleratingProjectile
import org.kryptonmc.api.space.Vector
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.ListTag

abstract class KryptonAcceleratingProjectile(world: KryptonWorld, type: EntityType<out AcceleratingProjectile>) : KryptonProjectile(world, type), AcceleratingProjectile {

    final override var acceleration = Vector.ZERO

    override fun load(tag: CompoundTag) {
        super.load(tag)
        if (tag.contains("power", ListTag.ID)) {
            val power = tag.getList("power", DoubleTag.ID)
            if (power.size == 3) {
                val x = power.getDouble(0)
                val y = power.getDouble(1)
                val z = power.getDouble(2)
                acceleration = Vector(x, y, z)
            }
        }
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        list("power", DoubleTag.ID, DoubleTag.of(acceleration.x), DoubleTag.of(acceleration.y), DoubleTag.of(acceleration.z))
    }
}

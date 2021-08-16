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

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.projectile.LargeFireball
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonLargeFireball(world: KryptonWorld) : KryptonFireball(world, EntityTypes.FIREBALL), LargeFireball {

    override var explosionPower = 1

    override fun load(tag: CompoundTag) {
        super.load(tag)
        if (tag.contains("ExplosionPower", 99)) explosionPower = tag.getByte("ExplosionPower").toInt()
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        byte("ExplosionPower", explosionPower.toByte())
    }
}

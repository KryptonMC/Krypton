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
package org.kryptonmc.krypton.entity.vehicle

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.vehicle.Boat
import org.kryptonmc.api.entity.vehicle.BoatType
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource

class KryptonBoat(world: KryptonWorld) : KryptonEntity(world, EntityTypes.BOAT), Boat {

    override var boatType: BoatType
        get() = TYPES.getOrNull(data[MetadataKeys.BOAT.TYPE]) ?: BoatType.OAK
        set(value) = data.set(MetadataKeys.BOAT.TYPE, value.ordinal)
    override var damageTaken: Float
        get() = data[MetadataKeys.BOAT.DAMAGE]
        set(value) = data.set(MetadataKeys.BOAT.DAMAGE, value)
    override var damageTimer: Int
        get() = data[MetadataKeys.BOAT.HURT_TIMER]
        set(value) = data.set(MetadataKeys.BOAT.HURT_TIMER, value)
    override var isLeftPaddleTurning: Boolean
        get() = data[MetadataKeys.BOAT.LEFT_PADDLE_TURNING]
        set(value) = data.set(MetadataKeys.BOAT.LEFT_PADDLE_TURNING, value)
    override var isRightPaddleTurning: Boolean
        get() = data[MetadataKeys.BOAT.RIGHT_PADDLE_TURNING]
        set(value) = data.set(MetadataKeys.BOAT.RIGHT_PADDLE_TURNING, value)
    private var damageDirection: Int
        get() = data[MetadataKeys.BOAT.HURT_DIRECTION]
        set(value) = data.set(MetadataKeys.BOAT.HURT_DIRECTION, value)

    init {
        data.add(MetadataKeys.BOAT.HURT_TIMER)
        data.add(MetadataKeys.BOAT.HURT_DIRECTION)
        data.add(MetadataKeys.BOAT.DAMAGE)
        data.add(MetadataKeys.BOAT.TYPE)
        data.add(MetadataKeys.BOAT.LEFT_PADDLE_TURNING)
        data.add(MetadataKeys.BOAT.RIGHT_PADDLE_TURNING)
        data.add(MetadataKeys.BOAT.SPLASH_TIMER)
    }

    override fun damage(source: KryptonDamageSource, damage: Float): Boolean {
        if (isInvulnerableTo(source)) return false
        if (isRemoved) return true
        damageDirection = -damageDirection
        damageTimer = 10
        damageTaken += damage * 10F
        markDamaged()
        return true
    }

    companion object {

        internal val TYPES = BoatType.values()
    }
}

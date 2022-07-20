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
import org.kryptonmc.api.entity.vehicle.BoatVariant
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource

class KryptonBoat(world: KryptonWorld) : KryptonEntity(world, EntityTypes.BOAT), Boat {

    override var variant: BoatVariant
        get() = TYPES.getOrNull(data.get(MetadataKeys.Boat.TYPE)) ?: BoatVariant.OAK
        set(value) = data.set(MetadataKeys.Boat.TYPE, value.ordinal)
    override var damageTaken: Float
        get() = data.get(MetadataKeys.Boat.DAMAGE)
        set(value) = data.set(MetadataKeys.Boat.DAMAGE, value)
    override var damageTimer: Int
        get() = data.get(MetadataKeys.Boat.HURT_TIMER)
        set(value) = data.set(MetadataKeys.Boat.HURT_TIMER, value)
    override var isLeftPaddleTurning: Boolean
        get() = data.get(MetadataKeys.Boat.LEFT_PADDLE_TURNING)
        set(value) = data.set(MetadataKeys.Boat.LEFT_PADDLE_TURNING, value)
    override var isRightPaddleTurning: Boolean
        get() = data.get(MetadataKeys.Boat.RIGHT_PADDLE_TURNING)
        set(value) = data.set(MetadataKeys.Boat.RIGHT_PADDLE_TURNING, value)
    private var damageDirection: Int
        get() = data.get(MetadataKeys.Boat.HURT_DIRECTION)
        set(value) = data.set(MetadataKeys.Boat.HURT_DIRECTION, value)

    init {
        data.add(MetadataKeys.Boat.HURT_TIMER, 0)
        data.add(MetadataKeys.Boat.HURT_DIRECTION, 1)
        data.add(MetadataKeys.Boat.DAMAGE, 0F)
        data.add(MetadataKeys.Boat.TYPE, BoatVariant.OAK.ordinal)
        data.add(MetadataKeys.Boat.LEFT_PADDLE_TURNING, false)
        data.add(MetadataKeys.Boat.RIGHT_PADDLE_TURNING, false)
        data.add(MetadataKeys.Boat.SPLASH_TIMER, 0)
    }

    override fun damage(source: KryptonDamageSource, damage: Float): Boolean {
        if (isInvulnerableTo(source)) return false
        if (isRemoved) return true
        damageDirection = -damageDirection
        damageTimer = DEFAULT_DAMAGE_TIMER
        damageTaken += damage * DAMAGE_INCREASE_MULTIPLIER
        markDamaged()
        return true
    }

    companion object {

        private const val DEFAULT_DAMAGE_TIMER = 10
        private const val DAMAGE_INCREASE_MULTIPLIER = 10F

        private val TYPES = BoatVariant.values()
    }
}

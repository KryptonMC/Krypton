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

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.projectile.ArrowLike
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld

abstract class KryptonArrowLike(
    world: KryptonWorld,
    type: EntityType<out ArrowLike>,
    defaultHitGroundSound: SoundEvent = SoundEvents.ARROW_HIT
) : KryptonProjectile(world, type), ArrowLike {

    final override var damage: Double = 2.0
    final override var isInGround: Boolean = false
    final override var life: Int = 0
    final override var shakeTime: Int = 0
    final override var sound: SoundEvent = defaultHitGroundSound
    final override var stuckInBlock: Block? = null
    final override var pickup: ArrowLike.Pickup = ArrowLike.Pickup.DISALLOWED

    final override var isCritical: Boolean
        get() = getFlag(0)
        set(value) = setFlag(0, value)
    final override var ignoresPhysics: Boolean
        get() = getFlag(1)
        set(value) = setFlag(1, value)
    final override var wasShotFromCrossbow: Boolean
        get() = getFlag(2)
        set(value) = setFlag(2, value)
    final override var piercingLevel: Int
        get() = data[MetadataKeys.ARROW_LIKE.PIERCING_LEVEL].toInt()
        set(value) = data.set(MetadataKeys.ARROW_LIKE.PIERCING_LEVEL, value.toByte())

    init {
        data.add(MetadataKeys.ARROW_LIKE.FLAGS, 0)
        data.add(MetadataKeys.ARROW_LIKE.PIERCING_LEVEL, 0)
    }

    private fun getFlag(flag: Int): Boolean = getFlag(MetadataKeys.ARROW_LIKE.FLAGS, flag)

    private fun setFlag(flag: Int, value: Boolean) {
        setFlag(MetadataKeys.ARROW_LIKE.FLAGS, flag, value)
    }
}

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

import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.projectile.ArrowLike
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.projectile.ArrowLikeSerializer
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.block.state.downcast

abstract class KryptonArrowLike(world: KryptonWorld) : KryptonProjectile(world), ArrowLike {

    override val serializer: EntitySerializer<out KryptonArrowLike>
        get() = ArrowLikeSerializer

    final override var baseDamage: Double = 2.0
    final override var isInGround: Boolean = false
    var life: Int = 0
    var shakeTime: Int = 0
    var sound: SoundEvent = defaultHitGroundSound
    final override var stuckInBlock: BlockState?
        get() = internalStuckInBlock
        set(value) {
            internalStuckInBlock = value?.downcast()
        }
    var internalStuckInBlock: KryptonBlockState? = null
    final override var pickupRule: ArrowLike.PickupRule = ArrowLike.PickupRule.DISALLOWED

    final override var isCritical: Boolean
        get() = data.getFlag(MetadataKeys.ArrowLike.FLAGS, FLAG_CRITICAL)
        set(value) = data.setFlag(MetadataKeys.ArrowLike.FLAGS, FLAG_CRITICAL, value)
    final override var ignoresPhysics: Boolean
        get() = data.getFlag(MetadataKeys.ArrowLike.FLAGS, FLAG_IGNORES_PHYSICS)
        set(value) = data.setFlag(MetadataKeys.ArrowLike.FLAGS, FLAG_IGNORES_PHYSICS, value)
    final override var wasShotFromCrossbow: Boolean
        get() = data.getFlag(MetadataKeys.ArrowLike.FLAGS, FLAG_WAS_SHOT_FROM_CROSSBOW)
        set(value) = data.setFlag(MetadataKeys.ArrowLike.FLAGS, FLAG_WAS_SHOT_FROM_CROSSBOW, value)
    final override var piercingLevel: Int
        get() = data.get(MetadataKeys.ArrowLike.PIERCING_LEVEL).toInt()
        set(value) = data.set(MetadataKeys.ArrowLike.PIERCING_LEVEL, value.toByte())

    internal open val defaultHitGroundSound: SoundEvent
        get() = SoundEvents.ARROW_HIT

    init {
        data.define(MetadataKeys.ArrowLike.FLAGS, 0)
        data.define(MetadataKeys.ArrowLike.PIERCING_LEVEL, 0)
    }

    companion object {

        private const val FLAG_CRITICAL = 0
        private const val FLAG_IGNORES_PHYSICS = 1
        private const val FLAG_WAS_SHOT_FROM_CROSSBOW = 2
    }
}

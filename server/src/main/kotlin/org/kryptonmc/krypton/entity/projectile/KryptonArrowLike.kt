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
    var sound: SoundEvent = defaultHitGroundSound()
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

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.ArrowLike.FLAGS, 0)
        data.define(MetadataKeys.ArrowLike.PIERCING_LEVEL, 0)
    }

    internal open fun defaultHitGroundSound(): SoundEvent = SoundEvents.ARROW_HIT.get()

    companion object {

        private const val FLAG_CRITICAL = 0
        private const val FLAG_IGNORES_PHYSICS = 1
        private const val FLAG_WAS_SHOT_FROM_CROSSBOW = 2
    }
}

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

import org.kryptonmc.api.entity.projectile.FishingHook
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKey
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonFishingHook(world: KryptonWorld) : KryptonProjectile(world), FishingHook {

    override val type: KryptonEntityType<KryptonFishingHook>
        get() = KryptonEntityTypes.FISHING_HOOK

    override var hooked: KryptonEntity? = null
        set(value) {
            field = value
            data.set(MetadataKeys.FishingHook.HOOKED, if (value != null) value.id + 1 else 0)
        }
    override var state: FishingHook.State = FishingHook.State.FLYING
    private var biting = false

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.FishingHook.HOOKED, 0)
        data.define(MetadataKeys.FishingHook.BITING, false)
    }

    override fun onDataUpdate(key: MetadataKey<*>) {
        if (key === MetadataKeys.FishingHook.HOOKED) {
            val id = data.get(MetadataKeys.FishingHook.HOOKED)
            hooked = if (id > 0) world.entityManager.getById(id - 1) else null
        }

        if (key === MetadataKeys.FishingHook.BITING) {
            biting = data.get(MetadataKeys.FishingHook.BITING)
            if (biting) velocity = randomizeBitingVelocity(random, velocity)
        }

        super.onDataUpdate(key)
    }

    override fun isBiting(): Boolean = biting

    companion object {

        private const val BITING_VELOCITY_RANDOMNESS_MIN = 0.6F
        private const val BITING_VELOCITY_MULTIPLIER = -0.4F

        @JvmStatic
        private fun randomizeBitingVelocity(random: RandomSource, existing: Vec3d): Vec3d {
            // This always comes out to be a number between -0.4 and -0.24. These numbers are from vanilla.
            val randomY = BITING_VELOCITY_MULTIPLIER * Maths.nextFloat(random, BITING_VELOCITY_RANDOMNESS_MIN, 1F)
            return Vec3d(existing.x, randomY.toDouble(), existing.z)
        }
    }
}

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

import org.kryptonmc.api.entity.projectile.FishingHook
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKey
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.util.Vec3dImpl
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
    override var isBiting: Boolean = false

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.FishingHook.HOOKED, 0)
        data.define(MetadataKeys.FishingHook.BITING, false)
    }

    override fun onDataUpdate(key: MetadataKey<*>) {
        if (key === MetadataKeys.FishingHook.HOOKED) {
            val id = data.get(MetadataKeys.FishingHook.HOOKED)
            hooked = if (id > 0) world.entityManager.get(id - 1) else null
        }

        if (key === MetadataKeys.FishingHook.BITING) {
            isBiting = data.get(MetadataKeys.FishingHook.BITING)
            if (isBiting) velocity = randomizeBitingVelocity(random, velocity)
        }

        super.onDataUpdate(key)
    }

    companion object {

        private const val BITING_VELOCITY_RANDOMNESS_MIN = 0.6F
        private const val BITING_VELOCITY_MULTIPLIER = -0.4F

        @JvmStatic
        private fun randomizeBitingVelocity(random: RandomSource, existing: Vec3d): Vec3d {
            // This always comes out to be a number between -0.4 and -0.24. These numbers are from vanilla.
            val randomY = BITING_VELOCITY_MULTIPLIER * Maths.nextFloat(random, BITING_VELOCITY_RANDOMNESS_MIN, 1F)
            return Vec3dImpl(existing.x, randomY.toDouble(), existing.z)
        }
    }
}

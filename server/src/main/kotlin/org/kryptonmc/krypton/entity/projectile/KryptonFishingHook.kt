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

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.projectile.FishingHook
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKey
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.util.nextFloatClamped
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3d
import kotlin.random.Random

class KryptonFishingHook(world: KryptonWorld) : KryptonProjectile(world, EntityTypes.FISHING_HOOK), FishingHook {

    override var hooked: KryptonEntity? = null
        set(value) {
            field = value
            data.set(MetadataKeys.FishingHook.HOOKED, if (value != null) value.id + 1 else 0)
        }
    override var state: FishingHook.State = FishingHook.State.FLYING
    override var isBiting: Boolean = false

    init {
        data.add(MetadataKeys.FishingHook.HOOKED, 0)
        data.add(MetadataKeys.FishingHook.BITING, false)
    }

    override fun onDataUpdate(key: MetadataKey<*>) {
        if (key === MetadataKeys.FishingHook.HOOKED) {
            val id = data.get(MetadataKeys.FishingHook.HOOKED)
            hooked = if (id > 0) world.entityManager[id - 1] else null
        }

        if (key === MetadataKeys.FishingHook.BITING) {
            isBiting = data.get(MetadataKeys.FishingHook.BITING)
            if (isBiting) velocity = Vector3d(velocity.x(), (-0.4F * Random.nextFloatClamped(0.6F, 1F)).toDouble(), velocity.z())
        }

        super.onDataUpdate(key)
    }
}

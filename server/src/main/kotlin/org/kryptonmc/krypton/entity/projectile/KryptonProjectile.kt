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

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.projectile.Projectile
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.UUID

abstract class KryptonProjectile(world: KryptonWorld, type: EntityType<out Projectile>) : KryptonEntity(world, type), Projectile {

    internal var ownerId: UUID? = null
    final override var owner: Entity? = null
        get() {
            if (field != null) return field
            if (ownerId != null) {
                field = world.entityManager.get(ownerId!!)
                return field
            }
            return null
        }
        set(value) {
            if (value == null) return
            ownerId = value.uuid
            field = value
        }
    final override var hasLeftOwner: Boolean = false
    final override var hasBeenShot: Boolean = false
}

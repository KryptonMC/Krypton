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
import org.kryptonmc.api.entity.projectile.Projectile
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.projectile.ProjectileSerializer
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.UUID

abstract class KryptonProjectile(world: KryptonWorld) : KryptonEntity(world), Projectile {

    override val serializer: EntitySerializer<out KryptonProjectile>
        get() = ProjectileSerializer

    private var ownerId: UUID? = null
    final override var owner: Entity? = null
        get() {
            if (field != null) return field
            if (ownerId != null) {
                field = world.entityManager.getByUUID(ownerId!!)
                return field
            }
            return null
        }
        set(value) {
            if (value == null) return
            ownerId = value.uuid
            field = value
        }
    private var leftOwner = false
    private var beenShot = false

    fun ownerId(): UUID? = ownerId

    fun setOwnerId(id: UUID) {
        ownerId = id
    }

    override fun hasLeftOwner(): Boolean = leftOwner

    fun setLeftOwner(left: Boolean) {
        leftOwner = left
    }

    override fun hasBeenShot(): Boolean = beenShot

    fun setBeenShot(shot: Boolean) {
        beenShot = shot
    }
}

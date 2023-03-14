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

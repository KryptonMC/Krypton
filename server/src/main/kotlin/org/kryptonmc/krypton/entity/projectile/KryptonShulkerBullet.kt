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
import org.kryptonmc.api.entity.projectile.ShulkerBullet
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.projectile.ShulkerBulletSerializer
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.UUID

class KryptonShulkerBullet(world: KryptonWorld) : KryptonProjectile(world), ShulkerBullet {

    override val type: KryptonEntityType<KryptonShulkerBullet>
        get() = KryptonEntityTypes.SHULKER_BULLET
    override val serializer: EntitySerializer<KryptonShulkerBullet>
        get() = ShulkerBulletSerializer

    private var targetId: UUID? = null
    override var steps: Int = 0
    override var target: Entity? = null
    override var targetDelta: Vec3d = Vec3d.ZERO
    override var movingDirection: Direction? = null

    fun targetId(): UUID? = targetId

    fun setTargetId(id: UUID) {
        targetId = id
    }
}

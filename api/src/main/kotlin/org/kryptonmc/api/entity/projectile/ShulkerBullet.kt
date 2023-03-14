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
package org.kryptonmc.api.entity.projectile

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3d

/**
 * A bullet fired from a [Shulker].
 */
public interface ShulkerBullet : Projectile {

    /**
     * How many steps the bullet will take to attack the target.
     *
     * The higher this value is, the further out of the way the bullet travels
     * to reach the target.
     * If this value is 0, the bullet makes no attempt to attack the target,
     * and instead follows the target delta X, Y, and Z values in a straight
     * line.
     */
    public val steps: Int

    /**
     * The target of this bullet, or null if this bullet does not have a target
     * established.
     */
    public val target: Entity?

    /**
     * The offset of the target from the location of this bullet.
     */
    public val targetDelta: Vec3d

    /**
     * The current direction that this bullet is moving, or null if this bullet
     * is not moving.
     */
    public val movingDirection: Direction?
}

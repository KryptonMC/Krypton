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
package org.kryptonmc.api.entity.ai.pathfinding

import org.kryptonmc.api.entity.Mob
import org.kryptonmc.api.util.Position
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.util.Vec3i

/**
 * A navigator for pathfinding an entity to a target.
 */
public interface Navigator {

    /**
     * The entity that is navigating with this navigator.
     */
    public val entity: Mob

    /**
     * The current target position.
     */
    public val target: Vec3d?

    /**
     * Whether the pathfinding entity has reached its target.
     *
     * @return true if the entity has reached the target
     */
    public fun hasReachedTarget(): Boolean

    /**
     * Requests that the entity be moved towards the given [x], [y],
     * and [z] coordinates.
     *
     * This may happen immediately, if the current target is the same as or
     * very close to the coordinates. It may also take a while to reach the
     * target, if it is far away.
     *
     * @param x the X coordinate to move to
     * @param y the Y coordinate to move to
     * @param z the Z coordinate to move to
     */
    public fun pathTo(x: Double, y: Double, z: Double)

    /**
     * Requests that the entity be moved towards the given [position].
     *
     * This may happen immediately, if the current target is the same as or
     * very close to the position. It may also take a while to reach the
     * target, if it is far away.
     *
     * @param position the position to move to
     */
    public fun pathTo(position: Vec3d) {
        pathTo(position.x, position.y, position.z)
    }

    /**
     * Requests that the entity be moved towards the given [position].
     *
     * This may happen immediately, if the current target is the same as or
     * very close to the position. It may also take a while to reach the
     * target, if it is far away.
     *
     * @param position the position to move to
     */
    public fun pathTo(position: Vec3i) {
        pathTo(position.x.toDouble(), position.y.toDouble(), position.z.toDouble())
    }

    /**
     * Requests that the entity be moved towards the given [position].
     *
     * This may happen immediately, if the current target is the same as or
     * very close to the position. It may also take a while to reach the
     * target, if it is far away.
     *
     * @param position the position to move to
     */
    public fun pathTo(position: Position) {
        pathTo(position.x, position.y, position.z)
    }
}

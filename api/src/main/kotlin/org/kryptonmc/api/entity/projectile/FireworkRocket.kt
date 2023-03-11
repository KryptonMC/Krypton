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
import org.kryptonmc.api.item.ItemStackLike

/**
 * A fired firework rocket.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface FireworkRocket : Projectile, ItemStackLike {

    /**
     * The number of ticks this rocket has been flying for.
     */
    public var life: Int

    /**
     * The number of ticks until this rocket explodes.
     *
     * This value is randomized when the rocket is launched, using the
     * following algorithm:
     * ```
     * (flight + 1) * 10 + random(0 to 5) + random(0 to 6)
     * ```
     */
    public var lifetime: Int

    /**
     * If this rocket was shot at an angle, which occurs when shot from a
     * crossbow or dispenser.
     */
    @get:JvmName("wasShotAtAngle")
    public var wasShotAtAngle: Boolean

    /**
     * The entity that is attached to this rocket, or null if this rocket is
     * not attached to an entity.
     *
     * This is used for elytra boosting.
     */
    public val attachedEntity: Entity?
}

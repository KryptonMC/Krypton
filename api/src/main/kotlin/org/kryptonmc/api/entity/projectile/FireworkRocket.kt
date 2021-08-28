/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.projectile

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.item.ItemSupplier

/**
 * A fired firework rocket.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface FireworkRocket : Projectile, ItemSupplier {

    /**
     * The number of ticks this rocket has been flying for.
     */
    public var life: Int

    /**
     * The number of ticks until this rocket explodes.
     *
     * This value is randomized when the rocket is launched, using the following algorithm:
     * ```
     * (flight + 1) * 10 + random(0 to 5) + random(0 to 6)
     * ```
     */
    public var lifetime: Int

    /**
     * If this rocket was shot at an angle, which occurs when shot from a crossbow
     * or dispenser.
     */
    @get:JvmName("wasShotAtAngle")
    public var wasShotAtAngle: Boolean

    /**
     * The entity that is attached to this rocket, or null if this rocket is not attached
     * to an entity.
     *
     * This is used for elytra boosting.
     */
    public val attachedEntity: Entity?
}

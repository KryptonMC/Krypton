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

/**
 * A projectile.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Projectile : Entity {

    /**
     * The owner of this projectile, or null if this projectile does not have an owner
     * yet.
     */
    public val owner: Entity?

    /**
     * If this projectile has left its owner's hitbox.
     */
    @get:JvmName("hasLeftOwner")
    public val hasLeftOwner: Boolean

    /**
     * If this projectile has been shot.
     */
    @get:JvmName("hasBeenShot")
    public val hasBeenShot: Boolean
}

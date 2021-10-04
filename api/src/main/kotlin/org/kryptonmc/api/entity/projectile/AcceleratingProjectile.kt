/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.projectile

import org.spongepowered.math.vector.Vector3d

/**
 * A projectile that will accelerate at constant velocity until it hits
 * something, when it will explode.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface AcceleratingProjectile : Projectile {

    /**
     * The acceleration values of this projectile.
     */
    @get:JvmName("acceleration")
    public val acceleration: Vector3d
}

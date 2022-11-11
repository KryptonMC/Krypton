/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.projectile

import org.kryptonmc.api.util.Vec3d

/**
 * A projectile that will accelerate at constant velocity until it hits
 * something, when it will explode.
 */
public interface AcceleratingProjectile : Projectile {

    /**
     * The acceleration values of this projectile.
     */
    public val acceleration: Vec3d
}

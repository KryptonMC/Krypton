/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.projectile

/**
 * A large fireball.
 */
public interface LargeFireball : Fireball {

    /**
     * The power of the explosion that will be produced by this large fireball.
     */
    public val explosionPower: Int
}

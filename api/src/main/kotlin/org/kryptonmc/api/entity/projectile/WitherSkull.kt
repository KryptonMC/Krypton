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
 * A skull launched from a [Wither].
 */
interface WitherSkull : AcceleratingProjectile {

    /**
     * If this skull is dangerous (invulnerable).
     */
    var isDangerous: Boolean
}

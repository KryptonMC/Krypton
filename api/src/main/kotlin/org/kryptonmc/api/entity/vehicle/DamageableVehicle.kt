/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.vehicle

/**
 * A vehicle that can be damaged by a player hitting it, causing it to
 * eventually break when it takes enough damage.
 */
public interface DamageableVehicle : Vehicle {

    /**
     * The amount of damage that this damageable vehicle has taken.
     */
    public var damageTaken: Float

    /**
     * The time since this damageable vehicle last took damage.
     */
    public var damageTimer: Int
}

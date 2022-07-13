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
 * A vehicle that can be damaged by a player.
 *
 * When a player, for example, punches a boat or minecart like object, it will
 * take damage, and show increasingly violent rocking animations until it
 * breaks.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface DamageableVehicle : Vehicle {

    /**
     * The damage that this damageable vehicle has taken.
     */
    @get:JvmName("damageTaken")
    public var damageTaken: Float

    /**
     * The time, in ticks, until the damage taken is reset to 0.
     */
    @get:JvmName("damageTimer")
    public var damageTimer: Int
}

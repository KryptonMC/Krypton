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
 * A boat.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Boat : Vehicle {

    /**
     * The type of the boat.
     */
    @get:JvmName("boatType")
    public var boatType: BoatType

    /**
     * The amount of damage that this boat has taken.
     */
    @get:JvmName("damageTaken")
    public var damageTaken: Float

    /**
     * The time since this boat last took damage.
     */
    @get:JvmName("damageTimer")
    public var damageTimer: Int

    /**
     * If the left paddle of this boat is turning, thus making the boat turn
     * left.
     */
    public var isLeftPaddleTurning: Boolean

    /**
     * If the right paddle of this boat is turning, thus making the boat turn
     * right.
     */
    public var isRightPaddleTurning: Boolean
}

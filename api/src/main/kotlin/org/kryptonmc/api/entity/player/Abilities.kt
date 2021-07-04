/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.player

/**
 * Represents a [Player]'s abilities.
 *
 * @param isInvulnerable if this [Player] can take damage
 * @param canFly if this [Player] can fly
 * @param isFlying if this [Player] is currently flying
 * @param canBuild if this [Player] can build (place/break blocks)
 * @param canInstantlyBuild if this [Player] can place and break blocks with no delay (creative mode)
 * @param walkSpeed the speed at which this [Player] can walk
 * @param flyingSpeed the speed at which this [Player] can fly
 */
data class Abilities @JvmOverloads constructor(
    var isInvulnerable: Boolean = false,
    @get:JvmName("canFly") var canFly: Boolean = false,
    var isFlying: Boolean = false,
    @get:JvmName("canBuild") var canBuild: Boolean = true,
    @get:JvmName("canInstantlyBuild") var canInstantlyBuild: Boolean = false,
    var walkSpeed: Float = DEFAULT_WALKING_SPEED,
    var flyingSpeed: Float = DEFAULT_FLYING_SPEED
) {

    companion object {

        /**
         * The default walking speed for players
         */
        const val DEFAULT_WALKING_SPEED = 0.1F

        /**
         * The default flying speed for players.
         */
        const val DEFAULT_FLYING_SPEED = 0.05F
    }
}

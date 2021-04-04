package org.kryptonmc.krypton.api.entity

import org.kryptonmc.krypton.api.entity.entities.Player

/**
 * Represents a [Player]'s abilities.
 *
 * @param isInvulnerable if this [Player] does not take damage
 * @param canFly if this [Player] can fly
 * @param isFlying if this [Player] is currently flying
 * @param canBuild if this [Player] can build (place/break blocks)
 * @param canInstantlyBuild if this [Player] is in creative mode
 */
data class Abilities(
    var isInvulnerable: Boolean = false,
    var canFly: Boolean = false,
    var isFlying: Boolean = false,
    var canBuild: Boolean = true,
    var canInstantlyBuild: Boolean = false,
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
        const val DEFAULT_FLYING_SPEED = 0.05f
    }
}
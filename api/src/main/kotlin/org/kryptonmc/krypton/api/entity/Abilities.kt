package org.kryptonmc.krypton.api.entity

import org.kryptonmc.krypton.api.entity.entities.Player

/**
 * Represents a [Player]'s abilities.
 *
 * [isInvulnerable] - if this [Player] does not take damage
 * [canFly] - if this [Player] can fly
 * [isFlyingAllowed] - if this [Player] is allowed to fly
 * [isCreativeMode] if this [Player] is in creative mode
 */
data class Abilities(
    val isInvulnerable: Boolean = false,
    val canFly: Boolean = false,
    val isFlyingAllowed: Boolean = false,
    val isCreativeMode: Boolean = false,
    val flyingSpeed: Float = DEFAULT_FLYING_SPEED,
    val fieldOfViewModifier: Float = DEFAULT_FIELD_OF_VIEW_MODIFIER
) {

    companion object {

        /**
         * The default flying speed for players.
         */
        const val DEFAULT_FLYING_SPEED = 0.05f

        /**
         * The default field of view modifier for players.
         */
        const val DEFAULT_FIELD_OF_VIEW_MODIFIER = 0.1f
    }
}
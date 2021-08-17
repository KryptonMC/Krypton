package org.kryptonmc.api.entity

import org.kryptonmc.api.entity.player.Player

/**
 * An orb of experience.
 */
interface ExperienceOrb : Entity {

    /**
     * The amount of ticks this orb has not been touched for.
     */
    val age: Int

    /**
     * The remaining amount of times this orb can be picked up.
     *
     * When this orb is picked up, this value will decrease by 1.
     * When multiple orbs are merged, their counts will be summed.
     * When this value reaches 0, the orb is depleted.
     */
    val count: Int

    /**
     * The current health of the orb.
     *
     * Experience orbs can take damage from fire, lava, falling anvils, and explosions.
     * The orb is destroyed when this value reaches 0.
     */
    val health: Int

    /**
     * The amount of experience given by this orb when it is picked up.
     */
    val value: Int

    /**
     * The player this orb is currently following, or null if this orb is not currently
     * following a player.
     */
    val following: Player?
}

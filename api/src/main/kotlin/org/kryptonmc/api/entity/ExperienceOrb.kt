/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import org.kryptonmc.api.entity.player.Player

/**
 * An orb of experience.
 */
public interface ExperienceOrb : Entity {

    /**
     * The remaining amount of times this orb can be picked up.
     *
     * When this orb is picked up, this value will decrease by 1.
     * When multiple orbs are merged, their counts will be summed.
     * When this value reaches 0, the orb is depleted.
     */
    public val count: Int

    /**
     * The current health of the orb.
     *
     * Experience orbs can take damage from fire, lava, falling anvils, and
     * explosions.
     * The orb is destroyed when this value reaches 0.
     */
    public val health: Int

    /**
     * The amount of experience given by this orb when it is picked up.
     */
    public val experience: Int

    /**
     * The player this orb is currently following, or null if this orb is not
     * currently following a player.
     */
    public val following: Player?
}

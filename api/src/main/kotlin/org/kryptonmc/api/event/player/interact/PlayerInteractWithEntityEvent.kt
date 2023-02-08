/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player.interact

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.Hand

/**
 * Called when a player interacts with an entity as a whole.
 *
 * This is used for interactions where it only matters that an entity was
 * interacted with, not where on the entity the interaction occurred.
 */
public interface PlayerInteractWithEntityEvent : PlayerInteractEvent {

    /**
     * The entity that was interacted with.
     */
    public val target: Entity

    /**
     * The hand that the player used to interact with the target.
     */
    public val hand: Hand
}

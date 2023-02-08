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
import org.kryptonmc.api.util.Vec3d

/**
 * Called when a player interacts with a specific part of an entity.
 *
 * This is used for interactions that require knowledge of which part of an
 * entity was clicked, such as placing armour on to an armour stand.
 */
public interface PlayerInteractAtEntityEvent : PlayerInteractEvent {

    /**
     * The entity that was interacted with.
     */
    public val target: Entity

    /**
     * The hand the player used to interact with the target.
     */
    public val hand: Hand

    /**
     * The position that the player clicked on the entity.
     */
    public val clickedPosition: Vec3d
}

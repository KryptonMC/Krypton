/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player

/**
 * Called when a player interacts on an entity.
 *
 * @param target the entity that was interacted on
 * @param hand the hand the player used to interact with the target
 */
public class InteractEntityEvent(
    player: Player,
    public val target: Entity,
    public val hand: Hand
) : InteractEvent(player, Type.INTERACT_ON_ENTITY)

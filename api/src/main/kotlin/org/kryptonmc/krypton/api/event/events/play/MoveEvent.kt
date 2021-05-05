/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api.event.events.play

import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.event.Event
import org.kryptonmc.krypton.api.world.Location

/**
 * Called when a player moves.
 *
 * @param player the player who moved
 * @param oldLocation the old location of the player
 * @param newLocation the new location of the player
 */
data class MoveEvent(
    val player: Player,
    val oldLocation: Location,
    val newLocation: Location
) : Event

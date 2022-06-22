/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.entity.player.Player
import org.spongepowered.math.vector.Vector3d

/**
 * Called when a player moves.
 *
 * @param player the player who moved
 * @param oldLocation the old location of the player
 * @param newLocation the new location of the player
 */
@JvmRecord
public data class MoveEvent(public val player: Player, public val oldLocation: Vector3d, public val newLocation: Vector3d)

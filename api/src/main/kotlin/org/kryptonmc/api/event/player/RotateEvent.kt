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
import org.spongepowered.math.vector.Vector2f

/**
 * Called when a player rotates their head.
 *
 * @param player the player who rotated their head
 * @param oldRotation the old rotation of the player
 * @param newRotation the new rotation of the player
 */
@JvmRecord
public data class RotateEvent(
    public val player: Player,
    public val oldRotation: Vector2f,
    public val newRotation: Vector2f
)

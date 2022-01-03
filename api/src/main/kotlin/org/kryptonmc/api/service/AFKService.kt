/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.service

import org.kryptonmc.api.entity.player.Player

/**
 * A service for toggling the AFK status of a player.
 */
public interface AFKService {

    /**
     * Checks whether the given [player] is currently AFK.
     *
     * @param player the player
     * @return true if the player is currently afk, false otherwise
     */
    public fun isAfk(player: Player): Boolean

    /**
     * Sets whether the given [player] is currently AFK to the given [afk]
     * status.
     *
     * @param player the player
     * @param afk the new AFK status of the player
     */
    public fun setAfk(player: Player, afk: Boolean)
}

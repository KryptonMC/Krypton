/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.player

/**
 * A player that can be vanished.
 */
public interface VanishingPlayer {

    /**
     * Hides this player from all other players on the server.
     */
    public fun vanish()

    /**
     * Shows this player to all other players on the server.
     */
    public fun unvanish()

    /**
     * Shows the given [player] to this player if they are hidden, or does
     * nothing if they aren't.
     *
     * @param player the player to show
     */
    public fun show(player: Player)

    /**
     * Hides the given [player] from this player if they are shown, or does
     * nothing if they aren't.
     *
     * @param player the player to hide
     */
    public fun hide(player: Player)

    /**
     * Checks if this player can see the given other [player].
     *
     * @param player the player to check
     * @return true if this player can see the other player, false otherwise
     */
    public fun canSee(player: Player): Boolean
}

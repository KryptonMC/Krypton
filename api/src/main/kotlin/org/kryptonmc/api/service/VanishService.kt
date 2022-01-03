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
 * A service for showing and hiding players from each other.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface VanishService {

    /**
     * Checks if the given [player] is currently vanished on the server.
     *
     * @param player the player
     * @return true if the player is vanished, false otherwise
     */
    public fun isVanished(player: Player): Boolean

    /**
     * Hides the given [player] from all other players on the server.
     *
     * @param player the player
     */
    public fun vanish(player: Player)

    /**
     * Shows the given [player] to all other players on the server.
     *
     * @param player the player
     */
    public fun unvanish(player: Player)

    /**
     * Shows the given [player] to the given [target] if they are hidden, or
     * does nothing if they are not.
     *
     * @param player the player
     * @param target the target
     */
    public fun show(player: Player, target: Player)

    /**
     * Hides the given [player] from the given [target] if they are hidden, or
     * does nothing if they are not.
     *
     * @param player the player
     * @param target the target
     */
    public fun hide(player: Player, target: Player)

    /**
     * Checks if the given [player] can see the given [target].
     *
     * @param player the player
     * @param target the target
     * @return true if the player can see the target, false otherwise
     */
    public fun canSee(player: Player, target: Player): Boolean
}

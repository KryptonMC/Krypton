/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.event.type.DeniableEventWithResult
import org.kryptonmc.api.event.type.PlayerEvent
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Called when the given [player] changes game mode.
 */
public interface PlayerChangeGameModeEvent : PlayerEvent, DeniableEventWithResult<PlayerChangeGameModeEvent.Result> {

    /**
     * The game mode that the player was in before the change.
     */
    public val oldGameMode: GameMode

    /**
     * The game mode that the player will be in after the change.
     */
    public val newGameMode: GameMode

    /**
     * The cause of the game mode being changed for the player.
     */
    public val cause: Cause

    /**
     * The cause of the game mode change.
     */
    public enum class Cause {

        /**
         * The game mode was changed through the API, by using
         * [org.kryptonmc.api.entity.player.Player.gameMode].
         */
        API,

        /**
         * The game mode was changed by a command, such as `/gamemode`.
         */
        COMMAND,

        /**
         * The game mode was loaded from the player's data.
         */
        LOAD
    }

    /**
     * The result of an attempt to change a player's game mode.
     *
     * This allows plugins to completely overwrite the game mode that the
     * player is switching to. For example, this could be used to force players
     * that attempt to switch to creative mode in to survival.
     *
     * @property newGameMode The game mode to change the player to.
     */
    @JvmRecord
    @ImmutableType
    public data class Result(public val newGameMode: GameMode)
}

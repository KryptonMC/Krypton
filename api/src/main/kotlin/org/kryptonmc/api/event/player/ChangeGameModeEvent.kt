/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.ResultedEvent
import org.kryptonmc.api.world.GameMode

/**
 * Called when the given [player] changes game mode.
 *
 * @param player the player who's game mode is changing
 * @param oldGameMode the old game mode of the player
 * @param newGameMode the new game mode of the player
 * @param cause the cause of the game mode change
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public data class ChangeGameModeEvent(
    @get:JvmName("player") public val player: Player,
    @get:JvmName("oldGameMode") public val oldGameMode: GameMode,
    @get:JvmName("newGameMode") public val newGameMode: GameMode,
    @get:JvmName("cause") public val cause: Cause
) : ResultedEvent<GameModeResult> {

    @get:JvmName("result")
    override var result: GameModeResult = GameModeResult.allowed()

    /**
     * The cause of the game mode change.
     */
    public enum class Cause {

        /**
         * The game mode was changed through the API, by using [Player.gameMode].
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
}

/**
 * The result of a [ChangeGameModeEvent].
 *
 * @param newGameMode the new game mode to change to
 */
@JvmRecord
public data class GameModeResult(
    override val isAllowed: Boolean,
    public val newGameMode: GameMode?
) : ResultedEvent.Result {

    public companion object {

        private val ALLOWED = GameModeResult(true, null)
        private val DENIED = GameModeResult(false, null)

        /**
         * Gets the result that allows the game mode change to continue as
         * normal, using the original old and new game mode values.
         *
         * @return the allowed result
         */
        @JvmStatic
        @Contract(pure = true)
        public fun allowed(): GameModeResult = ALLOWED

        /**
         * Creates a new result that allows the game mode change to continue,
         * but silently replaces the new game mode with the given
         * [newGameMode].
         *
         * @param newGameMode the new game mode for the player
         * @return a new allowed result
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun allowed(newGameMode: GameMode): GameModeResult = GameModeResult(true, newGameMode)

        /**
         * Gets the result that denies the game mode change from continuing.
         *
         * @return the denied result
         */
        @JvmStatic
        @Contract(pure = true)
        public fun denied(): GameModeResult = DENIED
    }
}

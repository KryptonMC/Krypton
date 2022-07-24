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
import org.kryptonmc.api.event.ResultedEvent
import org.kryptonmc.api.world.GameMode

/**
 * Called when the given [player] changes game mode.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ChangeGameModeEvent : PlayerEvent, ResultedEvent<ChangeGameModeEvent.Result> {

    /**
     * The game mode that the player was in before the change.
     */
    @get:JvmName("oldGameMode")
    public val oldGameMode: GameMode

    /**
     * The game mode that the player will be in after the change.
     */
    @get:JvmName("newGameMode")
    public val newGameMode: GameMode

    /**
     * The cause of the game mode being changed for the player.
     */
    @get:JvmName("cause")
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
     * @param newGameMode the new game mode to change to
     */
    @JvmRecord
    public data class Result(override val isAllowed: Boolean, public val newGameMode: GameMode?) : ResultedEvent.Result {

        public companion object {

            private val ALLOWED = Result(true, null)
            private val DENIED = Result(false, null)

            /**
             * Gets the result that allows the game mode change to continue as
             * normal, using the original old and new game mode values.
             *
             * @return the allowed result
             */
            @JvmStatic
            @Contract(pure = true)
            public fun allowed(): Result = ALLOWED

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
            public fun allowed(newGameMode: GameMode): Result = Result(true, newGameMode)

            /**
             * Gets the result that denies the game mode change from continuing.
             *
             * @return the denied result
             */
            @JvmStatic
            @Contract(pure = true)
            public fun denied(): Result = DENIED
        }
    }
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import net.kyori.adventure.text.Component
import org.kryptonmc.api.event.ResultedEvent

/**
 * Called when a player logs in and a player object has been constructed for
 * them.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface JoinEvent : PlayerEvent, ResultedEvent<JoinEvent.Result> {

    /**
     * If the player has joined the server before.
     */
    @get:JvmName("hasJoinedBefore")
    public val hasJoinedBefore: Boolean

    /**
     * The result of a [JoinEvent].
     *
     * @param message the join message
     * @param hasJoinedBefore if the player joining has joined before
     */
    @JvmRecord
    public data class Result(
        override val isAllowed: Boolean,
        public val message: Component?,
        public val hasJoinedBefore: Boolean
    ) : ResultedEvent.Result {

        public companion object {

            private val ALLOWED_NOT_JOINED_BEFORE = Result(true, null, false)
            private val ALLOWED_JOINED_BEFORE = Result(true, null, true)
            private val DENIED_NOT_JOINED_BEFORE = Result(false, null, false)
            private val DENIED_JOINED_BEFORE = Result(false, null, true)

            /**
             * Creates a new join result that allows the player to join, optionally
             * specifying if the player [has joined before][hasJoinedBefore],
             * defaulting to false if not specified.
             *
             * @param hasJoinedBefore if the player has joined before
             * @return an allowed join result
             */
            @JvmStatic
            @JvmOverloads
            public fun allowed(hasJoinedBefore: Boolean = false): Result {
                if (hasJoinedBefore) return ALLOWED_JOINED_BEFORE
                return ALLOWED_NOT_JOINED_BEFORE
            }

            /**
             * Creates a new join result that allows the player to join with the
             * given join [message], optionally specifying if the player
             * [has joined before][hasJoinedBefore], defaulting to false if not
             * specified.
             *
             * @param message the join message
             * @param hasJoinedBefore if the player has joined before
             * @return a new allowed join result
             */
            @JvmStatic
            @JvmOverloads
            public fun allowed(message: Component, hasJoinedBefore: Boolean = false): Result = Result(true, message, hasJoinedBefore)

            /**
             * Creates a new join result that denies the player from joining,
             * optionally specifying if the player
             * [has joined before][hasJoinedBefore], defaulting to false if not
             * specified.
             *
             * @param hasJoinedBefore if the player has joined before
             * @return a denied join result
             */
            @JvmStatic
            @JvmOverloads
            public fun denied(hasJoinedBefore: Boolean = false): Result {
                if (hasJoinedBefore) return DENIED_JOINED_BEFORE
                return DENIED_NOT_JOINED_BEFORE
            }

            /**
             * Creates a new join result that denies the player from joining with
             * the given join [message], optionally specifying if the player
             * [has joined before][hasJoinedBefore], defaulting to false if not
             * specified.
             *
             * @param message the join message
             * @param hasJoinedBefore if the player has joined before
             * @return a new denied join result
             */
            @JvmStatic
            @JvmOverloads
            public fun denied(message: Component, hasJoinedBefore: Boolean = false): Result = Result(false, message, hasJoinedBefore)
        }
    }
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import net.kyori.adventure.text.Component
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.ResultedEvent
import org.kryptonmc.api.event.ResultedEvent.Result

/**
 * Called when a player logs in and a player object has been
 * constructed for them (just before the state is switched to PLAY).
 *
 * @param player the player who joined
 * @param hasJoinedBefore if this player has joined before
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public data class JoinEvent(
    @get:JvmName("player") public val player: Player,
    @get:JvmName("hasChangedName") public val hasJoinedBefore: Boolean
) : ResultedEvent<JoinResult> {

    @get:JvmName("result")
    override var result: JoinResult = JoinResult.allowed(hasJoinedBefore)
}

/**
 * The result of a [JoinEvent].
 *
 * @param message the join message
 * @param hasJoinedBefore if the player joining has joined before
 */
@JvmRecord
public data class JoinResult(
    override val isAllowed: Boolean,
    public val message: Component?,
    public val hasJoinedBefore: Boolean
) : Result {

    public companion object {

        private val ALLOWED_NOT_JOINED_BEFORE = JoinResult(true, null, false)
        private val ALLOWED_JOINED_BEFORE = JoinResult(true, null, true)
        private val DENIED_NOT_JOINED_BEFORE = JoinResult(false, null, false)
        private val DENIED_JOINED_BEFORE = JoinResult(false, null, true)

        /**
         * Creates a new join result that allows the player to join, optionally
         * specifying if the player [has joined before][hasJoinedBefore],
         * defaulting to false if not specified.
         *
         * @param hasJoinedBefore if the player has joined before
         */
        @JvmStatic
        @JvmOverloads
        public fun allowed(hasJoinedBefore: Boolean = false): JoinResult {
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
         */
        @JvmStatic
        @JvmOverloads
        public fun allowed(
            message: Component,
            hasJoinedBefore: Boolean = false
        ): JoinResult = JoinResult(true, message, hasJoinedBefore)

        /**
         * Creates a new join result that denies the player from joining,
         * optionally specifying if the player
         * [has joined before][hasJoinedBefore], defaulting to false if not
         * specified.
         *
         * @param hasJoinedBefore if the player has joined before
         */
        @JvmStatic
        @JvmOverloads
        public fun denied(hasJoinedBefore: Boolean = false): JoinResult {
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
         */
        @JvmStatic
        @JvmOverloads
        public fun denied(
            message: Component,
            hasJoinedBefore: Boolean = false
        ): JoinResult = JoinResult(false, message, hasJoinedBefore)
    }
}

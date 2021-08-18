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
import net.kyori.adventure.text.Component.empty
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.Result
import org.kryptonmc.api.event.ResultedEvent

/**
 * Called when a player logs in and a player object has been
 * constructed for them (just before the state is switched to PLAY).
 *
 * @param player the player who joined
 * @param hasChangedName if this player has joined before
 */
class JoinEvent(
    val player: Player,
    val hasChangedName: Boolean
) : ResultedEvent<JoinResult> {

    // The message here is the default translatable component that vanilla Minecraft sends when a player joins.
    override var result = JoinResult.allowed(
        translatable(
            if (hasChangedName) "multiplayer.player.joined.renamed" else "multiplayer.player.joined",
            NamedTextColor.YELLOW,
            text(player.name)
        ),
        hasChangedName
    )
}

/**
 * The result of a [JoinEvent].
 *
 * @param message the join message
 * @param hasChangedName if the player joining has joined before
 */
class JoinResult(
    override val isAllowed: Boolean,
    val message: Component,
    val hasChangedName: Boolean
) : Result {

    companion object {

        /**
         * Creates a new join result that allows the player to join, optionally
         * specifying if the player [has joined before][hasJoinedBefore], defaulting
         * to false if not specified.
         *
         * @param hasJoinedBefore if the player has joined before
         */
        @JvmStatic
        @JvmOverloads
        fun allowed(hasJoinedBefore: Boolean = false) = JoinResult(true, empty(), hasJoinedBefore)

        /**
         * Creates a new join result that allows the player to join with the given
         * join [message], optionally specifying if the player [has joined before][hasJoinedBefore],
         * defaulting to false if not specified.
         *
         * @param message the join message
         * @param hasJoinedBefore if the player has joined before
         */
        @JvmStatic
        @JvmOverloads
        fun allowed(message: Component, hasJoinedBefore: Boolean = false) = JoinResult(true, message, hasJoinedBefore)

        /**
         * Creates a new join result that denies the player from joining, optionally
         * specifying if the player [has joined before][hasJoinedBefore], defaulting
         * to false if not specified.
         *
         * @param hasJoinedBefore if the player has joined before
         */
        @JvmStatic
        @JvmOverloads
        fun denied(hasJoinedBefore: Boolean = false) = JoinResult(false, empty(), hasJoinedBefore)

        /**
         * Creates a new join result that denies the player from joining with the
         * given join [message], optionally specifying if the player
         * [has joined before][hasJoinedBefore], defaulting to false if not specified.
         *
         * @param message the join message
         * @param hasJoinedBefore if the player has joined before
         */
        @JvmStatic
        @JvmOverloads
        fun denied(message: Component, hasJoinedBefore: Boolean = false) = JoinResult(false, message, hasJoinedBefore)
    }
}

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
import org.kryptonmc.api.event.type.DeniableEventWithResult
import org.kryptonmc.api.event.type.PlayerEvent
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Called when a player logs in and a player object has been constructed for
 * them.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface PlayerJoinEvent : PlayerEvent, DeniableEventWithResult<PlayerJoinEvent.Result> {

    /**
     * If the player has joined the server before.
     */
    @get:JvmName("hasJoinedBefore")
    public val hasJoinedBefore: Boolean

    /**
     * The result of a join event.
     */
    @JvmRecord
    @ImmutableType
    public data class Result(
        /**
         * The custom join message to send, or null, if no custom message is to
         * be sent.
         *
         * Note: some implementations may still send a join message.
         */
        public val message: Component?,
        /**
         * If the joining player has joined before.
         *
         * Implementations may choose to use this information to alter the join
         * message.
         */
        public val hasJoinedBefore: Boolean
    )
}

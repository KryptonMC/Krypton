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
 * Called when a player sends a chat message (not a command).
 */
public interface PlayerChatEvent : PlayerEvent, DeniableEventWithResult<PlayerChatEvent.Result> {

    /**
     * The message that the player has sent.
     */
    public val message: String

    /**
     * The result of a chat event.
     *
     * This allows you to modify the message that was sent by the player.
     * For example, you could replace bad words with asterisks.
     *
     * @property message The message that will be sent by the player.
     */
    @JvmRecord
    @ImmutableType
    public data class Result(public val message: Component)
}

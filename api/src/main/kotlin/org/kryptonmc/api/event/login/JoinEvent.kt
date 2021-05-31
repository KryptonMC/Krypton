/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.login

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.entity.entities.Player
import org.kryptonmc.api.event.CancellableEvent

/**
 * Called when a player logs in and a player object has been
 * constructed for them (just before the state is switched to PLAY).
 *
 * @param player the player who joined
 */
class JoinEvent(val player: Player) : CancellableEvent() {

    /**
     * The reason for cancelling this join event.
     */
    @Volatile
    var cancelledReason: Component = translatable("multiplayer.disconnect.kicked")

    /**
     * The message sent when the player joins the game.
     */
    @Volatile
    var message: Component = translatable("multiplayer.player.joined", NamedTextColor.YELLOW, listOf(text(player.name)))

    /**
     * Cancel this event with the specified [reason] for cancellation.
     *
     * This reason will be the exact disconnect message sent to the client.
     *
     * @param reason the reason for cancellation
     */
    fun cancel(reason: Component) {
        isCancelled = true
        cancelledReason = reason
    }
}

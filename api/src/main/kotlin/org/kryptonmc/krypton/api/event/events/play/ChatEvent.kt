package org.kryptonmc.krypton.api.event.events.play

import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.event.CancellableEvent

/**
 * Called when a player sends a chat message (not a command)
 *
 * @param player the player sending the message
 * @param message the message sent
 * @author Callum Seabrook
 */
data class ChatEvent(
    val player: Player,
    val message: String
) : CancellableEvent()
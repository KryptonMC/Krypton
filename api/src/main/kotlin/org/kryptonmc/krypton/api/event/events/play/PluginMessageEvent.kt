package org.kryptonmc.krypton.api.event.events.play

import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.event.Event
import org.kryptonmc.krypton.api.registry.NamespacedKey

/**
 * Called when a plugin message is received from a client.
 *
 * @param player the player who's client sent the message
 * @param channel the channel the message came from
 * @param message the message received (UTF-8 decoded binary data)
 * @author Callum Seabrook
 */
data class PluginMessageEvent(
    val player: Player,
    val channel: NamespacedKey,
    val message: String
) : Event
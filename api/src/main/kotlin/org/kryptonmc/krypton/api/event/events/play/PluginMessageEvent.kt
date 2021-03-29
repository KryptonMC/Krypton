package org.kryptonmc.krypton.api.event.events.play

import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.event.Event
import org.kryptonmc.krypton.api.registry.NamespacedKey
import java.util.*

/**
 * Called when a plugin message is received from a client.
 *
 * @param player the player who's client sent the message
 * @param channel the channel the message came from
 * @param message the message received
 * @author Callum Seabrook
 */
data class PluginMessageEvent(
    val player: Player,
    val channel: NamespacedKey,
    val message: ByteArray
) : Event {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PluginMessageEvent
        return player == other.player && channel == other.channel && message.contentEquals(other.message)
    }

    override fun hashCode(): Int {
        var result = player.hashCode()
        result = 31 * result + channel.hashCode()
        result = 31 * result + message.contentHashCode()
        return result
    }
}
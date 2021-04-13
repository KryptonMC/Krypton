package org.kryptonmc.krypton.api.event.events.login

import net.kyori.adventure.extra.kotlin.translatable
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.event.CancellableEvent

/**
 * Called when a player logs in and a player object has been
 * constructed for them (just before the state is switched to PLAY)
 *
 * @param player the player who joined
 * @author Callum Seabrook
 */
data class JoinEvent(val player: Player) : CancellableEvent() {

    @Volatile
    var cancelledReason: Component = translatable { key("multiplayer.disconnect.kicked") }

    /**
     * Cancel this event with the specified [reason] for cancellation
     *
     * This reason will be the exact disconnect message sent to the client
     *
     * @param reason the reason for cancellation
     */
    fun cancel(reason: Component) {
        isCancelled = true
        cancelledReason = reason
    }
}
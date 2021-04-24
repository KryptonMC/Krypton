package org.kryptonmc.krypton.api.event.events.login

import net.kyori.adventure.extra.kotlin.translatable
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.event.CancellableEvent
import java.net.InetSocketAddress
import java.util.UUID

/**
 * Called when a player has been authenticated, but they have not yet had
 * a player object constructed for them (when the state has not moved into PLAY
 * yet)
 *
 * @param username the username of the player logging in
 * @param uuid the UUID of the player logging in
 * @param address the address of the player logging in
 */
data class LoginEvent(
    val username: String,
    val uuid: UUID,
    val address: InetSocketAddress
) : CancellableEvent() {

    @Volatile var cancelledReason: Component = translatable { key("multiplayer.disconnect.kicked") }

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

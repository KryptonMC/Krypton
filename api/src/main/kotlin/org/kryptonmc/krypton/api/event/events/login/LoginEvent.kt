package org.kryptonmc.krypton.api.event.events.login

import org.kryptonmc.krypton.api.event.CancellableEvent
import java.net.InetSocketAddress
import java.util.*

/**
 * Called when a player has been authenticated, but they have not yet had
 * a player object constructed for them (when the state has not moved into PLAY
 * yet)
 *
 * @author Callum Seabrook
 */
data class LoginEvent(
    val username: String,
    val uuid: UUID,
    val address: InetSocketAddress
) : CancellableEvent()
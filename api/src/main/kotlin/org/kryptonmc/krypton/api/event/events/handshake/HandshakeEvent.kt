package org.kryptonmc.krypton.api.event.events.handshake

import org.kryptonmc.krypton.api.event.Event
import java.net.InetSocketAddress

/**
 * Called when a connection is made and a handshake packet is sent.
 *
 * There is no player data sent by this time, as this packet may either
 * indicate the client intends to login, or they intend to request the
 * status information for the server.
 *
 * @param address the address of the connecting client
 */
data class HandshakeEvent(val address: InetSocketAddress) : Event

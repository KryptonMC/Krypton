package org.kryptonmc.krypton.packet.data

import org.kryptonmc.krypton.packet.state.PacketState
import java.net.InetAddress

/**
 * Data sent in the [Handshake packet][org.kryptonmc.krypton.packet.in.handshake.PacketInHandshake].
 *
 * [address] and [port] are ignored by the Notchian server, and it is unclear why they are even sent,
 * but we also completely ignore them, as we already know the server's address and port.
 */
data class HandshakeData(
    val protocol: Int,
    val address: InetAddress,
    val port: UShort,
    val nextState: PacketState
)

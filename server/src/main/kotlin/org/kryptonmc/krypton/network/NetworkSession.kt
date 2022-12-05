package org.kryptonmc.krypton.network

import org.kryptonmc.krypton.packet.GenericPacket
import org.kryptonmc.krypton.packet.Packet
import java.net.SocketAddress

interface NetworkSession {

    fun connectAddress(): SocketAddress

    fun latency(): Int

    fun send(packet: Packet)

    fun write(packet: GenericPacket)
}

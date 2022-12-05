package org.kryptonmc.krypton.testutil

import org.kryptonmc.krypton.network.NetworkSession
import org.kryptonmc.krypton.packet.GenericPacket
import org.kryptonmc.krypton.packet.Packet
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.util.Stack
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class TestSession : NetworkSession {

    private val sentPackets = Stack<GenericPacket>()

    override fun connectAddress(): SocketAddress = InetSocketAddress(InetAddress.getLocalHost(), 25565)

    override fun latency(): Int = 0

    override fun send(packet: Packet) {
        write(packet)
    }

    override fun write(packet: GenericPacket) {
        sentPackets.push(packet)
    }

    fun checkSent(packet: GenericPacket) {
        assertFalse(sentPackets.isEmpty(), "No packets were sent!")
        assertEquals(packet, sentPackets.pop())
    }
}

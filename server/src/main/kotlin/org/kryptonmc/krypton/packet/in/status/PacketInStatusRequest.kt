package org.kryptonmc.krypton.packet.`in`.status

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.StatusPacket

/**
 * Sent by the client to request the server's status information
 */
class PacketInStatusRequest : StatusPacket(0x00) {

    // No data in packet - overridden to avoid throwing
    override fun read(buf: ByteBuf) = Unit
}

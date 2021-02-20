package org.kryptonmc.krypton.packet.`in`.status

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.StatusPacket

class PacketInStatusRequest : StatusPacket(0x00) {

    override fun read(buf: ByteBuf) {
        // no data in packet
    }
}
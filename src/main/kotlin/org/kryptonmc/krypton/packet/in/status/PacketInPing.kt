package org.kryptonmc.krypton.packet.`in`.status

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.StatusPacket

class PacketInPing : StatusPacket(0x01) {

    var payload = -1L
        private set

    override fun read(buf: ByteBuf) {
        payload = buf.readLong()
    }
}
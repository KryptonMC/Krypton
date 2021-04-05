package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketInHeldItemChange : PlayPacket(0x25) {

    var slot: Short = 0
        private set

    override fun read(buf: ByteBuf) {
        slot = buf.readShort()
    }
}
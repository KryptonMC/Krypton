package org.kryptonmc.krypton.packet.`in`

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketInKeepAlive : PlayPacket(0x10) {

    // ignored for now
    var keepAliveId: Long = 0L
        private set

    override fun read(buf: ByteBuf) {
        keepAliveId = buf.readLong()
    }
}
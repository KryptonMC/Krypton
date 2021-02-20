package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutKeepAlive(
    val keepAliveId: Long
) : PlayPacket(0x1F) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(keepAliveId)
    }
}
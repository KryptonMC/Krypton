package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutKeepAlive(
    val keepAliveId: Long
) : PlayPacket(0x1F) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(keepAliveId)
    }
}
package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutKeepAlive : PlayPacket(0x1F) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(System.currentTimeMillis())
    }
}
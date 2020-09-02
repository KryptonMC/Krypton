package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.play.PlayPacket

class PacketOutHeldItemChange : PlayPacket(0x40) {
    override fun write(buf: ByteBuf) {
        buf.writeByte(0)
    }
}

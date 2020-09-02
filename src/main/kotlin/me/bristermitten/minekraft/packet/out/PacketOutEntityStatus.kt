package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.play.PlayPacket

class PacketOutEntityStatus : PlayPacket(0x1C) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(1)
        buf.writeByte(28)
    }
}

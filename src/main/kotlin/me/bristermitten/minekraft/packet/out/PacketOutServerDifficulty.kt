package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutServerDifficulty : PlayPacket(0x0D) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(0)
        buf.writeBoolean(true)
    }
}
package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutPlayerPositionAndLook : PlayPacket(0x36) {

    override fun write(buf: ByteBuf) {
        buf.writeDouble(0.0)
        buf.writeDouble(0.0)
        buf.writeDouble(0.0)
        buf.writeFloat(0f)
        buf.writeFloat(0f)
        buf.writeByte(0)
        buf.writeVarInt(0)
    }
}

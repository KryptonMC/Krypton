package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutDeclareCommands : PlayPacket(0x12) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(0)
    }
}

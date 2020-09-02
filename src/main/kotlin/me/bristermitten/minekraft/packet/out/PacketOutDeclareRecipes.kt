package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.play.PlayPacket

class PacketOutDeclareRecipes : PlayPacket(0x5B) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(0)
    }
}

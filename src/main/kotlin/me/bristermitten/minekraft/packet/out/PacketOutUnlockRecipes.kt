package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutUnlockRecipes : PlayPacket(0x37) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(0)
        buf.writeBoolean(true)
        buf.writeBoolean(true)
        buf.writeBoolean(true)
        buf.writeBoolean(true)

        buf.writeVarInt(0)
        buf.writeVarInt(0)
    }
}

package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

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
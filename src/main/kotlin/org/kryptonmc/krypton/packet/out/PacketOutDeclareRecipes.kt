package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutDeclareRecipes : PlayPacket(0x5A) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(0)
    }
}
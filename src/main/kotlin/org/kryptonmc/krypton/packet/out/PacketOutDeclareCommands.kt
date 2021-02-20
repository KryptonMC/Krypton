package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutDeclareCommands : PlayPacket(0x12) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(0)
    }
}
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutCamera(private val id: Int) : PlayPacket(0x47) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(id)
    }
}

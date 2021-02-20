package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutHeldItemChange(
    val slot: Int
) : PlayPacket(0x3F) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(slot)
    }
}
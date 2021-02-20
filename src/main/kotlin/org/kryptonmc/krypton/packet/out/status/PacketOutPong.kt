package org.kryptonmc.krypton.packet.out.status

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.StatusPacket

class PacketOutPong(private val value: Long) : StatusPacket(0x01) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(value)
    }
}
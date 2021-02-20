package org.kryptonmc.krypton.packet.out

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutTimeUpdate(
    val worldAge: Long,
    val timeOfDay: Long
) : PlayPacket(0x4E) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(worldAge)
        buf.writeLong(timeOfDay)
    }
}
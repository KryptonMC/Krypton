package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutTimeUpdate(
    private val worldAge: Long,
    private val timeOfDay: Long
) : PlayPacket(0x4E) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(worldAge)
        buf.writeLong(timeOfDay)
    }
}
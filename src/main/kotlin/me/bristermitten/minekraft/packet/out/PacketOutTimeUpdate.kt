package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutTimeUpdate(
    val worldAge: Long,
    val timeOfDay: Long
) : PlayPacket(0x4E) {

    override fun write(buf: ByteBuf) {
        buf.writeLong(worldAge)
        buf.writeLong(timeOfDay)
    }
}
package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutHeldItemChange(
    val slot: Int
) : PlayPacket(0x3F) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(slot)
    }
}

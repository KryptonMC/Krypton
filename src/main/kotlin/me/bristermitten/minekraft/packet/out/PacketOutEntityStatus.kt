package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutEntityStatus(
    val entityId: Int
) : PlayPacket(0x1A) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(entityId)
        buf.writeByte(28)
    }
}

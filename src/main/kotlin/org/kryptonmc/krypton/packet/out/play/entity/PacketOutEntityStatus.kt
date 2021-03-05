package org.kryptonmc.krypton.packet.out.play.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket

// TODO: Add the rest of the entity statuses into this
class PacketOutEntityStatus(private val entityId: Int) : PlayPacket(0x1A) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(entityId)
        buf.writeByte(28)
    }
}
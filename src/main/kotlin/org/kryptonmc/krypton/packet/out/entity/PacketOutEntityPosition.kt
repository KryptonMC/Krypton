package org.kryptonmc.krypton.packet.out.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutEntityPosition(
    val entityId: Int,
    val deltaX: Short,
    val deltaY: Short,
    val deltaZ: Short,
    val onGround: Boolean
) : PlayPacket(0x27) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeShort(deltaX.toInt())
        buf.writeShort(deltaY.toInt())
        buf.writeShort(deltaZ.toInt())
        buf.writeBoolean(onGround)
    }
}
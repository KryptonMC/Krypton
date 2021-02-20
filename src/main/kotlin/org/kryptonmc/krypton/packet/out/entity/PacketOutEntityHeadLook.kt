package org.kryptonmc.krypton.packet.out.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.cardinal.Angle
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutEntityHeadLook(
    val entityId: Int,
    val headYaw: Angle
) : PlayPacket(0x3A) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeByte(headYaw.value.toInt())
    }
}
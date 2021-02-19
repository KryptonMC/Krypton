package me.bristermitten.minekraft.packet.out.entity

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entity.cardinal.Angle
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutEntityHeadLook(
    val entityId: Int,
    val headYaw: Angle
) : PlayPacket(0x3A) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeByte(headYaw.value.toInt())
    }
}
package me.bristermitten.minekraft.packet.out.entity

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutEntityPositionAndRotation(
    val entityId: Int,
    val deltaX: Short,
    val deltaY: Short,
    val deltaZ: Short,
    val yaw: Byte, // 1/256ths of a full rotation
    val pitch: Byte,
    val onGround: Boolean
) : PlayPacket(0x28) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeShort(deltaX.toInt())
        buf.writeShort(deltaY.toInt())
        buf.writeShort(deltaZ.toInt())
        buf.writeByte(yaw.toInt())
        buf.writeByte(pitch.toInt())
        buf.writeBoolean(onGround)
    }
}
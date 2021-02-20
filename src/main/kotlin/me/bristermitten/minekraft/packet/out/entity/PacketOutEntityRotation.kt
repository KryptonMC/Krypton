package me.bristermitten.minekraft.packet.out.entity

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entity.cardinal.Angle
import me.bristermitten.minekraft.extension.writeAngle
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket

class PacketOutEntityRotation(
    val entityId: Int,
    val yaw: Angle,
    val pitch: Angle,
    val onGround: Boolean
) : PlayPacket(0x29) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeAngle(yaw)
        buf.writeAngle(pitch)
        buf.writeBoolean(onGround)
    }
}
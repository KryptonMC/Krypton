package org.kryptonmc.krypton.packet.out.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.cardinal.Angle
import org.kryptonmc.krypton.extension.writeAngle
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

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
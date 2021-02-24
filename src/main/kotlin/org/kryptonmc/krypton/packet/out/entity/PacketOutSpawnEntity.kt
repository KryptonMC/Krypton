package org.kryptonmc.krypton.packet.out.entity

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.Entity
import org.kryptonmc.krypton.space.toAngle
import org.kryptonmc.krypton.extension.writeAngle
import org.kryptonmc.krypton.extension.writeUUID
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutSpawnEntity(val entity: Entity) : PlayPacket(0x00) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entity.entityId)
        buf.writeUUID(entity.uuid)
        buf.writeVarInt(entity.type.type)

        buf.writeDouble(entity.location.x)
        buf.writeDouble(entity.location.y)
        buf.writeDouble(entity.location.z)
        buf.writeAngle(entity.location.yaw.toAngle())
        buf.writeAngle(entity.location.pitch.toAngle())

        buf.writeInt(entity.data)

        buf.writeShort(entity.velocityX.toInt())
        buf.writeShort(entity.velocityY.toInt())
        buf.writeShort(entity.velocityZ.toInt())
    }
}
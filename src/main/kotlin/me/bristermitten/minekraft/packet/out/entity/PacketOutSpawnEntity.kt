package me.bristermitten.minekraft.packet.out.entity

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entity.Entity
import me.bristermitten.minekraft.extension.writeUUID
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket
import me.bristermitten.minekraft.world.toAngle

class PacketOutSpawnEntity(val entity: Entity) : PlayPacket(0x00) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entity.entityId)
        buf.writeUUID(entity.uuid)
        buf.writeVarInt(entity.type.type)

        buf.writeDouble(entity.location.x)
        buf.writeDouble(entity.location.y)
        buf.writeDouble(entity.location.z)
        buf.writeByte(entity.location.yaw.toAngle())
        buf.writeByte(entity.location.pitch.toAngle())

        buf.writeInt(entity.data)

        buf.writeShort(entity.velocityX.toInt())
        buf.writeShort(entity.velocityY.toInt())
        buf.writeShort(entity.velocityZ.toInt())
    }
}
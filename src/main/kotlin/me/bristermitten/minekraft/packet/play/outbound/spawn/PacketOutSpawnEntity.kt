package me.bristermitten.minekraft.packet.play.outbound.spawn

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entities.EntityType
import me.bristermitten.minekraft.entities.Location
import me.bristermitten.minekraft.extension.writeUUID
import me.bristermitten.minekraft.packet.play.outbound.EntityPacket
import me.bristermitten.minekraft.packet.play.outbound.EntityPacketType
import java.util.*

class PacketOutSpawnEntity(
    override val entityID: Int,
    val objectUUID: UUID,
    val type: EntityType,
    val location: Location,
    val data: Int,
    val velocity: Location
) : EntityPacket(EntityPacketType.SPAWN_ENTITY) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(entityID)

        buf.writeUUID(objectUUID)
        buf.writeInt(type.id)

        buf.writeDouble(location.x)
        buf.writeDouble(location.y)
        buf.writeDouble(location.z)
        buf.writeFloat(location.pitch)
        buf.writeFloat(location.yaw)

        buf.writeInt(data)

        buf.writeDouble(velocity.x)
        buf.writeDouble(velocity.y)
        buf.writeDouble(velocity.z)
    }
}
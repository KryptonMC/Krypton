package me.bristermitten.minekraft.packet.play.outbound.spawn

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entities.EntityType
import me.bristermitten.minekraft.entities.Location
import me.bristermitten.minekraft.extension.writeUUID
import me.bristermitten.minekraft.packet.play.outbound.EntityPacket
import me.bristermitten.minekraft.packet.play.outbound.EntityPacketType
import java.util.*

class PacketOutSpawnLivingEntity(
    override val entityID: Int,
    val entityUUID: UUID,
    val type: EntityType,
    val location: Location,
    val headPitch: Float,
    val velocity: Location
) : EntityPacket(EntityPacketType.SPAWN_LIVING_ENTITY) {

    override fun write(buf: ByteBuf) {
        super.write(buf)

        buf.writeUUID(entityUUID)
        buf.writeInt(type.id)

        buf.writeDouble(location.x)
        buf.writeDouble(location.y)
        buf.writeDouble(location.z)
        buf.writeFloat(location.yaw)
        buf.writeFloat(location.pitch)
        buf.writeFloat(headPitch)

        buf.writeDouble(velocity.x)
        buf.writeDouble(velocity.y)
        buf.writeDouble(velocity.z)
    }
}
package me.bristermitten.minekraft.packet.play.outbound.spawn

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entities.Location
import me.bristermitten.minekraft.packet.play.outbound.EntityPacket
import me.bristermitten.minekraft.packet.play.outbound.EntityPacketType

class PacketOutSpawnExperienceOrb(
    override val entityID: Int,
    val location: Location,
    val count: Short
) : EntityPacket(EntityPacketType.SPAWN_EXPERIENCE_ORB) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(entityID)

        buf.writeDouble(location.x)
        buf.writeDouble(location.y)
        buf.writeDouble(location.z)

        buf.writeShort(count.toInt())
    }
}
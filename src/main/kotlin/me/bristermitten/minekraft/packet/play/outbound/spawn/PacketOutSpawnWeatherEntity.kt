package me.bristermitten.minekraft.packet.play.outbound.spawn

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entities.Location
import me.bristermitten.minekraft.packet.play.outbound.EntityPacket
import me.bristermitten.minekraft.packet.play.outbound.EntityPacketType

class PacketOutSpawnWeatherEntity(
    override val entityID: Int,
    val type: Int,
    val location: Location
) : EntityPacket(EntityPacketType.SPAWN_WEATHER_ENTITY) {

    override fun write(buf: ByteBuf) {
        buf.writeInt(entityID)

        buf.writeByte(type)

        buf.writeDouble(location.x)
        buf.writeDouble(location.y)
        buf.writeDouble(location.z)
    }
}
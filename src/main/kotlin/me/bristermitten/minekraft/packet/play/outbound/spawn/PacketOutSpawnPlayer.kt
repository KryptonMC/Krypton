package me.bristermitten.minekraft.packet.play.outbound.spawn

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.entities.Location
import me.bristermitten.minekraft.extension.writeUUID
import me.bristermitten.minekraft.packet.play.outbound.EntityPacket
import me.bristermitten.minekraft.packet.play.outbound.EntityPacketType
import java.util.*

class PacketOutSpawnPlayer(
    override val entityID: Int,
    val playerUUID: UUID,
    val location: Location
) : EntityPacket(EntityPacketType.SPAWN_PLAYER) {

    override fun write(buf: ByteBuf) {
        super.write(buf)
        buf.writeUUID(playerUUID)

        buf.writeDouble(location.x)
        buf.writeDouble(location.y)
        buf.writeDouble(location.z)
        buf.writeFloat(location.yaw)
        buf.writeFloat(location.pitch)
    }
}